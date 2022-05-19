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

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from DemonstrativosDeValores e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public DemonstrativosDeValores salvar(DemonstrativosDeValores entidade) {

		if (entidade.getId() == null || entidade.getId() < 0) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(DemonstrativosDeValores entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public DemonstrativosDeValores getById(Long id) {
		return entityManager.find(DemonstrativosDeValores.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<DemonstrativosDeValores> findDemonstrativosDeValores(String mesReferencia, String anoReferencia, Long idRemuneracaoTrabalhador, Long idFuncional) {	
		Query query = entityManager.createNativeQuery(getSQLDemonstrativosDeValores(), DemonstrativosDeValores.class);
		query.setParameter("mesReferencia", mesReferencia);
		query.setParameter("anoReferencia", anoReferencia);
		query.setParameter("idRemuneracaoTrabalhador",idRemuneracaoTrabalhador);
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}

	
	public String getSQLDemonstrativosDeValores() {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT dp.nome,   "); 
		sql.append(" (f.id * -1) as id, ");
		sql.append(" :idRemuneracaoTrabalhador as IDREMUNERACAOTRABALHADOR, ");
		sql.append(" dp.arquivo as IDE_DM_DEV, ");
		sql.append(" 302 as COD_CATEG, ");
		sql.append(" 1 as TP_INSC, ");
		sql.append(" '09499757'  as NR_INSC, ");
		sql.append(" 'LOTACAO-BASICA' as COD_LOTACAO, ");
		sql.append(" null as QTD_DIAS_AV, ");
		sql.append(" 0||dp.cod_func as matricula, ");
		sql.append(" null as IND_SIMPLES, ");
		sql.append(" null as GRAU_EXP, ");
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
		sql.append(" AND dp.contribui_inss = 'S' ");
		sql.append("AND dp.num_mes <> '13' ");
		sql.append(" and f.id = :idFuncional ");
		sql.append(" ORDER BY dp.nome ");

	    
	    return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<DemonstrativosDeValores> findDemonstrativosDeValoresByIdfuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select e from DemonstrativosDeValores e where e.remuneracaoTrabalhador.funcional.id = :idFuncional", DemonstrativosDeValores.class);
		query.setParameter("idFuncional",idFuncional);
		return query.getResultList();
	}

}
