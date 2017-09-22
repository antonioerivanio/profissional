package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.EstadoCivil;

/**
 *
 * @author Robstown
 * 
 */
@Repository
public class EstadoCivilDAOImpl implements EstadoCivilDAO {

	static Logger logger = Logger.getLogger(EstadoCivilDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT max(e.id) from EstadoCivil e");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public EstadoCivil salvar(EstadoCivil entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(EstadoCivil entidade) {
		Query query = entityManager.createQuery("DELETE FROM EstadoCivil e WHERE e.id = :id");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("SELECT count (e) FROM EstadoCivil e WHERE upper( e.descricao ) like :descricao ORDER BY e.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<EstadoCivil> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("SELECT e FROM EstadoCivil e WHERE upper( e.descricao ) like :descricao ORDER BY e.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public EstadoCivil getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("SELECT e FROM EstadoCivil e WHERE upper( e.descricao ) = :descricao");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (EstadoCivil) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<EstadoCivil> findAll() {
		return entityManager.createQuery("SELECT e FROM EstadoCivil e ORDER BY e.id").getResultList();
	}

}
