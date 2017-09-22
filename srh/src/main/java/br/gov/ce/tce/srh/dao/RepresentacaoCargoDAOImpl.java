package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.RepresentacaoCargo;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class RepresentacaoCargoDAOImpl implements RepresentacaoCargoDAO {

	static Logger logger = Logger.getLogger(RepresentacaoCargoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(r.id) from RepresentacaoCargo r ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public RepresentacaoCargo salvar(RepresentacaoCargo entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(RepresentacaoCargo entidade) {
		entityManager.createQuery("DELETE FROM RepresentacaoCargo r WHERE r.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(String nomenclatura) {
		Query query = entityManager.createQuery("Select count (r) from RepresentacaoCargo r where upper( r.nomenclatura ) LIKE :nomenclatura ORDER BY r.ativo desc, r.ordem ");
		query.setParameter("nomenclatura", "%" + nomenclatura.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<RepresentacaoCargo> search(String nomenclatura, int first, int rows) {
		Query query = entityManager.createQuery("Select r from RepresentacaoCargo r where upper( r.nomenclatura ) LIKE :nomenclatura ORDER BY r.ativo desc, r.ordem ");
		query.setParameter("nomenclatura", "%" + nomenclatura.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public RepresentacaoCargo getByNomenclaturaSimbolo(String nomenclatura, String simbolo) {
		try {
			Query query = entityManager.createQuery("Select r from RepresentacaoCargo r where upper( r.nomenclatura ) = :nomenclatura AND upper( r.simbolo ) = :simbolo ");
			query.setParameter("nomenclatura", nomenclatura.toUpperCase() );
			query.setParameter("simbolo", simbolo.toUpperCase() );
			return (RepresentacaoCargo) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<RepresentacaoCargo> findAll() {
		return entityManager.createNamedQuery("RepresentacaoCargo.findAll").getResultList();
	}

}
