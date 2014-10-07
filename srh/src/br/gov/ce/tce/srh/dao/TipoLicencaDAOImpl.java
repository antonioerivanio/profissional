package br.gov.ce.tce.srh.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.TipoLicenca;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class TipoLicencaDAOImpl implements TipoLicencaDAO {

	static Logger logger = Logger.getLogger(TipoLicencaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public TipoLicenca getById(Long id) {
		return entityManager.find(TipoLicenca.class, id);
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(t.id) from TipoLicenca t ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public TipoLicenca salvar(TipoLicenca entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(TipoLicenca entidade) {
		entityManager.createQuery("DELETE FROM TipoLicenca t WHERE t.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count (t) from TipoLicenca t where upper( t.descricao ) LIKE :descricao ORDER BY t.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoLicenca> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select t from TipoLicenca t where upper( t.descricao ) LIKE :descricao ORDER BY t.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public TipoLicenca getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("Select t from TipoLicenca t where upper( t.descricao ) = :descricao ");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (TipoLicenca) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoLicenca> findAll() {
		Query query = entityManager.createQuery("Select t from TipoLicenca t ORDER BY t.descricao");
		return query.getResultList();
	}

}
