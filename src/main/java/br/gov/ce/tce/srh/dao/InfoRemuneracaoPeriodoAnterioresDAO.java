package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
import br.gov.ce.tce.srh.domain.InfoRemuneracaoPeriodoAnteriores;

@Repository
public class InfoRemuneracaoPeriodoAnterioresDAO {

	static Logger logger = Logger.getLogger(InfoRemuneracaoPeriodoAnterioresDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from InfoRemuneracaoPeriodoAnteriores e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public InfoRemuneracaoPeriodoAnteriores salvar(InfoRemuneracaoPeriodoAnteriores entidade) {

		if (entidade.getId() == null || entidade.getId() < 0) {
			entityManager.persist(entidade);
		}
		else {
			entityManager.merge(entidade);
		}
		
		return entidade;
	}

	public void excluir(InfoRemuneracaoPeriodoAnteriores entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public InfoRemuneracaoPeriodoAnteriores getById(Long id) {
		return entityManager.find(InfoRemuneracaoPeriodoAnteriores.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<InfoRemuneracaoPeriodoAnteriores> findInfoRemuneracaoPeriodoAnteriores(String mesReferencia, String anoReferencia, DemonstrativosDeValores demonstrativosDeValores, Long idFuncional) {	
		Query query = entityManager.createNativeQuery(getSQLInfoRemuneracaoPeriodoAnteriores(idFuncional), InfoRemuneracaoPeriodoAnteriores.class);
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

	
	public String getSQLInfoRemuneracaoPeriodoAnteriores(Long idFuncional) {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT  ");    
		sql.append(" ( ROWNUM * -1) as id, "); 
		sql.append(" :idDmDev as IDDMDEV,  ");
		sql.append(" null as DT_AC_CONV, "); 
		sql.append(" 'B' as TP_AC_CONV,  ");
		sql.append(" null as DSC,  ");
		sql.append(" 'N'  as REMUN_SUC,  ");
		sql.append(" ano_esocial||'-'||dp.num_mes as PER_REF,  ");
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
		
		sql.append(" AND dp.num_mes <> '13' ");
		sql.append(" AND dp.num_mes <> :mesReferencia ");
		sql.append(" AND dp.arquivo = :ideDmDev ");
		if(idFuncional != null) {
			sql.append("AND f.id = :idFuncional ");
		}
		sql.append(" ORDER BY dp.nome ");

	    
	    return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<InfoRemuneracaoPeriodoAnteriores> findInfoRemuneracaoPeriodoAnterioresByIdfuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select e from InfoRemuneracaoPeriodoAnteriores e where e.remuneracaoTrabalhador.funcional.id = :idFuncional", InfoRemuneracaoPeriodoAnteriores.class);
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}



}
