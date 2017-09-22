package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.AreaSetor;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class AreaSetorDAOImpl implements AreaSetorDAO {

	static Logger logger = Logger.getLogger(AreaSetorDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from AreaSetor e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public AreaSetor salvar(AreaSetor entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(AreaSetor entidade) {
		entityManager.createQuery("DELETE FROM AreaSetor a WHERE a.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(Long setor, String descricao) {
		Query query = entityManager.createQuery("Select count (a) from AreaSetor a where a.setor.id = :setor and upper( a.descricao ) LIKE :descricao ORDER BY a.id");
		query.setParameter("setor", setor);
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AreaSetor> search(Long setor, String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select a from AreaSetor a join fetch a.setor where a.setor.id = :setor and upper( a.descricao ) LIKE :descricao ORDER BY a.id");
		query.setParameter("setor", setor);
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public AreaSetor getBySetorDescricao(Long setor, String descricao) {
		try {
			Query query = entityManager.createQuery("Select a from AreaSetor a join fetch a.setor where a.setor.id = :setor and upper( a.descricao ) = :descricao ");
			query.setParameter("setor", setor);
			query.setParameter("descricao", descricao.toUpperCase() );
			return (AreaSetor) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AreaSetor> findBySetor(Long setor) {
		Query query = entityManager.createQuery("Select a from AreaSetor a join fetch a.setor where a.setor.id = :setor ORDER BY a.id");
		query.setParameter("setor", setor);
		return query.getResultList();
	}

}
