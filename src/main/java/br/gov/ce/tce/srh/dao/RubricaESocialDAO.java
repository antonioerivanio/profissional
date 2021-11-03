package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.RubricaESocial;

@Repository
public class RubricaESocialDAO {

	static Logger logger = Logger.getLogger(RubricaESocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}	

	public RubricaESocial getById(Long id) {
		return entityManager.find(RubricaESocial.class, id);
	}
	
	public List<RubricaESocial> findAll(){
		TypedQuery<RubricaESocial> query = entityManager.createQuery("Select e from RubricaESocial e where emUso=true order by e.id", RubricaESocial.class);
		return query.getResultList(); 
	}

}
