package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.InformacaoPagamentos;

@Repository
public class InformacaoPagamentosDAO {

	static Logger logger = Logger.getLogger(InformacaoPagamentosDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from InformacaoPagamentos e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}*/

	public InformacaoPagamentos salvar(InformacaoPagamentos entidade) {

		if (entidade.getId() == null || entidade.getId() < 0) {
			entityManager.persist(entidade);
		}
		else {
			entityManager.merge(entidade);
		}
		
		return entidade;
	}

	public void excluir(InformacaoPagamentos entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public InformacaoPagamentos getById(Long id) {
		return entityManager.find(InformacaoPagamentos.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<InformacaoPagamentos> findInformacaoPagamentos(String mesReferencia, String anoReferencia, Long idRemuneracaoTrabalhador, Long idRemuneracaoServidor, Long idFuncional) {	
		Query query = entityManager.createNativeQuery(getSQLInformacaoPagamentos(idFuncional), InformacaoPagamentos.class);
		query.setParameter("mesReferencia", mesReferencia);
		query.setParameter("anoReferencia", anoReferencia);
		if(idRemuneracaoTrabalhador != null && idRemuneracaoTrabalhador > 0 ) {
			query.setParameter("idRemuneracaoTrabalhador", idRemuneracaoTrabalhador);
		}
		else {
			query.setParameter("idRemuneracaoTrabalhador", null);
		}
		
		if(idRemuneracaoServidor != null && idRemuneracaoServidor > 0 ) {
			query.setParameter("idRemuneracaoServidor", idRemuneracaoServidor);
		}
		else {
			query.setParameter("idRemuneracaoServidor", null);
		}
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}

	
	public String getSQLInformacaoPagamentos(Long idFuncional) {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT    "); 
		sql.append(" ( ROWNUM * -1) as id, ");
		sql.append(" :idRemuneracaoTrabalhador as IDREMUNERACAOTRABALHADOR, ");
		sql.append(" :idRemuneracaoServidor as IDREMUNERACAOSERVIDOR, ");
		sql.append(" null as IDREMUNERACAOBENEFICIO, ");
		
		sql.append(" dp.arquivo as IDE_DM_DEV, ");
		
		sql.append("  CASE f.idocupacao ");
		sql.append("  WHEN 33 THEN 302 ");
		sql.append("  WHEN 14 THEN 901 ");
		sql.append("  WHEN 15 THEN 901 ");
		sql.append("  ELSE 301 ");
		sql.append(" END AS COD_CATEG, ");	
		
		sql.append(" null as NR_BENEFICIO, ");
		sql.append(" null as COD_CBO, ");
		sql.append(" null as NAT_ATIVIDADE, ");
		sql.append(" null as QTD_DIAS_TRAB, ");
		sql.append(" CASE pg.mes_esocial WHEN to_number(dp.num_mes) THEN 0 ELSE 1 END AS FLINFOREMUNPERANTERIORES ");
				  
		sql.append(" FROM srh.fp_pagamentos pg ");
		sql.append(" INNER JOIN srh.fp_dadospagto dp ON pg.arquivo = dp.arquivo ");
		sql.append(" INNER JOIN srh.fp_cadastro c ON dp.cod_func = c.cod_func ");
		sql.append(" INNER JOIN srh.tb_pessoal p ON c.idpessoal = p.id ");
		sql.append(" INNER JOIN srh.tb_funcional f ON f.idpessoal = p.id and f.datasaida is null ");
		sql.append(" WHERE ano_esocial = :anoReferencia ");
		sql.append(" AND mes_esocial = :mesReferencia");
		
		sql.append(" AND dp.num_mes <> '13' ");
		if(idFuncional != null) {
			sql.append("AND f.id = :idFuncional ");
		}
		sql.append(" ORDER BY FLINFOREMUNPERANTERIORES, IDE_DM_DEV");

	    
	    return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<InformacaoPagamentos> findInformacaoPagamentosByIdfuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select e from InformacaoPagamentos e where e.remuneracaoTrabalhador.funcional.id = :idFuncional", InformacaoPagamentos.class);
		query.setParameter("idFuncional",idFuncional);
		return query.getResultList();
	}

	
}
