/**
 * 
 */
package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.TipoFerias;

/**
 * 
 * @author joel.barbosa
 *
 */
@Repository
public class TipoFeriasDAOImpl implements TipoFeriasDAO {

	static Logger logger = Logger.getLogger(TipoFeriasDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoFerias> findAll() {
		return entityManager.createQuery("SELECT tf FROM TipoFerias tf ORDER BY tf.id").getResultList();
	}

}
