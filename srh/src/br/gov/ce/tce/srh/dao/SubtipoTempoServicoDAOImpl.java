package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.SubtipoTempoServico;

@Repository
public class SubtipoTempoServicoDAOImpl implements SubtipoTempoServicoDAO {

	static Logger logger = Logger.getLogger(VinculoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@Override
	public SubtipoTempoServico getById(Long id) {
		return entityManager.find(SubtipoTempoServico.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SubtipoTempoServico> findByTipoTempoServico(Long idTipoTempoServico) {
		return entityManager.createQuery("SELECT s FROM SubtipoTempoServico s WHERE s.idTipoTempoServico = :idTipoTempoServico ORDER BY s.id")
				.setParameter("idTipoTempoServico", idTipoTempoServico)
				.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SubtipoTempoServico> findAll() {
		return entityManager.createQuery("SELECT s FROM SubtipoTempoServico s ORDER BY s.id").getResultList();
	}

}
