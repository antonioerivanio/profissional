package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;

@Repository
public class DemonstrativosDeValoresDAO {

	static Logger logger = Logger.getLogger(DemonstrativosDeValoresDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from DemonstrativosDeValores e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}*/

	public DemonstrativosDeValores salvar(DemonstrativosDeValores entidade) {

		if (entidade.getId() == null || entidade.getId() < 0) {
			entityManager.persist(entidade);
		}
		else {
			entityManager.merge(entidade);
		}
		
		return entidade;
	}

	public void excluir(DemonstrativosDeValores entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public DemonstrativosDeValores getById(Long id) {
		return entityManager.find(DemonstrativosDeValores.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<DemonstrativosDeValores> findDemonstrativosDeValores(String mesReferencia, String anoReferencia, Long idRemuneracaoTrabalhador, Long idRemuneracaoServidor, Long idFuncional) {	
		Query query = entityManager.createNativeQuery(getSQLDemonstrativosDeValores(idFuncional), DemonstrativosDeValores.class);
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

	
	public String getSQLDemonstrativosDeValores(Long idFuncional) {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT    "); 
		sql.append(" ( ROWNUM * -1) as id, ");
		sql.append(" :idRemuneracaoTrabalhador as IDREMUNERACAOTRABALHADOR, ");
		sql.append(" :idRemuneracaoServidor as IDREMUNERACAOSERVIDOR, ");
		sql.append(" null as IDREMUNERACAOBENEFICIO, ");
		
		sql.append(" dp.arquivo as IDE_DM_DEV, ");
		
		
		
		//sql.append(" 302 as COD_CATEG, ");
		
		
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
	public List<DemonstrativosDeValores> findDemonstrativosDeValoresByIdfuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select e from DemonstrativosDeValores e where e.remuneracaoTrabalhador.funcional.id = :idFuncional", DemonstrativosDeValores.class);
		query.setParameter("idFuncional",idFuncional);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<DemonstrativosDeValores> findDemonstrativosDeValoresRPA(String mesReferencia, String anoReferencia,
			Long idRemuneracaoTrabalhador, Long idPrestador) {
		Query query = entityManager.createNativeQuery(getSQLDemonstrativosDeValoresRPA(idPrestador), DemonstrativosDeValores.class);
		query.setParameter("competencia",anoReferencia+mesReferencia);
		if(idRemuneracaoTrabalhador != null && idRemuneracaoTrabalhador > 0 ) {
			query.setParameter("idRemuneracaoTrabalhador", idRemuneracaoTrabalhador);
		}
		else {
			query.setParameter("idRemuneracaoTrabalhador", null);
		}
		query.setParameter("idPrestador",idPrestador );
		return query.getResultList();
	}

	
	public String getSQLDemonstrativosDeValoresRPA(Long idPrestador) {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT    "); 
		sql.append(" ( ROWNUM * -1) as id, ");
		sql.append(" :idRemuneracaoTrabalhador as IDREMUNERACAOTRABALHADOR, ");
		sql.append(" null as IDREMUNERACAOSERVIDOR, ");
		sql.append(" null as IDREMUNERACAOBENEFICIO, ");
		
		sql.append(" 'RPA_'||dp.idpagtoprestador as IDE_DM_DEV, ");
		sql.append(" 701 as COD_CATEG, ");
		sql.append(" null as NR_BENEFICIO, ");
		sql.append("  dp.novocbo as COD_CBO, ");
		sql.append(" null as NAT_ATIVIDADE, ");
		sql.append(" null as QTD_DIAS_TRAB, ");
		sql.append(" 0 AS FLINFOREMUNPERANTERIORES ");
		
		sql.append(" FROM   ");
		sql.append("  fp_dadospagtoprestador dp  ");
		sql.append("  INNER JOIN fp_cadastroprestador cp ON dp.idprestador = cp.idprestador ");
		sql.append(" where  TO_CHAR(data_np, 'yyyymm') = :competencia  ");
				  
		
		 if(idPrestador != null) {
			 sql.append(" AND cp.idprestador = :idPrestador ");
		  }
		sql.append(" ORDER BY dp.nome ");

	    
	    return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<DemonstrativosDeValores> findDemonstrativosDeValoresBeneficio(String mesReferencia, String anoReferencia, Long idRemuneracaoBeneficio, Long idFuncional) {
		Query query = entityManager.createNativeQuery(getSQLDemonstrativosDeValores1207(idFuncional), DemonstrativosDeValores.class);
		query.setParameter("mesReferencia", mesReferencia);
		query.setParameter("anoReferencia", anoReferencia);
		if(idRemuneracaoBeneficio != null && idRemuneracaoBeneficio > 0 ) {
			query.setParameter("idRemuneracaoBeneficio", idRemuneracaoBeneficio);
		}
		else {
			query.setParameter("idRemuneracaoBeneficio", null);
		}

		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}
	
	public String getSQLDemonstrativosDeValores1207(Long idFuncional) {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT    "); 
		sql.append(" ( ROWNUM * -1) as id, ");
		sql.append(" null as IDREMUNERACAOTRABALHADOR, ");
		sql.append(" null as IDREMUNERACAOSERVIDOR, ");
		sql.append(" :idRemuneracaoBeneficio as IDREMUNERACAOBENEFICIO, ");
		sql.append(" dp.arquivo as IDE_DM_DEV, ");
		
		/*sql.append("  CASE f.idocupacao ");
		sql.append("  WHEN 33 THEN 302 ");
		sql.append("  WHEN 14 THEN 901 ");
		sql.append("  WHEN 15 THEN 901 ");
		sql.append("  ELSE 301 ");
		sql.append(" END AS COD_CATEG, ");*/
		sql.append(" null AS COD_CATEG, ");
		sql.append(" b.NR_BENEFICIO as NR_BENEFICIO, ");
		
		sql.append(" null as COD_CBO, ");
		sql.append(" null as NAT_ATIVIDADE, ");
		sql.append(" null as QTD_DIAS_TRAB, ");
		sql.append(" CASE pg.mes_esocial WHEN to_number(dp.num_mes) THEN 0 ELSE 1 END AS FLINFOREMUNPERANTERIORES ");
				  
		sql.append(" FROM srh.fp_pagamentos pg ");
		sql.append(" INNER JOIN srh.fp_dadospagto dp ON pg.arquivo = dp.arquivo ");
		sql.append(" INNER JOIN srh.fp_cadastro c ON dp.cod_func = c.cod_func ");
		sql.append(" INNER JOIN srh.tb_pessoal p ON c.idpessoal = p.id ");
		sql.append(" INNER JOIN srh.tb_funcional f ON f.idpessoal = p.id and f.datasaida is null ");
		sql.append(" INNER JOIN srh.ESOCIAL_BENEFICIO b ON b.idfuncional = f.id ");
		sql.append(" WHERE ano_esocial = :anoReferencia ");
		sql.append(" AND mes_esocial = :mesReferencia");
		
		sql.append(" AND dp.num_mes <> '13' ");
		if(idFuncional != null) {
			sql.append("AND f.id = :idFuncional ");
		}
		sql.append(" ORDER BY FLINFOREMUNPERANTERIORES, IDE_DM_DEV");

	    
	    return sql.toString();
	}

}
