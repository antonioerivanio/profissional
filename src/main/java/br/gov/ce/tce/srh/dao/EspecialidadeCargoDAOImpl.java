package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.EspecialidadeCargo;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class EspecialidadeCargoDAOImpl implements EspecialidadeCargoDAO {

	static Logger logger = Logger.getLogger(EspecialidadeCargoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from EspecialidadeCargo e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public EspecialidadeCargo salvar(EspecialidadeCargo entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	
	@Override
	public void excluir(EspecialidadeCargo entidade) {
		Query query = entityManager.createQuery("DELETE FROM EspecialidadeCargo e WHERE e.id=:id");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}


	@Override
	public void excluirAll(Long ocupacao) {
		entityManager.createQuery("DELETE FROM EspecialidadeCargo e WHERE e.ocupacao.id = :id").setParameter("id", ocupacao).executeUpdate();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<EspecialidadeCargo> findByOcupacao(Long ocupacao) {
		Query query = entityManager.createQuery("Select e from EspecialidadeCargo e join fetch e.ocupacao join fetch e.especialidade where e.ocupacao.id = :ocupacao ");
		query.setParameter("ocupacao", ocupacao );
		return query.getResultList();
	}
	
	
	@Override
	public EspecialidadeCargo getById(Long id) {
		return entityManager.find(EspecialidadeCargo.class, id);
	}

}
