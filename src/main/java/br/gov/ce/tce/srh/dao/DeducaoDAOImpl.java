/**
 * 
 */
package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Deducao;

/**
*
* @author robstown
* 
*/
@Repository
public class DeducaoDAOImpl implements DeducaoDAO {
	
	static Logger logger = Logger.getLogger(DeducaoDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(e.id) FROM Deducao e");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Deducao salvar(Deducao entidade) {

		if (entidade.getId() == null || entidade.getId() == 0){
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}
	

	@Override
	public void excluir(Deducao entidade) {
		Query query = entityManager.createQuery("DELETE FROM Deducao e WHERE e.id = :id ");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}


	@Override
	public int count(Long idPessoal) {
		Query query = entityManager.createQuery("SELECT count (e) FROM Deducao e WHERE e.pessoal.id = :pessoal");
		query.setParameter("pessoal", idPessoal);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Deducao> search(Long idPessoal, int first, int rows) {
		Query query = entityManager.createQuery("SELECT e FROM Deducao e WHERE e.pessoal.id = :pessoal ORDER BY e.anoReferencia DESC, e.inicio desc ");
		query.setParameter("pessoal", idPessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<Deducao> findByPessoal(Long idPessoal) {
		Query query = entityManager.createQuery("SELECT e FROM Deducao e WHERE e.pessoal.id = :pessoal order by e.inicio desc ");
		query.setParameter("pessoal", idPessoal);
		return query.getResultList();
	}

}
