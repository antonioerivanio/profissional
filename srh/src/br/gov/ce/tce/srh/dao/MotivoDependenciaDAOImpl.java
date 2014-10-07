package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.MotivoDependencia;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class MotivoDependenciaDAOImpl implements MotivoDependenciaDAO {

	static Logger logger = Logger.getLogger(MotivoDependenciaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(m.id) from MotivoDependencia m ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public MotivoDependencia salvar(MotivoDependencia entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(MotivoDependencia entidade) {
		entityManager.createQuery("DELETE FROM MotivoDependencia m WHERE m.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count (m) from MotivoDependencia m where upper( m.descricao ) LIKE :descricao ORDER BY m.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<MotivoDependencia> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select m from MotivoDependencia m where upper( m.descricao ) LIKE :descricao ORDER BY m.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public MotivoDependencia getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("Select m from MotivoDependencia m where upper( m.descricao ) = :descricao ");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (MotivoDependencia) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
