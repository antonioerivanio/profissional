package br.gov.ce.tce.srh.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.hibernate.EntityMode;
import org.hibernate.Session;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.hibernate.metadata.ClassMetadata;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.Revisao;
import br.gov.ce.tce.srh.domain.Revisao.Restricao;
import br.gov.ce.tce.srh.domain.TipoRevisao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 *
 * @author robson.castro
 * 
 */
@Repository
public class AuditoriaDAOImpl implements AuditoriaDAO {

	static Logger logger = Logger.getLogger(AuditoriaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private AuditQuery createAuditQuery(Revisao revisao) {
		
		if (revisao.getPeriodoInicial() != null
				&& revisao.getPeriodoFinal() != null
				&& revisao.getPeriodoInicial().after(revisao.getPeriodoFinal())) {
			throw new SRHRuntimeException("A data inicial deve ser anterior à data final.");
		}
		
		AuditQuery auditQuery = getAuditReader().createQuery().forRevisionsOfEntity(revisao.getEntidade(), false, true);
		
		if (revisao.getTipoRevisao() != null) {
			auditQuery.add(AuditEntity.revisionType().eq(getRevisionType(revisao.getTipoRevisao())));
		}

		if (revisao.getPeriodoInicial() != null) {
			auditQuery.add(AuditEntity.revisionProperty("dataAuditoria").ge(
					revisao.getPeriodoInicial()));
		}
		
		if (revisao.getPeriodoFinal() != null) {
			// Define a data final como sendo a hora, minuto e segundo zero do dia seguinte:
			Calendar periodoFinal = Calendar.getInstance();
			periodoFinal.setTime(revisao.getPeriodoFinal());
			periodoFinal.add(Calendar.DATE, +1);
			auditQuery.add(AuditEntity.revisionProperty("dataAuditoria").lt(periodoFinal.getTime()));
		}
		
		if (revisao.getUsuario() != null) {
			auditQuery.add(AuditEntity.revisionProperty("usuario").eq(revisao.getUsuario()));
		}

		Field chavePrimaria = getChavePrimaria(revisao.getEntidade());
		// Adiciona as restricoes definidas pelo usuario:
		for (Restricao restricao : revisao.getRestricoes()) {
			try {
				if (revisao.getEntidade().getDeclaredField(restricao.getAtributo()).getType().equals(String.class)) {
					auditQuery.add(AuditEntity.property(restricao.getAtributo()).like(restricao.getValor()));
				} else if (chavePrimaria != null && chavePrimaria.getName().equals(restricao.getAtributo())) {
					auditQuery.add(AuditEntity.id().eq(restricao.getValor()));
				} else {
					auditQuery.add(AuditEntity.property(restricao.getAtributo()).eq(restricao.getValor()));
				}
			} catch (Exception e) {
				// Nunca sera lancada
			}
		}
		
		auditQuery.addOrder(AuditEntity.revisionProperty("dataAuditoria").desc());
				
		return auditQuery;
	}
	
	
	protected AuditReader getAuditReader() {
		return AuditReaderFactory.get(entityManager);
	}
	
	
	private Field getChavePrimaria(Class<?> entidade) {
		if (entidade != null) {
			Field[] fields = entidade.getDeclaredFields();
			for (Field field : fields) {
				List<Annotation> annotations = Arrays.asList(field.getAnnotations());
				// Adiciona somente se o atributo nao for anotado com @Id - chave primária:
				for (Annotation annotation : annotations) {
					if (annotation instanceof Id) {
						return field;
					}
				}
			}
		}
		return null;
	}
	
	
	private RevisionType getRevisionType(TipoRevisao tipoRevisao) {
		if (tipoRevisao == TipoRevisao.INCLUSAO) {
			return RevisionType.ADD;
		} else if (tipoRevisao == TipoRevisao.ALTERACAO) {
			return RevisionType.MOD;
		} else if (tipoRevisao == TipoRevisao.EXCLUSAO) {
			return RevisionType.DEL;
		}
		return null;
	}
	
	
	private TipoRevisao getTipoRevisao(RevisionType tipoRevisao) {
		if (tipoRevisao == RevisionType.ADD) {
			return TipoRevisao.INCLUSAO;
		} else if (tipoRevisao == RevisionType.MOD) {
			return TipoRevisao.ALTERACAO;
		} else if (tipoRevisao == RevisionType.DEL) {
			return TipoRevisao.EXCLUSAO;
		}
		return null;
	}
	
	
	@Override
	public Set<Class<?>> getEntidadesAuditadas() {
		Map<String, ClassMetadata> classesMetadata = getSession().getSessionFactory().getAllClassMetadata();
		Set<Class<?>> entidadesAuditadas = new TreeSet<Class<?>>(new Comparator<Class<?>>() {
			@Override
			public int compare(Class<?> o1, Class<?> o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		for (ClassMetadata classMetadata : classesMetadata.values()) {
			Class<?> entidade = classMetadata.getMappedClass(EntityMode.POJO);
			if (entidade.isAnnotationPresent(Audited.class)) {
				entidadesAuditadas.add(entidade);
			}
		}
		return entidadesAuditadas;
	}
	
	protected Session getSession() {
		return ((Session)entityManager.getDelegate());
	}
	
	/**
	 * Obtem os atributos nao-transientes da entidade que sejam tipos simples como
	 * Boolean, numeros, String, Date, Calendar, etc...
	 * @return
	 */
	@Override
	public Set<Field> getAtributosSimplesEntidade(Class<?> entidade) {
		Set<Field> atributos = new TreeSet<Field>(new Comparator<Field>() {
			@Override
			public int compare(Field o1, Field o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		if (entidade != null) {
			Field[] fields = entidade.getDeclaredFields();
			for (Field field : fields) {
				//Adiciona somente atributos cujos tipos sejam dos pacotes java.lang e java.util:
				if (isSimpleField(field)) {
					List<Annotation> annotations = Arrays.asList(field.getAnnotations());
					boolean atributoValido = true;
					//Adiciona somente se o atributo nao for anotado com @Transient ou com @NotAudited:
					for (Annotation annotation : annotations) {
						if (annotation instanceof Transient || annotation instanceof NotAudited) {
							atributoValido = false;
							break;
						}
					}
					if (atributoValido) {
						atributos.add(field);
					}
				}
			}
		}
		return atributos;
	}
	
	private boolean isSimpleField(Field field) {
		if (Modifier.isFinal(field.getModifiers()) || 
			Modifier.isStatic(field.getModifiers()) || 
			Modifier.isTransient(field.getModifiers())) {
			return false;
		}
		Class<?> type = field.getType();
		return type.getPackage() == null || type.getPackage().equals(Package.getPackage("java.lang")) ||
			Date.class.isAssignableFrom(type) || Calendar.class.isAssignableFrom(type);
	}


	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public int count(Revisao revisao) {
		return ((Number) createAuditQuery(revisao).addProjection(AuditEntity.revisionProperty("id").count()).getSingleResult()).intValue();
	}


	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	@SuppressWarnings("unchecked")
	public List<Revisao> search(Revisao revisao, int first, int rows) {
		
		AuditQuery auditQuery = createAuditQuery(revisao);
		auditQuery.setFirstResult(first);
		auditQuery.setMaxResults(rows);

		List<Object[]> resultList = auditQuery.getResultList();
		
		List<Revisao> listaRevisao = new ArrayList<Revisao>();
		
		for (Object[] triple : resultList) {
			Object entidade = triple[0];
			Revisao revisaoEntidade = ((Revisao) triple[1]).clone();
			RevisionType tipoRevisao = (RevisionType) triple[2];			
			
			revisaoEntidade.setTipoRevisao(getTipoRevisao(tipoRevisao));
			
			// Copia os atributos da entidade encontrada:
			Set<Field> fields = getAtributosSimplesEntidade(entidade.getClass());
			for (Field field : fields) {
				field.setAccessible(true);
				
				if(field.getName().equalsIgnoreCase("id")){
					try {
						revisaoEntidade.setIdRegistro((Long)field.get(entidade));						
					} catch (Exception e) {
				
					}
					break;
				}
				
//				try {
//					revisaoEntidade.getRestricoes().add(new Restricao(field.getName(), field.getType(),	field.get(entidade)));
//				} catch (Exception e) {
//					// Excecao nunca sera lancada
//				}
			
			}

			listaRevisao.add(revisaoEntidade);
		}
		return listaRevisao;
	}

}
