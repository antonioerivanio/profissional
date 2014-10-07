package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.ReferenciaFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Repository
public class ReferenciaFuncionalDAOImpl implements ReferenciaFuncionalDAO {

	static Logger logger = Logger.getLogger(ReferenciaFuncionalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public ReferenciaFuncional getById(Long id) {
		return entityManager.find(ReferenciaFuncional.class, id);
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(rf.id) from ReferenciaFuncional rf ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public void salvar(ReferenciaFuncional entidade) throws SRHRuntimeException {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId( getMaxId() );
		}

		entityManager.merge(entidade);
	}


	@Override
	public void excluirAll(Long idFuncional) throws SRHRuntimeException {
		entityManager.createQuery("DELETE FROM ReferenciaFuncional rf WHERE rf.funcional.id = :id").setParameter("id", idFuncional).executeUpdate();		
	}


	@Override
	public int count(String matricula) {
		Query query = entityManager.createQuery("Select count (rf) from ReferenciaFuncional rf join rf.funcional f where f.matricula = :matricula ");
		query.setParameter("matricula", matricula);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<ReferenciaFuncional> search(String matricula, int first, int rows) {
		Query query = entityManager.createQuery("Select rf from ReferenciaFuncional rf join rf.funcional f where f.matricula = :matricula order by rf.inicio desc ");
		query.setParameter("matricula", matricula);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public ReferenciaFuncional getAtivoByFuncional(Long idFuncional) {
		try {
			Query query = entityManager.createQuery("Select rf from ReferenciaFuncional rf where rf.funcional.id = :funcional AND rf.fim IS NULL ");
			query.setParameter("funcional", idFuncional);
			return (ReferenciaFuncional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<ReferenciaFuncional> findByPessoa(Long idPessoa) {
		Query query = entityManager.createQuery("Select rf from ReferenciaFuncional rf where rf.funcional.pessoal.id = :pessoal order by rf.fim desc ");
		query.setParameter("pessoal", idPessoa);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<ReferenciaFuncional> findByFuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select rf from ReferenciaFuncional rf where rf.funcional.id = :funcional order by rf.fim desc ");
		query.setParameter("funcional", idFuncional);
		return query.getResultList();
	}
	
}
