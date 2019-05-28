package br.gov.ce.tce.srh.sapjava.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.sapjava.domain.Entidade;

/**
 *
 * @author Robson Castro
 */
@Repository
public class EntidadeDAOImpl implements EntidadeDAO {

	static Logger logger = Logger.getLogger(EntidadeDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@SuppressWarnings("unchecked")
	public List<Entidade> findAll() {
		return entityManager.createNamedQuery("Entidade.findAll").getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Entidade> listaOrgaoOrigemComRestricaoTipoEsfera() {
		Query query = entityManager.createQuery("SELECT e FROM Entidade e WHERE e.tipoEntidadeEsfera=1 or e.tipoEntidadeEsfera=2 or e.tipoEntidadeEsfera=3 ORDER BY descricaoEntidade ");
		return query.getResultList();
	}

}
