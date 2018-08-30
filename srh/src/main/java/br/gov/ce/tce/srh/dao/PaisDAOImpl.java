package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Pais;

@Repository
public class PaisDAOImpl implements PaisDAO {

	static Logger logger = Logger.getLogger(PaisDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Pais> findAll() {
		return entityManager.createQuery("SELECT p FROM Pais p ORDER BY p.ordenacao, p.descricao").getResultList();
	}

}
