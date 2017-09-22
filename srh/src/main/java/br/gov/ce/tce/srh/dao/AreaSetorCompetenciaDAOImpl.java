package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.AreaSetorCompetencia;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class AreaSetorCompetenciaDAOImpl implements AreaSetorCompetenciaDAO {

	static Logger logger = Logger.getLogger(AreaSetorCompetenciaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from AreaSetorCompetencia e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public AreaSetorCompetencia salvar(AreaSetorCompetencia entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(AreaSetorCompetencia entidade) {
		entityManager.createQuery("DELETE FROM AreaSetorCompetencia a WHERE a.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(Long area) {
		Query query = entityManager.createQuery("Select count (a) from AreaSetorCompetencia a where a.areaSetor.id = :idAreaSetor ORDER BY a.id ");
		query.setParameter("idAreaSetor", area);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	public int count(Long area, Long competencia) {
		Query query = entityManager.createQuery("Select count (a) from AreaSetorCompetencia a where a.areaSetor.id = :idAreaSetor AND a.competencia.id = :idCompetencia ORDER BY a.id ");
		query.setParameter("idAreaSetor", area);
		query.setParameter("idCompetencia", competencia);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AreaSetorCompetencia> search(Long area, int first, int rows) {
		Query query = entityManager.createQuery("Select a from AreaSetorCompetencia a join fetch a.areaSetor join fetch a.competencia where a.areaSetor.id = :idAreaSetor ORDER BY a.id ");
		query.setParameter("idAreaSetor", area);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AreaSetorCompetencia> search(Long area, Long competencia, int first, int rows) {
		Query query = entityManager.createQuery("Select a from AreaSetorCompetencia a join fetch a.areaSetor join fetch a.competencia where a.areaSetor.id = :idAreaSetor AND a.competencia.id = :idCompetencia ORDER BY a.id ");
		query.setParameter("idAreaSetor", area);
		query.setParameter("idCompetencia", competencia);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AreaSetorCompetencia> findBySetor(Long setor) {
		Query query = entityManager.createQuery("Select a from AreaSetorCompetencia a join fetch a.areaSetor join fetch a.competencia where a.areaSetor.setor.id = :setor ORDER BY a.competencia ");
		query.setParameter("setor", setor);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AreaSetorCompetencia> findByArea(Long area) {
		Query query = entityManager.createQuery("Select a from AreaSetorCompetencia a join fetch a.areaSetor join fetch a.competencia where a.areaSetor.id = :idAreaSetor ORDER BY a.id ");
		query.setParameter("idAreaSetor", area);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AreaSetorCompetencia> findByAreaCompetencia(Long idAreaSetor, Long idCompetencia){
		Query query = entityManager.createQuery("Select a from AreaSetorCompetencia a join fetch a.areaSetor join fetch a.competencia where a.areaSetor.id = :idAreaSetor AND a.competencia.id = :idCompetencia ORDER BY a.id ");
		query.setParameter("idAreaSetor", idAreaSetor);
		query.setParameter("idCompetencia", idCompetencia);
		return query.getResultList();
	}

}
