package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
import br.gov.ce.tce.srh.domain.ItensRemuneracaoTrabalhador;

@Repository
public class ItensRemuneracaoTrabalhadorDAO {

	static Logger logger = Logger.getLogger(ItensRemuneracaoTrabalhadorDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from ItensRemuneracaoTrabalhador e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public ItensRemuneracaoTrabalhador salvar(ItensRemuneracaoTrabalhador entidade) {

		if (entidade.getId() == null || entidade.getId() < 0) {
			entityManager.persist(entidade);
		}
		else {
			entityManager.merge(entidade);
		}
		
		return entidade;
	}

	public void excluir(ItensRemuneracaoTrabalhador entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public ItensRemuneracaoTrabalhador getById(Long id) {
		return entityManager.find(ItensRemuneracaoTrabalhador.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<ItensRemuneracaoTrabalhador> findByDemonstrativosDeValores(DemonstrativosDeValores demonstrativosDeValores, String matricula) {	
		Query query = entityManager.createNativeQuery(getSQLItensRemuneracaoTrabalhador(), ItensRemuneracaoTrabalhador.class);
		query.setParameter("idDmDev", demonstrativosDeValores.getId() );
		query.setParameter("ideDmDev", demonstrativosDeValores.getIdeDmDev() );
		query.setParameter("matricula", matricula);
		return query.getResultList();
	}

	
	public String getSQLItensRemuneracaoTrabalhador() {
		StringBuffer sql = new StringBuffer();

		sql.append(" Select " );
		sql.append(" ( ROWNUM * -1) as id, ");
		sql.append(" :idDmDev as IDDMDEV," );
		sql.append(" null as IDINFOREMUNPERANTERIORES, " );
		sql.append(" null as IDINFOREMUNPERAPUR, " );
		sql.append(" ip.rubrica as COD_RUBR, " );
		sql.append(" rt.CODIGO as IDE_TAB_RUBR," );
		sql.append(" ip.valor as VR_RUBR," );
		sql.append(" null as QTD_RUBR," );
		sql.append(" null as FATOR_RUBR," );
		sql.append(" 0 as IND_APUR_IR" );
		sql.append(" from srh.fp_dadospagto dp inner join srh.fp_itenspagto ip on dp.idpagto = ip.idpagto" );
		sql.append(" inner join srh.ESOCIAL_RUBRICACONFIG rc on ip.rubrica = rc.CODIGORUBRICA" );
		sql.append(" inner join srh.ESOCIAL_RUBRICA_TABELA rt on rt.id = rc.IDTABELARUBRICA" );
		sql.append(" where dp.arquivo = :ideDmDev" );
		sql.append(" and dp.cod_func = :matricula" );
		sql.append(" and rubrica not in ('BRU','DES','LIQ') " );
		sql.append(" order by dp.num_ano desc, dp.num_mes desc, dp.arquivo, rubrica" );
		//sql.append(" AND srh.tb_funcional.id = :idFuncional " ); 
	    
	    return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<ItensRemuneracaoTrabalhador> findItensRemuneracaoTrabalhadorByIdfuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select e from ItensRemuneracaoTrabalhador e where e.remuneracaoTrabalhador.funcional.id = :idFuncional", ItensRemuneracaoTrabalhador.class);
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}

}
