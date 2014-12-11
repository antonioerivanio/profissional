package br.gov.ce.tce.srh.dao;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.EntityManager;
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

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.Revisao;
import br.gov.ce.tce.srh.domain.Revisao.Restricao;
import br.gov.ce.tce.srh.domain.TipoRevisao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;


@Repository
public class AuditoriaDAOImpl implements AuditoriaDAO {

	static Logger logger = Logger.getLogger(AuditoriaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;	
	public void setEntityManager(EntityManager entityManager) {this.entityManager = entityManager;}
	
	protected AuditReader getAuditReader() {return AuditReaderFactory.get(entityManager);}
	
	protected Session getSession() {return ((Session)entityManager.getDelegate());}
	
	
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
			auditQuery.add(AuditEntity.revisionProperty("dataAuditoria").ge(revisao.getPeriodoInicial()));
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
		
		if (revisao.getRestricao() != null){
			
			if(revisao.getRestricao().getTipo().equals(Long.class)){
				auditQuery.add(AuditEntity.id().eq(revisao.getRestricao().getValor()));	
			} else {	
				auditQuery.add(AuditEntity.relatedId(revisao.getRestricao().getNome()).eq(revisao.getRestricao().getValor()));
			}			
		}
		
		auditQuery.addOrder(AuditEntity.revisionProperty("dataAuditoria").desc());
		
		return auditQuery;
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
	
	
	@Override
	public Set<Field> getAtributosEntidade(Class<?> entidade) {
		Set<Field> atributos = new TreeSet<Field>(new Comparator<Field>() {
			@Override
			public int compare(Field o1, Field o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
		if (entidade != null) {
			Field[] fields = entidade.getDeclaredFields();
			for (Field field : fields) {
				List<Annotation> annotations = Arrays.asList(field.getAnnotations());
				boolean atributoValido = true;
//				Adiciona somente se o atributo nao for anotado com @Transient ou com @NotAudited
//				ou não for da classe Funcional nem Pessoal:
				for (Annotation annotation : annotations) {
					if ( annotation instanceof Transient 
							|| annotation instanceof NotAudited
							|| field.getType() == Funcional.class
							|| field.getType() == Pessoal.class ) {
						
						atributoValido = false;
						break;
					}
				}
				if (atributoValido) {
					atributos.add(field);
				}
			}
		}
		return atributos;
	}
	

	private boolean isSimpleClass(Class<?> tipo) {
		return tipo.getPackage() == null || tipo.getPackage().equals(Package.getPackage("java.lang")) ||
			Date.class.isAssignableFrom(tipo) || Calendar.class.isAssignableFrom(tipo);
	}


	@Override
	@Transactional(propagation = Propagation.SUPPORTS)
	public int count(Revisao revisao) {
		
		int count = 0;				
		
		if(revisao.getPessoal() != null){
			
			String entidadeNome = revisao.getEntidade().getSimpleName();
		
			if(entidadeNome.equalsIgnoreCase("Dependente")){
				
				revisao.setRestricao(new Restricao("responsavel", revisao.getPessoal().getClass(), revisao.getPessoal().getId()));			
				count = ((Number) createAuditQuery(revisao).addProjection(AuditEntity.revisionProperty("id").count()).getSingleResult()).intValue();
			
			} else if(entidadeNome.equalsIgnoreCase("Pessoal")){
				
				revisao.setRestricao(new Restricao("id", Long.class, revisao.getPessoal().getId()));			
				count = ((Number) createAuditQuery(revisao).addProjection(AuditEntity.revisionProperty("id").count()).getSingleResult()).intValue();
				
			} else if(entidadeNome.equalsIgnoreCase("Licenca")){
				
				revisao.setRestricao(new Restricao("pessoal", revisao.getPessoal().getClass(), revisao.getPessoal().getId()));			
				count = ((Number) createAuditQuery(revisao).addProjection(AuditEntity.revisionProperty("id").count()).getSingleResult()).intValue();
				
			} else if(entidadeNome.equalsIgnoreCase("Funcional")){
				
				List<Funcional> funcionais = revisao.getFuncionais();
				
				for (Funcional funcional : funcionais) {
					revisao.setRestricao(new Restricao("id", Long.class, funcional.getId()));			
					count += ((Number) createAuditQuery(revisao).addProjection(AuditEntity.revisionProperty("id").count()).getSingleResult()).intValue();
				}
				
			} else {
				
				List<Funcional> funcionais = revisao.getFuncionais();
				
				for (Funcional funcional : funcionais) {
					revisao.setRestricao(new Restricao("funcional", funcional.getClass(), funcional.getId()));			
					count += ((Number) createAuditQuery(revisao).addProjection(AuditEntity.revisionProperty("id").count()).getSingleResult()).intValue();
				}				
			}		
		
		} else {
			revisao.setRestricao(null);
			count = ((Number) createAuditQuery(revisao).addProjection(AuditEntity.revisionProperty("id").count()).getSingleResult()).intValue();
		}
			
		return count;
	}


	@Transactional(propagation = Propagation.SUPPORTS)
	@Override
	@SuppressWarnings("unchecked")
	public List<Revisao> search(Revisao revisao, int first, int rows, String nomeColuna) {		
		
		List<Object[]> resultList = new ArrayList<Object[]>();		
		
		if(revisao.getPessoal() != null){
		
			String entidadeNome = revisao.getEntidade().getSimpleName();		
				
			if(entidadeNome.equalsIgnoreCase("Dependente")){
				
				revisao.setRestricao(new Restricao("responsavel", revisao.getPessoal().getClass(), revisao.getPessoal().getId()));				
				AuditQuery auditQuery = createAuditQuery(revisao);
				auditQuery.setFirstResult(first);
				auditQuery.setMaxResults(rows);	
				resultList = auditQuery.getResultList();
				
				
			} else if(entidadeNome.equalsIgnoreCase("Pessoal")){
				
				revisao.setRestricao(new Restricao("id", Long.class, revisao.getPessoal().getId()));				
				AuditQuery auditQuery = createAuditQuery(revisao);
				auditQuery.setFirstResult(first);
				auditQuery.setMaxResults(rows);	
				resultList = auditQuery.getResultList();
				
				
			} else if(entidadeNome.equalsIgnoreCase("Licenca")){
				
				revisao.setRestricao(new Restricao("pessoal", revisao.getPessoal().getClass(), revisao.getPessoal().getId()));				
				AuditQuery auditQuery = createAuditQuery(revisao);
				auditQuery.setFirstResult(first);
				auditQuery.setMaxResults(rows);	
				resultList = auditQuery.getResultList();				
				
			} else if(entidadeNome.equalsIgnoreCase("Funcional")){
				
				List<Funcional> funcionais = revisao.getFuncionais();
				
				for (Funcional funcional : funcionais) {					
					revisao.setRestricao(new Restricao("id", Long.class, funcional.getId()));					
					AuditQuery auditQuery = createAuditQuery(revisao);		
					resultList.addAll(auditQuery.getResultList());
				}				
				
			} else {
				
				List<Funcional> funcionais = revisao.getFuncionais();
				
				for (Funcional funcional : funcionais) {
					revisao.setRestricao(new Restricao("funcional", funcional.getClass() , funcional.getId()));					
					AuditQuery auditQuery = createAuditQuery(revisao);		
					resultList.addAll(auditQuery.getResultList());
				}				
			}		
		} else {
			revisao.setRestricao(null);
			AuditQuery auditQuery = createAuditQuery(revisao);
			auditQuery.setFirstResult(first);
			auditQuery.setMaxResults(rows);
			resultList = auditQuery.getResultList();
		}
		
		List<Revisao> listaRevisao = new ArrayList<Revisao>();
		
		for (Object[] triple : resultList) {
			Object entidade = triple[0];
			Revisao revisaoEntidade = ((Revisao) triple[1]).clone();
			RevisionType tipoRevisao = (RevisionType) triple[2];			
			
			revisaoEntidade.setTipoRevisao(this.getTipoRevisao(tipoRevisao));
			
			// Copia os atributos da entidade encontrada:
			Set<Field> fields = this.getAtributosEntidade(entidade.getClass());
			for (Field field : fields) {
				
				field.setAccessible(true);
				
				if(field.getName().equalsIgnoreCase("id")){
					try {
						revisaoEntidade.setIdRegistro((Long)field.get(entidade));						
					} catch (Exception e) {}
				}
				
				if(field.getName().equalsIgnoreCase(nomeColuna)){
					
					String nomeDoMetodo;												
					Method method;
					Object valor = null;					
					
					try {
					
						nomeDoMetodo = "get" + nomeColuna.substring(0, 1).toUpperCase() + nomeColuna.substring(1);
						method = entidade.getClass().getMethod(nomeDoMetodo);
						valor = method.invoke(entidade);					
					
					} catch (NoSuchMethodException e) {
						
						try {
							
							nomeDoMetodo = "is" + nomeColuna.substring(0, 1).toUpperCase() + nomeColuna.substring(1);
							method = entidade.getClass().getMethod(nomeDoMetodo);
							valor = method.invoke(entidade);
						
						} catch (Exception ex) {ex.printStackTrace();}	
					
					} catch (Exception e) {e.printStackTrace();}					
					
					if( valor != null && !isSimpleClass(valor.getClass()) ){
						try {							
							valor = valor.getClass().getMethod("getId").invoke(valor);						
						} catch (Exception e) {e.printStackTrace();}
					}
					
					revisaoEntidade.setValorColuna(valor);
				}
			}

			listaRevisao.add(revisaoEntidade);
		}
		
		Collections.sort(listaRevisao);		
		
		return listaRevisao;
	}	

}
