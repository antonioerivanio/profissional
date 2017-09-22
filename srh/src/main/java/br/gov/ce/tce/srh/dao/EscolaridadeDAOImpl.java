package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Escolaridade;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class EscolaridadeDAOImpl implements EscolaridadeDAO {

	static Logger logger = Logger.getLogger(EscolaridadeDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from Escolaridade e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Escolaridade salvar(Escolaridade entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(Escolaridade entidade) {
		entityManager.createQuery("DELETE FROM Escolaridade e WHERE e.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count (e) from Escolaridade e where upper( e.descricao ) LIKE :descricao ORDER BY e.ordem");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Escolaridade> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select e from Escolaridade e where upper( e.descricao ) LIKE :descricao ORDER BY e.ordem");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public Escolaridade getByOrdem(Long ordem) {
		try {
			Query query = entityManager.createQuery("Select e from Escolaridade e where e.ordem = :ordem ");
			query.setParameter("ordem", ordem );
			return (Escolaridade) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	public Escolaridade getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("Select e from Escolaridade e where upper( e.descricao ) = :descricao ");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (Escolaridade) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Escolaridade> findAll() {
		return entityManager.createQuery("SELECT e FROM Escolaridade e ORDER BY e.id").getResultList();
	}

}
