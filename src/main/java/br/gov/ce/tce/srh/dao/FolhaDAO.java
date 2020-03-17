package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Folha;

@Repository
public class FolhaDAO {

	static Logger logger = Logger.getLogger(FolhaDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Folha getById(Long id) {
		return entityManager.find(Folha.class, id);
	}
	
	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(f.id) from Folha f ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public Folha salvar(Folha entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(Folha entidade) {
		Query query = entityManager.createNamedQuery("Folha.delete");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();			
	}

	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count(f) from Folha f where upper( f.descricao ) LIKE :descricao ORDER BY f.descricao");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Folha> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select f from Folha f where upper( f.descricao ) LIKE :descricao ORDER BY f.descricao");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	public Folha getByCodigo(String codigo) {

		if (codigo.length() == 1)
			codigo += " ";

		try {
			Query query = entityManager.createNamedQuery("Folha.getByCodigo");
			query.setParameter("codigo", codigo.toUpperCase() );
			return (Folha) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Folha getByDescricao(String descricao) {
		try {
			Query query = entityManager.createNamedQuery("Folha.getByDescricao");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (Folha) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Folha> findByAtivo(Boolean ativo) {
		Query query = entityManager.createQuery("Select f from Folha f where f.ativo = :ativo ORDER BY f.descricao asc");
		query.setParameter("ativo", ativo);
		return query.getResultList();
	}

}
