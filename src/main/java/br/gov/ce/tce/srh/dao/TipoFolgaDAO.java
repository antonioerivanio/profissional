package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.TipoFolga;

@Repository
public class TipoFolgaDAO {

static Logger logger = Logger.getLogger(TipoFolgaDAO.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<TipoFolga> findAll() {
		return entityManager.createQuery("SELECT tf FROM TipoFolga tf ORDER BY tf.id").getResultList();
	}
	
}
