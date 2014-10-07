package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.FuncionalAnotacao;

/**
*
* @author robstown
* 
*/
@Repository
public class FuncionalAnotacaoDAOImpl implements FuncionalAnotacaoDAO {

	static Logger logger = Logger.getLogger(FuncionalAnotacaoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT max(p.id) FROM FuncionalAnotacao p");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public FuncionalAnotacao salvar(FuncionalAnotacao entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)){
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(FuncionalAnotacao entidade) {
		Query query = entityManager.createQuery("DELETE FROM FuncionalAnotacao p WHERE p.id = :id");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}


	@Override
	public int count(Long pessoal) {
		Query query = entityManager.createQuery("Select count (p) from FuncionalAnotacao p where p.funcional.pessoal.id = :pessoal ORDER BY p.data DESC");
		query.setParameter("pessoal", pessoal);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<FuncionalAnotacao> search(Long pessoal, int first, int rows) {
		Query query = entityManager.createQuery("Select p from FuncionalAnotacao p where p.funcional.pessoal.id = :pessoal ORDER BY p.data DESC");
		query.setParameter("pessoal", pessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<FuncionalAnotacao> findByPessoal(Long idPessoa) {
		Query query = entityManager.createQuery("Select p from FuncionalAnotacao p where p.funcional.pessoal.id = :pessoal ORDER BY p.data DESC");
		query.setParameter("pessoal", idPessoa);
		return query.getResultList();
	}

}