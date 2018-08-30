package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.TipoLogradouro;

@Repository
public class TipoLogradouroDAOImpl implements TipoLogradouroDAO {

	static Logger logger = Logger.getLogger(TipoLogradouroDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoLogradouro> findAll() {
		return entityManager.createQuery("SELECT t FROM TipoLogradouro t ORDER BY t.ordenacao, t.descricao").getResultList();
	}

}
