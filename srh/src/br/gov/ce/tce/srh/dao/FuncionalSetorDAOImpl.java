package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.FuncionalSetor;

/**
 *
 * @author wesllhey
 *
 */
@Repository
public class FuncionalSetorDAOImpl implements FuncionalSetorDAO {

	static Logger logger = Logger.getLogger(FuncionalSetorDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT max(f.id) FROM FuncionalSetor f");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public FuncionalSetor salvar(FuncionalSetor entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)){
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(FuncionalSetor entidade) {
//		Query query = entityManager.createQuery("DELETE FROM FuncionalSetor l WHERE l.id = :id");
//		query.setParameter("id", entidade.getId());
//		query.executeUpdate();
		
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}


	@Override
	public void excluirAll(Long idFuncional) {
//		Query query = entityManager.createQuery("DELETE FROM FuncionalSetor l WHERE l.funcional.id = :idFuncional");
//		query.setParameter("idFuncional", idFuncional);
//		query.executeUpdate();
		
		List<FuncionalSetor> funcionalSetorList = this.findByFuncional(idFuncional);
		
		for (FuncionalSetor funcionalSetor : funcionalSetorList) {
			entityManager.remove(funcionalSetor);
		}
	}


	@Override
	public int count(Long pessoal) {
		Query query = entityManager.createQuery("Select count (f) from FuncionalSetor f where f.funcional.pessoal.id = :pessoal ORDER BY f.dataInicio DESC");
		query.setParameter("pessoal", pessoal);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<FuncionalSetor> search(Long pessoal, int first, int rows) {
		Query query = entityManager.createQuery("Select f from FuncionalSetor f where f.funcional.pessoal.id = :pessoal ORDER BY f.dataInicio DESC");
		query.setParameter("pessoal", pessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<FuncionalSetor> findByPessoal(Long idPessoa) {
		Query query = entityManager.createQuery("Select f from FuncionalSetor f where f.funcional.pessoal.id = :pessoal ORDER BY f.dataInicio DESC");
		query.setParameter("pessoal", idPessoa);
		return query.getResultList();
	}
	
	@Override
	public FuncionalSetor getAtivoByFuncional(Long idFuncional) {
		try {
			Query query = entityManager.createQuery("Select f from FuncionalSetor f where f.funcional.id = :funcional AND f.dataFim IS NULL ");
			query.setParameter("funcional", idFuncional);
			return (FuncionalSetor) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FuncionalSetor> findByFuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select f from FuncionalSetor f where f.funcional.id = :funcional");
		query.setParameter("funcional", idFuncional);
		return query.getResultList();
	}

}