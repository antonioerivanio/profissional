package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Vinculo;

/**
*
* @author robstown
* 
*/
@Repository
public class VinculoDAOImpl implements VinculoDAO {

	static Logger logger = Logger.getLogger(VinculoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public Vinculo getById(Long id) {
		return entityManager.find(Vinculo.class, id);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Vinculo> findAll() {
		return entityManager.createQuery("SELECT v FROM Vinculo v ORDER BY v.id").getResultList();
	}

}
