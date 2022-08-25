package br.gov.ce.tce.srh.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.AfastamentoESocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.exception.AfastamentoEsocialException;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class AfastamentoESocialDAO {
	static Logger logger = Logger.getLogger(AfastamentoESocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public void excluir(AfastamentoESocial entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}
	
	public AfastamentoESocial getEvento2230ByServidor(Funcional servidorFuncional, Licenca licenca)  throws AfastamentoEsocialException  {
		try {
			
			String sql = getParametrosWhere(licenca, getSQLEventoS2230());
			
			Query query = entityManager.createNativeQuery(sql,  AfastamentoESocial.class);
			query.setParameter("idFuncional", servidorFuncional.getId());
			
			adicionarParametrosWhere(licenca, query);
			
			return (AfastamentoESocial) query.getSingleResult();
		} catch (NoResultException e) {
			throw new AfastamentoEsocialException("Nenhum Afastamento encontrado!");
		}
	}
	
	private String getParametrosWhere(Licenca licenca, StringBuilder sql) {
		if(licenca != null && licenca.isDataInicioFimLicencaNotNull()) {
			sql.append("AND tb_licenca.inicio = :dataLicencaInicio ");
			sql.append("AND tb_licenca.fim  =:dataLicencaFim ");
			sql.append("OR tb_licenca.fim IS NULL ");
		}
		
		return sql.toString();
	}
	
	private void adicionarParametrosWhere(Licenca licenca, Query query) {
		if(licenca != null  && licenca.isDataInicioFimLicencaNotNull()) {
			query.setParameter("dataLicencaInicio", licenca.getInicio(), TemporalType.DATE);
			query.setParameter("dataLicencaFim", licenca.getFim(), TemporalType.DATE);
		}
	}
	
	public AfastamentoESocial getById(Long id) {
		return entityManager.find(AfastamentoESocial.class, id);
	}
	
	public StringBuilder getSQLEventoS2230() {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT 0 AS ID, ");
		sql.append(" tb_funcional.id||'-'||tb_tipolicenca.id ||'-'||tb_licenca.inicio AS REFERENCIA, ");
		sql.append("tb_funcional.id AS idfuncional,  ");
		sql.append("NULL                  AS RETIFICAR_RECIBO, ");
		sql.append("NULL                  AS OCORRENCIA_ID, ");
		sql.append("NULL                  AS OCORRENCIA_COD_ESTADO, ");
		sql.append("tb_pessoal.cpf AS CPF_TRAB, ");
		sql.append(" '0'|| tb_funcional.matricula AS MATRICULA, ");
		sql.append("tb_licenca.inicio AS DT_INI_AFAST, ");
		sql.append("TO_CHAR(tb_tipolicenca.id,'09') AS COD_MOT_AFAST, ");
		sql.append("tb_tipolicenca.descricao AS OBSERVACAO, ");
		sql.append("tb_licenca.fim AS DT_TERM_AFAST ");
		sql.append("FROM srh.tb_licenca ");
		sql.append("INNER JOIN srh.tb_pessoal ");
		sql.append("ON srh.tb_licenca.idpessoal = srh.tb_pessoal.id ");
		sql.append("INNER JOIN srh.tb_tipolicenca ");
		sql.append("ON srh.tb_licenca.idtipolicenca = srh.tb_tipolicenca.id ");
		sql.append("INNER JOIN srh.tb_funcional ");
		sql.append("ON  srh.tb_funcional.idpessoal = srh.tb_pessoal.id ");
		sql.append("INNER JOIN srh.tb_ocupacao ");
		sql.append("ON  srh.tb_funcional.IDOCUPACAO = srh.tb_ocupacao.id ");
		sql.append("WHERE  tb_funcional.id = :idFuncional  ");
	    
		return sql;
	}
	
	@Transactional
	public AfastamentoESocial salvar(AfastamentoESocial entidade) {
		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		entityManager.detach(entidade);
		
		return entityManager.merge(entidade);
	}
	
	private Long getMaxId() {
		Query query = entityManager.createNativeQuery("SELECT max(ID) FROM srh.esocial_afastamento ");
		return query.getSingleResult() == null ? 1 : ((BigDecimal) query.getSingleResult()).longValue() + 1;
	}

	public int count(String nome, String cpf) {
		StringBuffer sql = new StringBuffer();

		sql.append(" Select count(e) FROM AfastamentoESocial e inner join e.funcional f WHERE 1=1 ");

		if (nome != null && !nome.isEmpty()) {
			sql.append("  and upper( f.nome ) like :nome ");
		}
		
		if (cpf != null && !cpf.isEmpty()) {
			sql.append("  AND f.cpf = :cpf ");
		}
						
		Query query = entityManager.createQuery(sql.toString());

		if (nome != null && !nome.isEmpty()) {
			query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		}
		
		if (cpf != null && !cpf.isEmpty()) {
			query.setParameter("cpf", SRHUtils.removerMascara( cpf ));
		}
		return ((Long) query.getSingleResult()).intValue();
	}
	

	@SuppressWarnings("unchecked")
	public List<AfastamentoESocial> search(String nome, String cpf, Integer first, Integer rows) {
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT e FROM AfastamentoESocial e inner join fetch e.funcional f WHERE 1=1 ");

		if (nome != null && !nome.isEmpty()) {
			sql.append("  and upper( f.nome ) like :nome ");
		}
		
		if (cpf != null && !cpf.isEmpty())
			sql.append("  AND f.cpf = :cpf ");

		sql.append("  ORDER BY f.nome ");

		Query query = entityManager.createQuery(sql.toString());

		if (nome != null && !nome.isEmpty()) {
			query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		}
		
		if (cpf != null && !cpf.isEmpty()) {
			query.setParameter("cpf", SRHUtils.removerMascara( cpf ));
		}
		if (first != null && first >= 0)
			query.setFirstResult(first);
		if (rows != null && rows > 0)
			query.setMaxResults(rows);

		return query.getResultList();
	}

}
