/**
 * 
 */
package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.AtestoPessoa;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author joel.barbosa
 *
 */
@Repository
public class AtestoPessoaDAOImpl implements AtestoPessoaDAO {

	static Logger logger = Logger.getLogger(AtestoPessoaDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(e.id) FROM AtestoPessoa e");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public AtestoPessoa salvar(AtestoPessoa entidade) throws SRHRuntimeException {

		if (entidade.getId() == null || entidade.getId() == 0 ) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(AtestoPessoa entidade) {
		Query query = entityManager.createQuery("DELETE FROM AtestoPessoa e WHERE e.id = :id");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
		
	}


	@Override
	public int count(Long pessoa) {
		Query query = entityManager.createQuery("SELECT count (e) FROM AtestoPessoa e WHERE e.pessoal.id = :pessoa ORDER BY e.competencia.descricao ");
		query.setParameter("pessoa", pessoa);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AtestoPessoa> search(Long pessoa, int first, int rows) {
		Query query = entityManager.createQuery("SELECT e FROM AtestoPessoa e join fetch e.competencia WHERE e.pessoal.id = :pessoa ORDER BY e.competencia.descricao ");
		query.setParameter("pessoa", pessoa);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<AtestoPessoa> findByPessoaCompetencia(Long idPessoa, Long idCompetencia) {
		Query query = entityManager.createQuery("SELECT e FROM AtestoPessoa e join fetch e.competencia WHERE e.pessoal.id = :pessoa AND e.competencia.id = :competencia ");
		query.setParameter("pessoa", idPessoa);
		query.setParameter("competencia", idCompetencia);
		return query.getResultList();
	}


	@Override
	public AtestoPessoa getByPessoalCompetencia(Long pessoal, Long competencia) {
		try {
			Query query = entityManager.createQuery("Select e from AtestoPessoa e join fetch e.competencia where e.pessoal.id = :pessoal AND e.competencia.id = :competencia");
			query.setParameter("pessoal", pessoal);
			query.setParameter("competencia", competencia);
			return (AtestoPessoa) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
}