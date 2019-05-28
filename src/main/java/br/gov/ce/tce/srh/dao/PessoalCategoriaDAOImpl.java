package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.PessoalCategoria;

/**
 * 
 * @author robstown
 *
 */
@Repository
public class PessoalCategoriaDAOImpl implements PessoalCategoriaDAO {

	static Logger logger = Logger.getLogger(PessoalCategoriaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<PessoalCategoria> findAll() {
		return entityManager.createQuery("SELECT c FROM PessoalCategoria c ORDER BY c.id").getResultList();
	}

}
