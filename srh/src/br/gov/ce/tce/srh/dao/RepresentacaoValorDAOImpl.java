package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.RepresentacaoValor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class RepresentacaoValorDAOImpl implements RepresentacaoValorDAO {

	static Logger logger = Logger.getLogger(RepresentacaoValorDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(r.id) from RepresentacaoValor r ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public RepresentacaoValor salvar(RepresentacaoValor entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		// verificando se existe representacao ja cadastrada
		RepresentacaoValor entidadeJaExiste = getByCargo( entidade.getRepresentacaoCargo().getId() );
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId())) {
			throw new SRHRuntimeException("Cargo de Representação do Valor já cadastrado. Operação cancelada.");
		}

		return entityManager.merge(entidade);
	}


	public RepresentacaoValor getByCargo( Long cargo ) {
		try {
			Query query = entityManager.createQuery("SELECT e FROM RepresentacaoValor e join fetch e.representacaoCargo WHERE e.representacaoCargo.id = :cargo AND e.fim IS NULL ");
			query.setParameter("cargo", cargo );
			return (RepresentacaoValor) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	public void excluir(RepresentacaoValor entidade) {
		entityManager.createQuery("DELETE FROM RepresentacaoValor r WHERE r.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(Long cargo) {
		Query query = entityManager.createQuery("SELECT count (e) FROM RepresentacaoValor e WHERE e.representacaoCargo.id = :cargo ORDER BY e.id ");
		query.setParameter("cargo", cargo);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<RepresentacaoValor> search(Long cargo, int first, int rows) {
		Query query = entityManager.createQuery("SELECT e FROM RepresentacaoValor e join fetch e.representacaoCargo WHERE e.representacaoCargo.id = :cargo ORDER BY e.id ");
		query.setParameter("cargo", cargo);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

}
