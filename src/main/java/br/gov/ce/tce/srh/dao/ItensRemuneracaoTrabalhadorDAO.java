package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

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

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(ItensRemuneracaoTrabalhador entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public ItensRemuneracaoTrabalhador getById(Long id) {
		return entityManager.find(ItensRemuneracaoTrabalhador.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public List<ItensRemuneracaoTrabalhador> findByIdfuncional(Long idFuncional) {	
		Query query = entityManager.createNativeQuery(getSQLItensRemuneracaoTrabalhador(), ItensRemuneracaoTrabalhador.class);
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}

	
	public String getSQLItensRemuneracaoTrabalhador() {
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
		sql.append(" WHERE (tb_dependente.depprev = 1 OR tb_dependente.depir = 1) ");
		sql.append(" AND srh.tb_funcional.id = :idFuncional " ); 
	    
	    return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<ItensRemuneracaoTrabalhador> findItensRemuneracaoTrabalhadorByIdfuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select e from ItensRemuneracaoTrabalhador e where e.remuneracaoTrabalhador.funcional.id = :idFuncional", ItensRemuneracaoTrabalhador.class);
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}

}
