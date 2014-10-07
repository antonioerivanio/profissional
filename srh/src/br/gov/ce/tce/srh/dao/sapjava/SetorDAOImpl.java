package br.gov.ce.tce.srh.dao.sapjava;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.sapjava.Setor;

/**
 *
 * @author robstown
 */
@Repository
public class SetorDAOImpl implements SetorDAO {

	static Logger logger = Logger.getLogger(SetorDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@SuppressWarnings("unchecked")
	public List<Setor> findAll() {
		return entityManager.createNamedQuery("Setor.findAll").getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Setor> findTodosAtivos(){
		return entityManager.createNamedQuery("Setor.findTodosAtivos").getResultList();
	}


	// TODO - remover
	@Override
	public Setor getById(Long id) {
		return entityManager.find(Setor.class, id);
	}

}
