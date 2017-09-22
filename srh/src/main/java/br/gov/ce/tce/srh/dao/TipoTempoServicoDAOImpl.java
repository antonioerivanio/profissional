package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.TipoTempoServico;

@Repository
public class TipoTempoServicoDAOImpl implements TipoTempoServicoDAO{

	static Logger logger = Logger.getLogger(VinculoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public TipoTempoServico getById(Long id) {
		return entityManager.find(TipoTempoServico.class, id);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoTempoServico> findAll() {
		return entityManager.createQuery("SELECT t FROM TipoTempoServico t ORDER BY t.id").getResultList();
	}

}
