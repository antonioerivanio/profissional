package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.TipoPublicacao;

/**
 * @author robson.castro
 *
 */
@Repository
public class TipoPublicacaoDAOImpl implements TipoPublicacaoDAO {

	static Logger logger = Logger.getLogger(TipoPublicacaoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoPublicacao> findAll() {
		return entityManager.createQuery("SELECT t FROM TipoPublicacao t ORDER BY t.id").getResultList();
	}

}
