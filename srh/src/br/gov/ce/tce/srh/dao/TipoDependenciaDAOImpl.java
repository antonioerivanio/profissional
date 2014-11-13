package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.TipoDependencia;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class TipoDependenciaDAOImpl implements TipoDependenciaDAO {

	static Logger logger = Logger.getLogger(TipoDependenciaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(d.id) from TipoDependencia d ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public TipoDependencia salvar(TipoDependencia entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(TipoDependencia entidade) {
		entityManager.createQuery("DELETE FROM TipoDependencia t WHERE t.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}

	
	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count (t) from TipoDependencia t where upper( t.descricao ) LIKE :descricao ORDER BY t.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDependencia> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select t from TipoDependencia t where upper( t.descricao ) LIKE :descricao ORDER BY t.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public TipoDependencia getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("Select t from TipoDependencia t where upper( t.descricao ) = :descricao ");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (TipoDependencia) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<TipoDependencia> findAll() {
		Query query = entityManager.createQuery("SELECT t FROM TipoDependencia t ORDER BY t.descricao");		
		return query.getResultList();
	}

}
