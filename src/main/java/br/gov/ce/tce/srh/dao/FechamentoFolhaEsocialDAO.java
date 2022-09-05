package br.gov.ce.tce.srh.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import br.gov.ce.tce.srh.domain.FechamentoFolhaEsocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.service.AmbienteService;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class FechamentoFolhaEsocialDAO {

	static Logger logger = Logger.getLogger(FechamentoFolhaEsocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private AmbienteService ambienteService;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(e.id) FROM FechamentoFolhaEsocial e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public FechamentoFolhaEsocial salvar(FechamentoFolhaEsocial entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(FechamentoFolhaEsocial entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public FechamentoFolhaEsocial getById(Long id) {
		return entityManager.find(FechamentoFolhaEsocial.class, id);
	}
	
	public int count(String nome, String cpf) {
		
		StringBuffer sql = new StringBuffer();

		sql.append(" Select count(b) FROM FechamentoEventoEsocial b inner join b.funcional f WHERE 1=1 ");

		if (nome != null && !nome.isEmpty()) {
			sql.append("  and upper( f.nome ) like :nome ");
		}
		
		if (cpf != null && !cpf.isEmpty()) {
			sql.append("  AND f.cpf = :cpf ");
		}
						
		Query query = entityManager.createQuery(sql.toString());

		if (nome != null && !nome.isEmpty()) {
			query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		}
		
		if (cpf != null && !cpf.isEmpty()) {
			query.setParameter("cpf", SRHUtils.removerMascara( cpf ));
		}
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<FechamentoFolhaEsocial> search(String nome, String cpf, Integer first, Integer rows) {

		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT b FROM FechamentoEventoEsocial b inner join fetch b.funcional f WHERE 1=1 ");

		if (nome != null && !nome.isEmpty()) {
			sql.append("  and upper( f.nome ) like :nome ");
		}
		
		if (cpf != null && !cpf.isEmpty())
			sql.append("  AND f.cpf = :cpf ");

		sql.append("  ORDER BY f.nome ");

		Query query = entityManager.createQuery(sql.toString());

		if (nome != null && !nome.isEmpty()) {
			query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		}
		
		if (cpf != null && !cpf.isEmpty()) {
			query.setParameter("cpf", SRHUtils.removerMascara( cpf ));
		}
		if (first != null && first >= 0)
			query.setFirstResult(first);
		if (rows != null && rows > 0)
			query.setMaxResults(rows);

		return query.getResultList();
	}

	public FechamentoFolhaEsocial getEventoS1299ByServidor(Funcional servidorFuncional) {
		try {
			Query query = entityManager.createNativeQuery(getSQLEventoS1299(), FechamentoFolhaEsocial.class);
			query.setParameter("idFuncional",servidorFuncional.getId() );
			return (FechamentoFolhaEsocial) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}	

	
	public String getSQLEventoS1299() {	  
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT    ");
		sql.append("   0 as id, ");
		sql.append("  f.id idfuncional,  ");
		sql.append("  f.id||'-'||o.id AS referencia,  ");
		sql.append("  1 as tp_insc, ");
		sql.append("  NULL as nr_insc, ");
		sql.append("  evt_remun, ");
		sql.append("  evt_comprod, ");
		sql.append("  evt_contraavnp, ");
		sql.append("  evt_infocomplper, ");
	    sql.append("  trans_dctweb, ");
	    sql.append("  nao_valid ");		   
		sql.append(" FROM srh.tb_funcional f ");
		sql.append(" WHERE f.id = :idFuncional ");
	    return sql.toString();
	}

}

