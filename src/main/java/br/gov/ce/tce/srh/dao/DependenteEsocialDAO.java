package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.DependenteEsocial;
import br.gov.ce.tce.srh.domain.DependenteEsocialVO;

@Repository
public class DependenteEsocialDAO {

	static Logger logger = Logger.getLogger(DependenteEsocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from DependenteEsocial e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public DependenteEsocial salvar(DependenteEsocial entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(DependenteEsocial entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public DependenteEsocial getById(Long id) {
		return entityManager.find(DependenteEsocial.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<DependenteEsocial> findByIdfuncional(Long idFuncional) {	
		Query query = entityManager.createNativeQuery(getSQLDependente(), DependenteEsocial.class);
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<DependenteEsocialVO> findDependenteByIdfuncional(Long idFuncional) {
		String nomeTab =  "CONECTOR_ESOCIAL.CON_S2200_DEPENDENTE";
		String filtro =  "CONECTOR_ESOCIAL.CON_S2200_DEPENDENTE.IDFUNCIONAL = :idFuncional";
		
		Query query = entityManager.createNativeQuery(getSQLDependenteEsocial(nomeTab, filtro), DependenteEsocialVO.class);
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}

	
	public String getSQLDependente() {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT ");
		sql.append(" tb_dependente.id AS id, ");
		sql.append(" tb_pessoal.nome AS NM_DEP,  ");
		sql.append(" tb_pessoal.cpf AS CPF_DEP, ");
		sql.append(" tb_pessoal.sexo AS SEXO_DEP,  ");         
		sql.append(" tb_pessoal.datanascimento AS DT_NASC,  ");
		sql.append(" Decode(tb_dependente.depir ,1, 'S', 'N') AS DEP_IRRF, ");
		sql.append(" Decode(tb_dependente.depsf ,1, 'S', 'N') AS DEP_SF,    ");          
		sql.append(" 'N' AS INC_TRAB, ");
		sql.append(" 'N' AS INC_FIS_MEN, ");
		sql.append(" trim(to_char(tb_tipodependencia.codigoesocial,'00')) AS TP_DEP, ");
		sql.append(" :idFuncional AS IDFUNCIONAL   ");                                                                                                                                                                                                                                                                                                                    
		sql.append(" FROM srh.tb_dependente ");
		sql.append(" INNER JOIN srh.tb_pessoal ON srh.tb_dependente.idpessoaldep = srh.tb_pessoal.id ");
		sql.append(" INNER JOIN srh.tb_tipodependencia ON srh.tb_dependente.idtipodependencia = srh.tb_tipodependencia.id ");
		sql.append(" INNER JOIN srh.tb_funcional ON srh.tb_funcional.idpessoal = srh.tb_dependente.idpessoalresp ");
		sql.append(" WHERE tb_dependente.depir = 1 "); //(tb_dependente.depprev = 1 OR tb_dependente.depir = 1)
		sql.append(" AND srh.tb_funcional.id = :idFuncional " ); 
	    
	    return sql.toString();
	}
	
	public String getSQLDependenteEsocial(String nomeTabela, String filtroWhere) {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT ");
		sql.append(" ID AS id ");
		sql.append(" , TP_DEP AS tpDep ");
		sql.append(" , NM_DEP AS nome ");
		sql.append(" , DT_NASC AS dataNascimento ");
		sql.append(" , CPF_DEP AS cpf ");
		sql.append(" , DEP_IRRF AS depIrrf ");
		sql.append(" , DEP_SF AS depSf "); 		
		sql.append(" , INC_TRAB AS incTrab ");
		sql.append(" , SEXO_DEP AS sexo ");		
		sql.append(" , INC_FIS_MEN AS IncFisMen ");
		sql.append(" FROM ");
		sql.append(nomeTabela);
		sql.append(" WHERE ");
		sql.append(filtroWhere);	    
	    return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<DependenteEsocial> findDependenteEsocialByIdfuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select e from DependenteEsocial e where e.funcional.id = :idFuncional", DependenteEsocial.class);
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}

}
