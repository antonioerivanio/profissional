package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.AreaProfissional;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class AreaProfissionalDAOImpl implements AreaProfissionalDAO {

	static Logger logger = Logger.getLogger(AreaProfissionalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT max(a.id) FROM AreaProfissional a");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public AreaProfissional salvar(AreaProfissional entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(AreaProfissional entidade) {
		Query query = entityManager.createQuery("DELETE FROM AreaProfissional a WHERE a.id=:id");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("SELECT count (a) FROM AreaProfissional a WHERE upper( a.descricao ) LIKE :descricao ORDER BY a.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AreaProfissional> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("SELECT a FROM AreaProfissional a WHERE upper( a.descricao ) LIKE :descricao ORDER BY a.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public AreaProfissional getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("SELECT a FROM AreaProfissional a WHERE upper( a.descricao ) = :descricao");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (AreaProfissional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AreaProfissional> findAll() {
		return entityManager.createQuery("SELECT a FROM AreaProfissional a ORDER BY a.descricao").getResultList();
	}

}
