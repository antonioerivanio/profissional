package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.FuncionalAreaSetor;

/**
 *
 * @author wesllhey
 * 
 */
@Repository
public class FuncionalAreaSetorDAOImpl implements FuncionalAreaSetorDAO {

	static Logger logger = Logger.getLogger(FuncionalAreaSetorDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {		
		this.entityManager = entityManager;
	}


	@Override
	public FuncionalAreaSetor salvar(FuncionalAreaSetor entidade) {
		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(FuncionalAreaSetor entidade) {
		Query query = entityManager.createQuery("DELETE FROM FuncionalAreaSetor a WHERE a.pk.funcional = :idFuncional AND a.pk.areaSetor = :area ");
		query.setParameter("idFuncional", entidade.getPk().getFuncional() );
		query.setParameter("area", entidade.getPk().getAreaSetor() );
		query.executeUpdate();
	}


	@Override
	public int count(Long funcional) {
		Query query = entityManager.createQuery("Select count (a.funcional.id) from FuncionalAreaSetor a where a.pk.funcional = :idFuncional ORDER BY a.pk.areaSetor ");
		query.setParameter("idFuncional", funcional);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<FuncionalAreaSetor> search(Long funcional, int first, int rows) {
		Query query = entityManager.createQuery("Select a from FuncionalAreaSetor a where a.pk.funcional = :idFuncional ORDER BY a.pk.areaSetor ");
		query.setParameter("idFuncional", funcional);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<FuncionalAreaSetor> findByFuncional(Long funcional) {
		Query query = entityManager.createQuery("Select a from FuncionalAreaSetor a where a.pk.funcional = :idFuncional ORDER BY a.pk.areaSetor ");
		query.setParameter("idFuncional", funcional);
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<FuncionalAreaSetor> findByFuncionalComSetorAtual(Long funcional) {
		Query query = entityManager.createQuery("Select a from FuncionalAreaSetor a where a.pk.funcional = :idFuncional and a.funcional.setor.id=a.areaSetor.setor.id ORDER BY a.pk.areaSetor ");
		query.setParameter("idFuncional", funcional);
		return query.getResultList();
	}



	@Override
	@SuppressWarnings("unchecked")
	public List<FuncionalAreaSetor> findyByAreaSetor(Long areaSetor) {
		Query query = entityManager.createQuery("Select a from FuncionalAreaSetor a where a.areaSetor.id = :areaSetor");
		query.setParameter("areaSetor", areaSetor);
		return query.getResultList();
	}

}
