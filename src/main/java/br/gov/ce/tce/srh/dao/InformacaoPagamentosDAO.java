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
	public List<InformacaoPagamentos> findInformacaoPagamentos(String mesReferencia, String anoReferencia, Long idPagamentos, Long idFuncional, String periodoApuracao) {	
		Query query = entityManager.createNativeQuery(getSQLInformacaoPagamentos(idFuncional), InformacaoPagamentos.class);
		query.setParameter("mesReferencia", mesReferencia);
		query.setParameter("anoReferencia", anoReferencia);
		query.setParameter("idPagamentos", idPagamentos);
		query.setParameter("periodoApuracao", periodoApuracao);
	
		
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}

	
	public String getSQLInformacaoPagamentos(Long idFuncional) {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT    "); 
		sql.append(" ( ROWNUM * -1) as id, ");
		sql.append(" :idPagamentos as IDPAGAMENTOS, ");
		sql.append(" pg.data_pag as DT_PGTO,  ");
		sql.append(" Decode(rt.id, null, Decode(rs.id, null, 5,4), 1)  ");
		sql.append(" as TP_PGTO,  ");
		sql.append(" dp.arquivo as IDE_DM_DEV,   ");
		sql.append(" :periodoApuracao as PER_REF,   ");
		sql.append(" ip.valor as VR_LIQ  ");
		sql.append(" from srh.fp_pagamentos pg  ");
		sql.append(" inner join srh.fp_dadospagto dp on pg.arquivo = dp.arquivo   ");
		sql.append(" inner join srh.fp_itenspagto ip on dp.idpagto = ip.idpagto   ");
		sql.append(" inner join srh.fp_cadastro c on dp.cod_func = c.cod_func   ");
		sql.append(" inner join srh.tb_pessoal p on c.idpessoal = p.id   ");
		sql.append(" left join srh.esocial_remuneracaotrabalhador rt on rt.idfuncional = dp.idfuncional and rt.PER_APUR = :periodoApuracao  ");
		sql.append(" left join srh.esocial_remuneracaoservidor rs on rs.idfuncional = dp.idfuncional and rs.PER_APUR = :periodoApuracao  ");
		sql.append(" left join srh.esocial_remuneracaobeneficio rb on rb.idfuncional = dp.idfuncional and rs.PER_APUR = :periodoApuracao  ");
		
		sql.append(" where ano_esocial = :anoReferencia  ");
		sql.append(" and mes_esocial = :mesReferencia ");
		sql.append(" and ip.rubrica = 'LIQ'   ");
		//sql.append(" and dp.num_mes <> '13'   ");
		sql.append(" AND (dp.num_mes <> '13' or (dp.num_mes = '13' and RESCISAO_13 = 'S' )) ");
		if(idFuncional != null) {
			sql.append(" and dp.idfuncional = :idFuncional  ");
		}
		sql.append(" order by  dp.arquivo desc  ");

	    return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<InformacaoPagamentos> findInformacaoPagamentosByIdfuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select e from InformacaoPagamentos e where e.remuneracaoTrabalhador.funcional.id = :idFuncional", InformacaoPagamentos.class);
		query.setParameter("idFuncional",idFuncional);
		return query.getResultList();
	}

	
}
