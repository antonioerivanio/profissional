package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Instituicao;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class InstituicaoDAOImpl implements InstituicaoDAO {

	static Logger logger = Logger.getLogger(InstituicaoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(i.id) from Instituicao i ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Instituicao salvar(Instituicao entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(Instituicao entidade) {
		entityManager.createQuery("DELETE FROM Instituicao i WHERE i.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count (i) from Instituicao i where upper( i.descricao ) LIKE :descricao ORDER BY i.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Instituicao> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select i from Instituicao i where upper( i.descricao ) LIKE :descricao ORDER BY i.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public Instituicao getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("Select i from Instituicao i where upper( i.descricao ) = :descricao ");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (Instituicao) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Instituicao> findAll() {
		return entityManager.createQuery("SELECT i FROM Instituicao i ORDER BY i.descricao").getResultList();
	}

}
