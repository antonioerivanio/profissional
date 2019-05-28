package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CursoAcademica;

/**
 *
 * @author robstown
 */
@Repository
public class CursoAcademicaDAOImpl implements CursoAcademicaDAO {

	static Logger logger = Logger.getLogger(CursoAcademicaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public CursoAcademica getById(Long id) {
		return entityManager.find(CursoAcademica.class, id);
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(c.id) from CursoAcademica c");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public CursoAcademica salvar(CursoAcademica entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(CursoAcademica entidade) {
		Query query = entityManager.createQuery("DELETE FROM CursoAcademica c WHERE c.id=:id");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("SELECT count (c) FROM CursoAcademica c WHERE upper( c.descricao ) like :descricao ");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	public int count(Long area, String descricao) {
		Query query = entityManager.createQuery("SELECT count (c) FROM CursoAcademica c WHERE c.area.id = :area and upper( c.descricao ) like :descricao ");
		query.setParameter("area", area);
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CursoAcademica> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("SELECT c FROM CursoAcademica c join fetch c.area WHERE upper( c.descricao ) like :descricao ORDER BY c.area.descricao, c.descricao ");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CursoAcademica> search(Long area, String descricao, int first, int rows) {
		Query query = entityManager.createQuery("SELECT c FROM CursoAcademica c join fetch c.area WHERE c.area.id = :area and upper( c.descricao ) like :descricao ORDER BY c.descricao");
		query.setParameter("area", area);
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public CursoAcademica getByAreaDescricao(Long area, String descricao) {
		try {
			Query query = entityManager.createQuery("Select c from CursoAcademica c where c.area.id = :area and upper( c.descricao ) = :descricao");
			query.setParameter("area", area);
			query.setParameter("descricao", descricao.toUpperCase() );
			return (CursoAcademica) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CursoAcademica> findAll() {
		return entityManager.createQuery("SELECT c FROM CursoAcademica c ORDER BY c.descricao ").getResultList();
	}

}
