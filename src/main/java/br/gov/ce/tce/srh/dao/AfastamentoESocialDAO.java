package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.AfastamentoESocial;
import br.gov.ce.tce.srh.domain.Funcional;
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
			Query query = entityManager.createNativeQuery(getSQLEventoS2230(possuiCargo), AfastamentoESocial.class);
			query.setParameter("idFuncional", servidorFuncional.getId());
			return (AfastamentoESocial) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public AfastamentoESocial getById(Long id) {
		return entityManager.find(AfastamentoESocial.class, id);
	}
	
	public String getSQLEventoS2230(boolean possuiCargo) {
		StringBuffer sql = new StringBuffer();
		
		sql.append("SELECT 0 AS ID, ");
		sql.append(" tb_funcional.id||'-'||tb_ocupacao.id AS REFERENCIA, ");
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
		sql.append("WHERE  tb_funcional.id = :idFuncional  ");
//		sql.append("AND ( tb_licenca.inicio > To_date('21/11/2021', 'dd/mm/yyyy') ");
//		sql.append("AND ( tb_licenca.fim >= To_date('22/11/2021', 'dd/mm/yyyy') ");
//		sql.append("OR tb_licenca.fim IS NULL ) ) ");
		          
		return sql.toString();
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
