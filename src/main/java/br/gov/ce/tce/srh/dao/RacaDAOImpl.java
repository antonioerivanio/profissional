package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Raca;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class RacaDAOImpl implements RacaDAO {

	static Logger logger = Logger.getLogger(RacaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId(){
		Query query = entityManager.createQuery(" Select max(r.id) from Raca r ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Raca salvar(Raca entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(Raca entidade) {
		entityManager.createQuery("DELETE FROM Raca r WHERE r.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count (r) from Raca r where upper( r.descricao ) LIKE :descricao ORDER BY r.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Raca> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select r from Raca r where upper( r.descricao ) LIKE :descricao ORDER BY r.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public Raca getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("Select r from Raca r where upper( r.descricao ) = :descricao ");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (Raca) query.getSingleResult();	
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Raca> findAll() {
		return entityManager.createQuery("SELECT r FROM Raca r ORDER BY r.id").getResultList();
	}

}
