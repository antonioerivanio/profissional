package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
import br.gov.ce.tce.srh.domain.InfoRemuneracaoPeriodoApuracao;

@Repository
public class InfoRemuneracaoPeriodoApuracaoDAO {

	static Logger logger = Logger.getLogger(InfoRemuneracaoPeriodoApuracaoDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from InfoRemuneracaoPeriodoApuracao e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public InfoRemuneracaoPeriodoApuracao salvar(InfoRemuneracaoPeriodoApuracao entidade) {

		if (entidade.getId() == null || entidade.getId() < 0) {
			entityManager.persist(entidade);
		}
		else {
			entityManager.merge(entidade);
		}
		
		return entidade;
	}

	public void excluir(InfoRemuneracaoPeriodoApuracao entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public InfoRemuneracaoPeriodoApuracao getById(Long id) {
		return entityManager.find(InfoRemuneracaoPeriodoApuracao.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<InfoRemuneracaoPeriodoApuracao> findInfoRemuneracaoPeriodoApuracao(String mesReferencia, String anoReferencia, DemonstrativosDeValores demonstrativosDeValores, Long idFuncional) {	
		Query query = entityManager.createNativeQuery(getSQLInfoRemuneracaoPeriodoApuracao(idFuncional), InfoRemuneracaoPeriodoApuracao.class);
		query.setParameter("mesReferencia", mesReferencia);
		query.setParameter("anoReferencia", anoReferencia);
		
		if(demonstrativosDeValores != null && demonstrativosDeValores.getId() != null && demonstrativosDeValores.getId() > 0 ) {
			query.setParameter("idDmDev", demonstrativosDeValores.getId());
		}
		else {
			query.setParameter("idDmDev", null);
		}
		
		query.setParameter("ideDmDev", demonstrativosDeValores.getIdeDmDev() );
		query.setParameter("idFuncional", idFuncional );
		return query.getResultList();
	}

	
	public String getSQLInfoRemuneracaoPeriodoApuracao(Long idFuncional) {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT  ");    
		sql.append(" ( ROWNUM * -1) as id, "); 
		sql.append(" :idDmDev as IDDMDEV,  ");		
		sql.append(" 1 as TP_INSC,  ");
		sql.append(" '09499757000146'  as NR_INSC,  ");
		sql.append(" 'LOTACAO-BASICA' as COD_LOTACAO, "); 		 
		sql.append("  0||dp.cod_func as matricula,  ");
		sql.append(" null as IND_SIMPLES,  ");
		sql.append("  1 as GRAU_EXP  ");
		  
		sql.append(" FROM srh.fp_pagamentos pg ");
		sql.append(" INNER JOIN srh.fp_dadospagto dp ON pg.arquivo = dp.arquivo ");
		sql.append(" INNER JOIN srh.fp_cadastro c ON dp.cod_func = c.cod_func ");
		sql.append(" INNER JOIN srh.tb_pessoal p ON c.idpessoal = p.id ");
		sql.append(" INNER JOIN srh.tb_funcional f ON f.idpessoal = p.id and f.datasaida is null ");
		sql.append(" WHERE ano_esocial = :anoReferencia ");
		sql.append(" AND mes_esocial = :mesReferencia");
		sql.append(" AND dp.contribui_inss = 'S' ");
		sql.append(" AND dp.num_mes <> '13' ");
		sql.append(" AND dp.num_mes = :mesReferencia ");
		sql.append(" AND dp.arquivo = :ideDmDev ");
		if(idFuncional != null) {
			sql.append("AND f.id = :idFuncional ");
		}
		sql.append(" ORDER BY dp.nome ");

	    
	    return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<InfoRemuneracaoPeriodoApuracao> findInfoRemuneracaoPeriodoApuracaoByIdfuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select e from InfoRemuneracaoPeriodoApuracao e where e.remuneracaoTrabalhador.funcional.id = :idFuncional", InfoRemuneracaoPeriodoApuracao.class);
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}

	
	public InfoRemuneracaoPeriodoApuracao findInfoRemuneracaoPeriodoApuracao(DemonstrativosDeValores demonstrativosDeValores) {
		Query query = entityManager.createNativeQuery(getSQLInfoRemuneracaoPeriodoApuracaoRPA(), InfoRemuneracaoPeriodoApuracao.class);

		if(demonstrativosDeValores != null && demonstrativosDeValores.getId() != null && demonstrativosDeValores.getId() > 0 ) {
			query.setParameter("idDmDev", demonstrativosDeValores.getId());
		}
		else {
			query.setParameter("idDmDev", null);
		}
	
		return (InfoRemuneracaoPeriodoApuracao) query.getSingleResult();
	}
	
	public String getSQLInfoRemuneracaoPeriodoApuracaoRPA() {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT  ");    
		sql.append(" ( ROWNUM * -1) as id, "); 
		sql.append(" :idDmDev as IDDMDEV,  ");		
		sql.append(" 1 as TP_INSC,  ");
		sql.append(" '09499757000146'  as NR_INSC,  ");
		sql.append(" 'LOTACAO-BASICA' as COD_LOTACAO, "); 	
		sql.append("  null as matricula,  ");
		sql.append("  null as IND_SIMPLES,  ");
		sql.append("  1 as GRAU_EXP  ");

		sql.append("  from tb_funcional ");
				sql.append("  WHERE id = 1 ");

	    return sql.toString();
	}

}
