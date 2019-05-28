package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.RepresentacaoSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class RepresentacaoSetorDAOImpl implements RepresentacaoSetorDAO {

	static Logger logger = Logger.getLogger(RepresentacaoSetorDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(r.id) from RepresentacaoSetor r ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public RepresentacaoSetor salvar(RepresentacaoSetor entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		// verificando se existe representacao ja cadastrada
		RepresentacaoSetor entidadeJaExiste = getByCargoSetor( entidade.getRepresentacaoCargo().getId(), entidade.getSetor().getId() );

		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId())) {
			throw new SRHRuntimeException("Cargo de Representação do Setor já cadastrado. Operação cancelada.");
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(RepresentacaoSetor entidade) {
		entityManager.createQuery("DELETE FROM RepresentacaoSetor r WHERE r.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(Long cargo) {
		Query query = entityManager.createQuery("SELECT count (e) FROM RepresentacaoSetor e WHERE e.representacaoCargo.id = :cargo ORDER BY e.id ");
		query.setParameter("cargo", cargo);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<RepresentacaoSetor> search(Long cargo, int first, int rows) {
		Query query = entityManager.createQuery("SELECT e FROM RepresentacaoSetor e join fetch e.setor join fetch e.representacaoCargo WHERE e.representacaoCargo.id = :cargo ORDER BY e.id ");
		query.setParameter("cargo", cargo);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public RepresentacaoSetor getByCargoSetor(Long cargo, Long setor) {
		try {
			Query query = entityManager.createQuery("SELECT e FROM RepresentacaoSetor e join fetch e.setor join fetch e.representacaoCargo WHERE e.representacaoCargo.id = :cargo AND e.setor.id = :setor AND e.ativo = true ");
			query.setParameter("cargo", cargo );
			query.setParameter("setor", setor );
			return (RepresentacaoSetor) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<RepresentacaoSetor> findBySetorAtivo(Long setor, boolean ativo) {
		Query query = entityManager.createQuery("SELECT e FROM RepresentacaoSetor e join fetch e.setor s join fetch e.representacaoCargo r WHERE s.id = :setor AND e.ativo = :ativo ORDER BY r.nomenclatura ");
		query.setParameter("setor", setor);
		query.setParameter("ativo", ativo);
		return query.getResultList();
	}

}
