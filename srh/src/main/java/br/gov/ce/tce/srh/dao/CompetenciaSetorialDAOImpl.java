package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CompetenciaSetorial;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

/**
*
* @author Carlos.almeida
* 
*/
@Repository
public class CompetenciaSetorialDAOImpl implements CompetenciaSetorialDAO {

	static Logger logger = Logger.getLogger(CompetenciaSetorialDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery(" Select max(c.id) from CompetenciaSetorial c ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}
	
	@Override
	public int count(Setor setor, Long tipo) {
		Query query = entityManager.createQuery("Select count(c) from CompetenciaSetorial c " +
				"join   c.setor join  c.competencia " +
				"where c.setor.id = :setor and c.competencia.tipo =:tipo ORDER BY c.id");
		query.setParameter("setor", setor.getId());
		query.setParameter("tipo", tipo);
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@Override
	public int count(Setor setor) {
		Query query = entityManager.createQuery("Select count(c) from CompetenciaSetorial c " +
				"join   c.setor where c.setor.id = :setor ORDER BY c.id");
		query.setParameter("setor", setor.getId());
		return ((Long) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CompetenciaSetorial> search(Setor setor, Long tipo, int first,
			int rows) {
		Query query = entityManager.createQuery("Select c from CompetenciaSetorial c " +
				"join  c.setor join   c.competencia " +
				"where c.setor.id = :setor and c.competencia.tipo =:tipo ORDER BY c.id");
		query.setParameter("setor", setor.getId());
		query.setParameter("tipo", tipo);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<CompetenciaSetorial> search(Setor setor, int first,
			int rows) {
		Query query = entityManager.createQuery("Select c from CompetenciaSetorial c " +
				"join  c.setor where c.setor.id = :setor ORDER BY c.id");
		query.setParameter("setor", setor.getId());
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@Override
	public CompetenciaSetorial salvar(CompetenciaSetorial entidade) {
		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	@Override
	public void excluir(CompetenciaSetorial entidade) {
		entityManager.createQuery("DELETE FROM CompetenciaSetorial c WHERE c.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}

	@Override
	public CompetenciaSetorial getById(Long id) {
		return entityManager.find(CompetenciaSetorial.class, id);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CompetenciaSetorial> findAll() {
		return entityManager.createQuery("SELECT c FROM CompetenciaSetorial c ORDER BY c.id").getResultList();
	}
	
	

}
