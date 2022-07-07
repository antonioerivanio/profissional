package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CodigoCategoria;


/**
 * @author robson.castro
 *
 */
@Repository
public class CodigoCategoriaDAOImpl implements CodigoCategoriaDAO {

	static Logger logger = Logger.getLogger(CodigoCategoriaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public CodigoCategoria getById(Long id) {
		return entityManager.find(CodigoCategoria.class, id);
	}


	@Override
	public CodigoCategoria getByCodigo(Long codigo) {
		try {
			Query query = entityManager.createQuery("Select c from CodigoCategoria c where c.codigo = :codigo");
			query.setParameter("codigo", codigo);
			return (CodigoCategoria) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CodigoCategoria> findAll() {
		return entityManager.createQuery("SELECT c FROM CodigoCategoria c ORDER BY c.id").getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CodigoCategoria> findByCodigo(Long codigo) {
		Query query = entityManager.createQuery("Select c from CodigoCategoria c where c.nivel = :nivel AND c.codigo like :codigo order by c.codigo");
		query.setParameter("codigo", codigo + "%");
		return query.getResultList();
	}

}
