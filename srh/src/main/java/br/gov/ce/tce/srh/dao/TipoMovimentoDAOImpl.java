package br.gov.ce.tce.srh.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.TipoMovimento;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class TipoMovimentoDAOImpl implements TipoMovimentoDAO {

	static Logger logger = Logger.getLogger(TipoMovimentoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public TipoMovimento getById(Long id) {
		return entityManager.find(TipoMovimento.class, id);
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(t.id) from TipoMovimento t ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public TipoMovimento salvar(TipoMovimento entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)){
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(TipoMovimento entidade) {
		entityManager.createQuery("DELETE FROM TipoMovimento t WHERE t.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count (t) from TipoMovimento t where upper( t.descricao ) LIKE :descricao ORDER BY t.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoMovimento> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select t from TipoMovimento t where upper( t.descricao ) LIKE :descricao ORDER BY t.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	
	@Override
	public TipoMovimento getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("Select t from TipoMovimento t where upper( t.descricao ) = :descricao ");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (TipoMovimento) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoMovimento> findByTipo(Long tipo) {
		Query query = entityManager.createQuery("Select t from TipoMovimento t where t.tipo = :tipo ORDER BY t.descricao ");
		query.setParameter("tipo", tipo);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<TipoMovimento> findByDescricaoForId(Long id) {
		Query query = entityManager.createQuery("Select t from TipoMovimento t where t.id = :id ORDER BY t.descricao ");
		query.setParameter("id", id);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<TipoMovimento> findAll() {
		return entityManager.createQuery("SELECT t FROM TipoMovimento t ORDER BY t.descricao ").getResultList();
	}

}
