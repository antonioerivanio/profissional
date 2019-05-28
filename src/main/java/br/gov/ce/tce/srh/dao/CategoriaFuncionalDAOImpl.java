package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CategoriaFuncional;

@Repository
public class CategoriaFuncionalDAOImpl implements CategoriaFuncionalDAO {

	static Logger logger = Logger.getLogger(CategoriaFuncionalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	@Override
	public CategoriaFuncional salvar(CategoriaFuncional entidade) {
		if (entidade.getId() == null || entidade.getId() == 0) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	private Long getMaxId() {
		Query query = entityManager
				.createQuery("SELECT MAX(c.id) FROM CategoriaFuncional c ORDER BY c.descricao");
		return query.getSingleResult() == null ? 1 : (Long) query
				.getSingleResult() + 1;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaFuncional> buscarCategorias() {
		Query query = entityManager
				.createQuery("SELECT c FROM CategoriaFuncional c");
		return query.getResultList();
	}

	@Override
	public int count(String descricaoCategoria) {
		Query query = entityManager
				.createQuery("SELECT count(c) FROM CategoriaFuncional c WHERE c.descricao LIKE CONCAT('%',:descricaoCategoria, '%')");
		query.setParameter("descricaoCategoria", descricaoCategoria);
		return ((Long) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaFuncional> search(String descricaoCategoria,
			int first, int rows) {
		Query query = entityManager
				.createQuery("SELECT c FROM CategoriaFuncional c WHERE c.descricao LIKE CONCAT('%',:descricaoCategoria, '%')");
		query.setParameter("descricaoCategoria", descricaoCategoria);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaFuncional> findAll() {
		return entityManager.createNamedQuery("CategoriaFuncional.findAll")
				.getResultList();
	}

	@Override
	public void excluir(CategoriaFuncional entidade) {
		Query query = entityManager
				.createQuery("DELETE FROM CategoriaFuncional c WHERE c.id = :id ");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();

	}

	@Override
	public CategoriaFuncional getById(Long id) {
		return entityManager.find(CategoriaFuncional.class, id);
	}
}
