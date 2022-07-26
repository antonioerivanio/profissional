package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.RemuneracaoOutraEmpresa;

@Repository
public class RemuneracaoOutraEmpresaDAO {

	static Logger logger = Logger.getLogger(RemuneracaoOutraEmpresaDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from RemuneracaoOutraEmpresa e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public RemuneracaoOutraEmpresa salvar(RemuneracaoOutraEmpresa entidade) {

		if (entidade.getId() == null || entidade.getId() < 0) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(RemuneracaoOutraEmpresa entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public RemuneracaoOutraEmpresa getById(Long id) {
		return entityManager.find(RemuneracaoOutraEmpresa.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<RemuneracaoOutraEmpresa> findRemuneracaoOutraEmpresa(String mesReferencia, String anoReferencia, Long idRemuneracaoTrabalhador, Long idFuncional) {	
		Query query = entityManager.createNativeQuery(getSQLRemuneracaoOutraEmpresa(idFuncional), RemuneracaoOutraEmpresa.class);
		query.setParameter("mesReferencia", mesReferencia);
		query.setParameter("anoReferencia", anoReferencia);
		if(idRemuneracaoTrabalhador != null && idRemuneracaoTrabalhador > 0 ) {
			query.setParameter("idRemuneracaoTrabalhador", idRemuneracaoTrabalhador);
		}
		else {
			query.setParameter("idRemuneracaoTrabalhador", null);
		}
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}

	
	public String getSQLRemuneracaoOutraEmpresa(Long idFuncional) {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT    "); 
		sql.append(" ( ROWNUM * -1) as id, ");
		sql.append(" :idRemuneracaoTrabalhador as IDREMUNERACAOTRABALHADOR, ");
		sql.append("  1 as TP_INSC_OUTR_EMPR, ");
		sql.append(" pj.cnpj as NR_INSC_OUTR_EMPR, ");
		sql.append(" v.tipoesocial as COD_CATEG_OUTR_EMPR, ");
		sql.append(" v.valoroutraempresa as VLR_REMUN_OE ");
				
		sql.append(" FROM srh.fp_pagamentos pg ");
		sql.append(" INNER JOIN srh.fp_dadospagto dp ON pg.arquivo = dp.arquivo ");
		sql.append(" INNER JOIN srh.fp_cadastro c ON dp.cod_func = c.cod_func ");
		sql.append(" INNER JOIN srh.tb_pessoal p ON c.idpessoal = p.id ");
		sql.append(" INNER JOIN srh.tb_funcional f ON f.idpessoal = p.id and f.datasaida is null ");
		sql.append(" LEFT JOIN srh.tb_vinculorgps v ON v.idfuncional = f.id ");
		sql.append(" LEFT JOIN srh.tb_pessoajuridica pj ON pj.id = v.idpessoajuridica ");
		sql.append(" WHERE ano_esocial = :anoReferencia ");
		sql.append(" AND mes_esocial = :mesReferencia");
		sql.append(" AND dp.contribui_inss = 'S' ");
		sql.append("AND dp.num_mes <> '13' ");
		
		if(idFuncional != null) {
			sql.append("AND f.id = :idFuncional ");
		}
		sql.append("ORDER BY dp.nome ");

	    
	    return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<RemuneracaoOutraEmpresa> findRemuneracaoOutraEmpresaByIdfuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select e from RemuneracaoOutraEmpresa e where e.remuneracaoTrabalhador.funcional.id = :idFuncional", RemuneracaoOutraEmpresa.class);
		query.setParameter("idFuncional",idFuncional);
		return query.getResultList();
	}

}