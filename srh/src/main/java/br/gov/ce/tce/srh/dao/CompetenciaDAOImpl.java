package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Competencia;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class CompetenciaDAOImpl implements CompetenciaDAO {

	static Logger logger = Logger.getLogger(CompetenciaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery(" Select max(c.id) from Competencia c ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Competencia salvar(Competencia entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(Competencia entidade) {
		entityManager.createQuery("DELETE FROM Competencia c WHERE c.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count (c) as total from Competencia c where upper( c.descricao ) LIKE :descricao ORDER BY c.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Competencia> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select c from Competencia c where upper( c.descricao ) LIKE :descricao ORDER BY c.descricao");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Competencia> search(String tipo) {
		Query query = entityManager.createQuery("Select c from Competencia c where c.tipo = :pTipo ORDER BY c.descricao");
		query.setParameter("pTipo", new Long(tipo));
		return query.getResultList();
	}

	@Override
	public Competencia getById(Long id) {
		return entityManager.find(Competencia.class, id);
	}


	@Override
	public Competencia getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("Select c from Competencia c where upper( c.descricao ) = :descricao ORDER BY c.descricao");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (Competencia) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Competencia> findAll() {
		return entityManager.createQuery("SELECT c FROM Competencia c ORDER BY c.descricao").getResultList();
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Competencia> findByTipo(Long tipo) {
		Query query = entityManager.createQuery("SELECT c FROM Competencia c WHERE c.tipo =:tipo ORDER BY c.descricao");
		query.setParameter("tipo", tipo);
		return query.getResultList();
	}

}
