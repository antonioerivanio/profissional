package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Situacao;

/**
*
* @author robstown
* 
*/
@Repository
public class SituacaoDAOImpl implements SituacaoDAO {

	static Logger logger = Logger.getLogger(SituacaoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	public Situacao getById(Long id) {
		return entityManager.find(Situacao.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<Situacao> findAll() {
		return entityManager.createQuery("SELECT s FROM Situacao s ORDER BY s.id").getResultList();
	}

}
