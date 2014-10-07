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

import br.gov.ce.tce.srh.domain.Acrescimo;

/**
*
* @author robstown
* 
*/
@Repository
public class AcrescimoDAOImpl implements AcrescimoDAO {
	
	static Logger logger = Logger.getLogger(AcrescimoDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(e.id) FROM Acrescimo e");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Acrescimo salvar(Acrescimo entidade) {

		if (entidade.getId() == null || entidade.getId() == 0){
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}
	

	@Override
	public void excluir(Acrescimo entidade) {
		Query query = entityManager.createQuery("DELETE FROM Acrescimo e WHERE e.id = :id ");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}


	@Override
	public int count(Long idPessoal) {
		Query query = entityManager.createQuery("SELECT count (e) FROM Acrescimo e WHERE e.pessoal.id = :pessoal");
		query.setParameter("pessoal", idPessoal);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Acrescimo> search(Long idPessoal, int first, int rows) {
		Query query = entityManager.createQuery("SELECT e FROM Acrescimo e WHERE e.pessoal.id = :pessoal order by e.inicio desc ");
		query.setParameter("pessoal", idPessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	
	@Override
	@SuppressWarnings("unchecked")
	public List<Acrescimo> findByPessoal(Long idPessoal) {
		Query query = entityManager.createQuery("SELECT e FROM Acrescimo e join fetch e.pessoal WHERE e.pessoal.id = :pessoal order by e.inicio desc ");
		query.setParameter("pessoal", idPessoal);
		return query.getResultList();
	}

}
