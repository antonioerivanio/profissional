package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Cbo;


/**
 * @author robson.castro
 *
 */
@Repository
public class CboDAOImpl implements CboDAO {

	static Logger logger = Logger.getLogger(CboDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public Cbo getById(Long id) {
		return entityManager.find(Cbo.class, id);
	}


	@Override
	public Cbo getByCodigo(String codigo) {
		try {
			Query query = entityManager.createQuery("Select c from Cbo c where c.codigo = :codigo");
			query.setParameter("codigo", codigo);
			return (Cbo) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Cbo> findAll() {
		return entityManager.createQuery("SELECT c FROM Cbo c ORDER BY c.id").getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Cbo> findByNivel(Long nivel) {
		Query query = entityManager.createQuery("Select c from Cbo c where c.nivel = :nivel order by c.codigo");
		query.setParameter("nivel", nivel);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Cbo> findByNivelCodigo(Long nivel, String codigo) {
		Query query = entityManager.createQuery("Select c from Cbo c where c.nivel = :nivel AND c.codigo like :codigo order by c.codigo");
		query.setParameter("nivel", nivel);
		query.setParameter("codigo", codigo + "%");
		return query.getResultList();
	}

}
