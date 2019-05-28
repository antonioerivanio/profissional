package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.TipoOcupacao;

/**
 *
 * @author robstown
 */
@Repository
public class TipoOcupacaoDAOImpl implements TipoOcupacaoDAO {

	static Logger logger = Logger.getLogger(TipoOcupacaoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public TipoOcupacao getById(Long id) {
		return this.entityManager.find(TipoOcupacao.class, id);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoOcupacao> findAll() {
		return entityManager.createQuery("SELECT t FROM TipoOcupacao t ORDER BY t.id").getResultList();
	}

}
