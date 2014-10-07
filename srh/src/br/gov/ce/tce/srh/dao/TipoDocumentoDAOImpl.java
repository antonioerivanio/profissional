package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.TipoDocumento;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class TipoDocumentoDAOImpl implements TipoDocumentoDAO {

	static Logger logger = Logger.getLogger(TipoDocumentoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(t.id) from TipoDocumento t ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public TipoDocumento salvar(TipoDocumento entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(TipoDocumento entidade) {
		entityManager.createQuery("DELETE FROM TipoDocumento t WHERE t.id=:id").setParameter("id", entidade.getId()).executeUpdate();			
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count (t) from TipoDocumento t where upper( t.descricao ) LIKE :descricao");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDocumento> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select t from TipoDocumento t where upper( t.descricao ) LIKE :descricao ORDER BY t.descricao");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public TipoDocumento getByDescricao(String descricao) {
		try {
			Query query = entityManager.createQuery("Select t from TipoDocumento t where upper( t.descricao ) = :descricao ");
			query.setParameter("descricao", descricao.toUpperCase() );
			return (TipoDocumento) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDocumento> findByEsfera(Long esfera) {
		Query query = entityManager.createQuery("Select t from TipoDocumento t where t.esfera = :esfera ORDER BY t.descricao");
		query.setParameter("esfera", esfera);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDocumento> findByDocFuncional(boolean docFuncional) {
		Query query = entityManager.createQuery("Select t from TipoDocumento t where t.documentoFuncional = :doc ORDER BY t.descricao");
		query.setParameter("doc", docFuncional);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<TipoDocumento> findAll() {
		return entityManager.createQuery("SELECT t FROM TipoDocumento t ORDER BY t.descricao").getResultList();
	}

}
