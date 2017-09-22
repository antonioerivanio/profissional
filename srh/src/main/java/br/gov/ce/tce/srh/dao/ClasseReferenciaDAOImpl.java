package br.gov.ce.tce.srh.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.ClasseReferencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class ClasseReferenciaDAOImpl implements ClasseReferenciaDAO {

	static Logger logger = Logger.getLogger(ClasseReferenciaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(c.id) from ClasseReferencia c ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public ClasseReferencia salvar(ClasseReferencia entidade) throws SRHRuntimeException {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(ClasseReferencia entidade) {
		entityManager.createQuery("DELETE FROM ClasseReferencia c WHERE c.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(Long idOcupacao) {
		Query query = entityManager.createQuery("Select count (c) from ClasseReferencia c where c.simbolo.ocupacao.id = :id ORDER BY c.referencia");
		query.setParameter("id", idOcupacao);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<ClasseReferencia> search(Long idOcupacao, int first, int rows) {
		Query query = entityManager.createQuery("Select c from ClasseReferencia c join fetch c.escolaridade join fetch c.simbolo where c.simbolo.ocupacao.id = :id ORDER BY c.referencia ");
		query.setParameter("id", idOcupacao);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public ClasseReferencia getBySimboloReferencia(Long simbolo, Long referencia) {
		try {
			Query query = entityManager.createQuery("Select c from ClasseReferencia c join fetch c.escolaridade join fetch c.simbolo where c.simbolo.id = :simbolo and c.referencia = :referencia ");
			query.setParameter("simbolo", simbolo );
			query.setParameter("referencia", referencia );
			return (ClasseReferencia) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<ClasseReferencia> findByCargo(Long cargo) {
		Query query = entityManager.createQuery("Select c from ClasseReferencia c join fetch c.escolaridade join fetch c.simbolo where c.simbolo.ocupacao.id = :cargo ORDER BY c.referencia ");
		query.setParameter("cargo", cargo);
		return query.getResultList();
	}

}
