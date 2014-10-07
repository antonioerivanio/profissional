package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Publicacao;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class PublicacaoDAOImpl implements PublicacaoDAO {

	static Logger logger = Logger.getLogger(PublicacaoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(r.id) from Publicacao r ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Publicacao salvar(Publicacao entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(Publicacao entidade) {
		entityManager.createQuery("DELETE FROM Publicacao r WHERE r.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(Long tipoDocumento) {
		Query query = entityManager.createQuery("SELECT count (e) FROM Publicacao e WHERE e.tipoDocumento.id = :tipoDocumento ORDER BY e.id ");
		query.setParameter("tipoDocumento", tipoDocumento);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Publicacao> search(Long tipoDocumento, int first, int rows) {
		Query query = entityManager.createQuery("SELECT e FROM Publicacao e join fetch e.tipoDocumento WHERE e.tipoDocumento.id = :tipoDocumento ORDER BY e.id ");
		query.setParameter("tipoDocumento", tipoDocumento);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public Publicacao getByEmenta( String ementa ) {
		try {
			Query query = entityManager.createQuery("SELECT e FROM Publicacao e join fetch e.tipoDocumento WHERE upper( e.ementa ) = :ementa ");
			query.setParameter("ementa", ementa );
			return (Publicacao) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
