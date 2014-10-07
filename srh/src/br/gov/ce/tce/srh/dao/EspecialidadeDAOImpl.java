package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Especialidade;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class EspecialidadeDAOImpl implements EspecialidadeDAO {

	static Logger logger = Logger.getLogger(EspecialidadeDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from Especialidade e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Especialidade salvar(Especialidade entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(Especialidade entidade) {
		entityManager.createQuery("DELETE FROM Especialidade e WHERE e.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count (e) from Especialidade e where upper( e.descricao ) LIKE :descricao ORDER BY e.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Especialidade> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select e from Especialidade e where upper( e.descricao ) LIKE :descricao ORDER BY e.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public Especialidade getById(Long id) {
		return entityManager.find(Especialidade.class, id);
	}


	@Override
	public Especialidade getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("Select e from Especialidade e where upper( e.descricao ) = :descricao ");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (Especialidade) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Especialidade> findByDescricao(String descricao) {
		Query query = entityManager.createQuery("Select e from Especialidade e where upper( e.descricao ) LIKE :descricao ORDER BY e.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Especialidade> findAll() {
		return entityManager.createQuery("SELECT e FROM Especialidade e ORDER BY e.id").getResultList();
	}

}
