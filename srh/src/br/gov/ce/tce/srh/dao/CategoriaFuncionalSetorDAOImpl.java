package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetor;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

@Repository
public class CategoriaFuncionalSetorDAOImpl implements CategoriaFuncionalSetorDAO {

	static Logger logger = Logger.getLogger(CategoriaFuncionalSetorDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(c.id) FROM CategoriaFuncionalSetor c ORDER BY c.id");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	@Override
	public int count(Setor setor) {
		Query query = entityManager.createQuery("Select count(c) from CategoriaFuncionalSetor c " +
				"join   c.setor where c.setor.id = :setor ORDER BY c.id");
		query.setParameter("setor", setor.getId());
		return ((Long) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaFuncionalSetor> search(Setor setor, int first, int rows) {
		Query query = entityManager.createQuery("Select c from CategoriaFuncionalSetor c " +
				"join  c.setor where c.setor.id = :setor ORDER BY c.id");
		query.setParameter("setor", setor.getId());
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@Override
	public CategoriaFuncionalSetor salvar(CategoriaFuncionalSetor entidade) {
		if (entidade.getId() == null || entidade.getId() == 0) {
			entidade.setId(getMaxId());
		}
		return entityManager.merge(entidade);
	}

	@Override
	public void excluir(CategoriaFuncionalSetor entidade) {
		entityManager.createQuery("DELETE FROM CategoriaFuncionalSetor c WHERE c.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}

	@Override
	public CategoriaFuncionalSetor findById(Long id){
		return entityManager.find(CategoriaFuncionalSetor.class, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaFuncionalSetor> findAll() {
		return entityManager.createQuery("SELECT c FROM CategoriaFuncionalSetor c ORDER BY c.categoriaFuncional.descricao").getResultList();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaFuncionalSetor> findBySetor(Setor setor) {
		Query query = entityManager.createQuery("Select c from CategoriaFuncionalSetor c " +
				"join   c.setor where c.setor.id = :setor ORDER BY c.id");
		query.setParameter("setor", setor.getId());
		return query.getResultList();
	}
}
