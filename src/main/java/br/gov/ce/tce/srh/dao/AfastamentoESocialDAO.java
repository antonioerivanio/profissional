package br.gov.ce.tce.srh.dao;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

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
import br.gov.ce.tce.srh.enums.TipoLicenca;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class AfastamentoESocialDAO {
	static Logger logger = Logger.getLogger(EstagiarioESocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public void excluir(AfastamentoESocial entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}
	
	public AfastamentoESocial getEvento2230ByServidor(Funcional servidorFuncional, boolean possuiCargo) {
		try {
			StringBuffer campoAdicionalWhere = new StringBuffer(" tb_funcional.id = :idFuncional ");
			
			Query query = entityManager.createNativeQuery(getSQLEventoS2230(possuiCargo, getSqlColunasEJoins(null), campoAdicionalWhere), AfastamentoESocial.class);
			query.setParameter("idFuncional", servidorFuncional.getId());

			return (AfastamentoESocial) query.getSingleResult();			
		} catch (NoResultException e) {
			return null;
		}
	}
	
	/**
	 * Metodo que retorna a lista de afasmentos por id e tipo de licença do servidor e que não esteja salvos na tabela ESOCIAL_AFASTAMENTO
	 * @author erivanio.cruz
	 * @param servidorFuncional
	 * @param possuiCargo
	 * @return
	 */
	public List<AfastamentoESocial> getEvento2230ByServidorList(Funcional servidorFuncional, boolean possuiCargo) {
		try {
			StringBuffer campoAdicionalWhere = new StringBuffer(" NOT EXISTS (SELECT 1 from srh.ESOCIAL_AFASTAMENTO ea " );
					campoAdicionalWhere.append(" WHERE ea.DT_INI_AFAST = srh.TB_LICENCA.INICIO ");
					campoAdicionalWhere.append(" AND ea.DT_TERM_AFAST = srh.TB_LICENCA.fim ) ");
					campoAdicionalWhere.append(" AND tb_funcional.id = :idFuncional   AND tb_tipolicenca.id IN (:idTipoLicenca)" );
			
			Query query = entityManager.createNativeQuery(getSQLEventoS2230(possuiCargo, getSqlColunasEJoins(null), campoAdicionalWhere), AfastamentoESocial.class);
			query.setParameter("idFuncional", servidorFuncional.getId());
			query.setParameter("idTipoLicenca",    TipoLicenca.getTodosCodigos());
			
			return query.getResultList();
			
		} catch (NoResultException e) {
			return new ArrayList<AfastamentoESocial>();
		}
	}
	
	public AfastamentoESocial getById(Long id) {
		return entityManager.find(AfastamentoESocial.class, id);
	}
	
	/**
	 * Metodo criado para adicionar sql codicional no JOIN e no WHERE 
	 * @author erivanio.cruz
	 * @param possuiCargo
	 * @param sqlAdicionalWhere
	 * @return string
	 */
	public String getSQLEventoS2230(boolean possuiCargo, StringBuffer sqlColunasEJoinsAdicional, StringBuffer whereSqlAdicional) {
		StringBuilder sql =new StringBuilder();
		
		if(Objects.nonNull(sqlColunasEJoinsAdicional)) {
			sql. append(sqlColunasEJoinsAdicional);
		}
		
		sql.append("WHERE  ");
		
		if(Objects.nonNull( whereSqlAdicional)) {
			sql.append(whereSqlAdicional);
		}
		
		return sql.toString();
	}
	
	/**
	 * @author erivanio.cruz
	 * @return string
	 */
	private StringBuffer getSqlColunasEJoins(StringBuffer joinsAdicional) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT rownum AS ID, ");
		sql.append(" tb_funcional.id ||'-'|| tb_ocupacao.id AS REFERENCIA, ");
		sql.append("tb_funcional.id AS idfuncional,  ");
		sql.append("NULL                  AS RETIFICAR_RECIBO, ");
		sql.append("NULL                  AS OCORRENCIA_ID, ");
		sql.append("NULL                  AS OCORRENCIA_COD_ESTADO, ");
		sql.append("tb_pessoal.cpf AS CPF_TRAB, ");
		sql.append("tb_funcional.matricula AS MATRICULA, ");
		sql.append("tb_licenca.inicio AS DT_INI_AFAST, ");
		sql.append("tb_tipolicenca.id AS COD_MOT_AFAST, ");
		sql.append("tb_tipolicenca.descricao AS OBSERVACAO, ");
		sql.append("tb_licenca.fim AS DT_TERM_AFAST ");
		sql.append("FROM   srh.tb_licenca ");
		sql.append("INNER JOIN srh.tb_pessoal ");
		sql.append("ON srh.tb_licenca.idpessoal = srh.tb_pessoal.id ");
		sql.append("INNER JOIN srh.tb_tipolicenca ");
		sql.append("ON srh.tb_licenca.idtipolicenca = srh.tb_tipolicenca.id ");
		sql.append("INNER JOIN srh.tb_funcional ");
		sql.append("ON  srh.tb_funcional.idpessoal = srh.tb_pessoal.id ");
		sql.append("INNER JOIN srh.tb_ocupacao ");
		sql.append("ON  srh.tb_funcional.IDOCUPACAO = srh.tb_ocupacao.id ");
		
		if(Objects.nonNull(joinsAdicional)) {
			sql.append(joinsAdicional);	
		}
		
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
		Query query = entityManager.createQuery("Select max(e.id) from EstagiarioESocial e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
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
