package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.AreaAcademica;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class AreaAcademicaDAOImpl implements AreaAcademicaDAO {

	static Logger logger = Logger.getLogger(AreaAcademicaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(a.id) from AreaAcademica a ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public AreaAcademica salvar(AreaAcademica entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(AreaAcademica entidade) {
		Query query = entityManager.createQuery("DELETE FROM AreaAcademica a WHERE a.id=:id");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("SELECT count (a) FROM AreaAcademica a  WHERE upper( a.descricao ) like :descricao ORDER BY a.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AreaAcademica> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("SELECT a FROM AreaAcademica a  WHERE upper( a.descricao ) like :descricao ORDER BY a.id");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public AreaAcademica getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("SELECT a FROM AreaAcademica a WHERE upper( a.descricao ) = :descricao");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (AreaAcademica) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AreaAcademica> findAll() {
		return entityManager.createQuery("SELECT a FROM AreaAcademica a ORDER BY a.id").getResultList();
	}

}
