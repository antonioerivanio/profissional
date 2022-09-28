package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Pagamentos;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class PagamentosEsocialDAO {

	static Logger logger = Logger.getLogger(PagamentosEsocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from Pagamentos e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public Pagamentos salvar(Pagamentos entidade) {

		if (entidade.getId() == null || entidade.getId() < 0) {
			entityManager.persist(entidade);
		}
		else {
			entityManager.merge(entidade);
		}
		
		return entidade;
	}

	public void excluir(Pagamentos entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public Pagamentos getById(Long id) {
		return entityManager.find(Pagamentos.class, id);
	}
	
	public int count(String nome, String cpf, String periodoApuracao) {
		
		StringBuffer sql = new StringBuffer();

		sql.append(" Select count(rt) FROM Pagamentos rt  WHERE 1=1 ");

		sql.append(" and rt.perApurCompetencia = :periodoApuracao ");
		
		if (nome != null && !nome.isEmpty()) {
			sql.append("  and upper( rt.nmTrabDesc ) like :nome ");
		}
		
		if (cpf != null && !cpf.isEmpty()) {
			sql.append("  AND rt.cpfBenef = :cpf ");
		}
	
						
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("periodoApuracao", periodoApuracao);

		if (nome != null && !nome.isEmpty()) {
			query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		}
		
		if (cpf != null && !cpf.isEmpty()) {
			query.setParameter("cpf", SRHUtils.removerMascara( cpf ));
		}
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<Pagamentos> search(String nome, String cpf, String periodoApuracao, Integer first, Integer rows) {

		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT rt FROM Pagamentos rt  WHERE 1=1 ");
		
		sql.append(" and rt.perApurCompetencia = :periodoApuracao ");

		if (nome != null && !nome.isEmpty()) {
			sql.append("  and upper( rt.nmTrabDesc ) like :nome ");
		}
		
		if (cpf != null && !cpf.isEmpty()) {
			sql.append("  AND rt.cpfBenef = :cpf ");
		}

		sql.append("  ORDER BY rt.nmTrabDesc ");

		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("periodoApuracao", periodoApuracao);
		
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

	@SuppressWarnings("unchecked")
	public Pagamentos getEventoS1210(String mesReferencia, String anoReferencia, String periodoApuracao, Funcional servidorFuncional) {
		try {
			Query query = entityManager.createNativeQuery(getSQLEventoS1210(servidorFuncional), Pagamentos.class);
			query.setParameter("mesReferencia", mesReferencia);
			query.setParameter("anoReferencia", anoReferencia);
			query.setParameter("periodoApuracao",periodoApuracao);
			query.setParameter("idFuncional",servidorFuncional.getId() );
			
			return  (Pagamentos) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}	

	public String getSQLEventoS1210(Funcional servidorFuncional) {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT distinct ( f.id * -1) as id,   "); 
		sql.append(" dp.nome as NM_TRAB_DESC, ");
		sql.append(" f.id as idfuncional, ");
		sql.append(" p.cpf||'-'||:periodoApuracao as referencia, ");
		sql.append("  1 as IND_APURACAO, ");
		sql.append(" :periodoApuracao as PER_APUR,       ");
		sql.append(" :periodoApuracao as PER_APUR_COMPETENCIA,       ");
		sql.append("  p.cpf as CPF_BENEF ");
					
		sql.append(" FROM srh.fp_pagamentos pg ");
		sql.append(" INNER JOIN srh.fp_dadospagto dp ON pg.arquivo = dp.arquivo ");
		sql.append(" INNER JOIN srh.fp_cadastro c ON dp.cod_func = c.cod_func ");
		sql.append(" INNER JOIN srh.tb_pessoal p ON c.idpessoal = p.id ");
		sql.append(" INNER JOIN srh.tb_funcional f ON f.idpessoal = p.id and f.datasaida is null ");
		sql.append(" LEFT JOIN srh.tb_vinculorgps v ON v.idfuncional = f.id ");
		sql.append(" LEFT JOIN srh.tb_pessoajuridica pj ON pj.id = v.idpessoajuridica ");
		sql.append(" WHERE ano_esocial = :anoReferencia ");
		sql.append(" AND mes_esocial = :mesReferencia");
	
		sql.append(" AND dp.num_mes <> '13' ");
		if(servidorFuncional != null) {
			sql.append("AND f.id = :idFuncional ");
		}
		sql.append("ORDER BY dp.nome ");		
	    
	    return sql.toString();
	}

}