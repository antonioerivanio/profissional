package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Uf;

@Repository
public class UfDAO {

	static Logger logger = Logger.getLogger(UfDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<Uf> findAll() {
		return entityManager.createQuery("SELECT u FROM Uf u ORDER BY u.id").getResultList();
	}

}
