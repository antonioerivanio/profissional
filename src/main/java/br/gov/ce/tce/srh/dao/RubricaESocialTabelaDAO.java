package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.RubricaESocialTabela;

@Repository
public class RubricaESocialTabelaDAO {

	static Logger logger = Logger.getLogger(RubricaESocialTabelaDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}	

	public RubricaESocialTabela getById(Long id) {
		return entityManager.find(RubricaESocialTabela.class, id);
	}
	
	public List<RubricaESocialTabela> findAll(){
		TypedQuery<RubricaESocialTabela> query = entityManager.createQuery("Select e from RubricaESocialTabela e order by e.id", RubricaESocialTabela.class);
		return query.getResultList(); 
	}	

}
