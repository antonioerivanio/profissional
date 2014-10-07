package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CategoriaSetorPessoal;

@Repository
public class CategoriaSetorPessoalDAOImpl implements CategoriaSetorPessoalDAO {

	static Logger logger = Logger.getLogger(CategoriaSetorPessoalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(c.id) FROM CategoriaSetorPessoal c");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public void salvar(CategoriaSetorPessoal entidade) {
		if (entidade.getId() == null || entidade.getId() == 0) {
			entidade.setId(getMaxId());
		}
		entityManager.merge(entidade);
	}

	@Override
	public int count(Long pessoa) {
		Query query = entityManager.createQuery("SELECT count (c) FROM CategoriaSetorPessoal c where c.pessoal.id =:id");
		query.setParameter("id", pessoa);
		return ((Long) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CategoriaSetorPessoal> search(Long pessoa, int first, int rows) {
		Query query = entityManager.createQuery("SELECT c FROM CategoriaSetorPessoal c where c.pessoal.id =:id");
		query.setParameter("id", pessoa);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@Override
	public void excluir(CategoriaSetorPessoal entidade) {
		Query query = entityManager.createQuery("DELETE FROM CategoriaSetorPessoal c WHERE c.id = :id");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}

}
