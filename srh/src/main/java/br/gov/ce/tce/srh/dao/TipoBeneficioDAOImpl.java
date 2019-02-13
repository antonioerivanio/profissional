package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.TipoBeneficio;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class TipoBeneficioDAOImpl implements TipoBeneficioDAO {

	static Logger logger = Logger.getLogger(TipoBeneficioDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(t.id) from TipoBeneficio t ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public TipoBeneficio salvar(TipoBeneficio entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(TipoBeneficio entidade) {
		entityManager.createQuery("DELETE FROM TipoBeneficio t WHERE t.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<TipoBeneficio> findAll() {
		return entityManager.createQuery("SELECT t FROM TipoBeneficio t ORDER BY t.id").getResultList();
	}
	
	
	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count (t) from TipoBeneficio t where upper( t.descricao ) LIKE :descricao ORDER BY t.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoBeneficio> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select t from TipoBeneficio t where upper( t.descricao ) LIKE :descricao ORDER BY t.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public TipoBeneficio getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("Select t from TipoBeneficio t where upper( t.descricao ) = :descricao ");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (TipoBeneficio) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}	

}
