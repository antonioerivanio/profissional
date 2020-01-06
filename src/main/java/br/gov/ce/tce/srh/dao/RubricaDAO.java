package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Rubrica;

@Repository
public class RubricaDAO {

	static Logger logger = Logger.getLogger(RubricaDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(r.id) from Rubrica r ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	
	public Rubrica salvar(Rubrica entidade) {
		
		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}
	

	public void excluir(Rubrica entidade) {
		entityManager.createQuery("DELETE FROM Rubrica r WHERE r.id=:id").setParameter("id", entidade.getId()).executeUpdate();			
	}


	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count(r) from Rubrica r where upper( r.descricao ) LIKE :descricao ORDER BY r.ordem ");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	public List<Rubrica> search(String descricao, int first, int rows) {
		TypedQuery<Rubrica> query = entityManager.createQuery("Select r from Rubrica r where upper( r.descricao ) LIKE :descricao ORDER BY r.ordem ", Rubrica.class);
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	public List<Rubrica> findAll() {
		TypedQuery<Rubrica> query = entityManager.createQuery("Select r from Rubrica r ORDER BY r.descricao ", Rubrica.class);
		return query.getResultList();
	}

	
	public Rubrica getByCodigo(String codigo) {
		try {
			Query query = entityManager.createQuery("Select r from Rubrica r where upper( r.codigo) = :codigo ");
			query.setParameter("codigo", codigo.toUpperCase() );
			return (Rubrica) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	public Rubrica getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("Select r from Rubrica r where upper( r.descricao ) = :descricao ");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (Rubrica) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
