package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.FuncionalCedido;


/**
 * @author robson.castro
 *
 */
@Repository
public class FuncionalCedidoDAOImpl implements FuncionalCedidoDAO {

	static Logger logger = Logger.getLogger(FuncionalCedidoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(v.id) FROM VinculoRGPS  v");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public FuncionalCedido salvar(FuncionalCedido entidade) {
		
		if (entidade.getId() == null || entidade.getId() == 0){
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	@Override
	public FuncionalCedido getById(Long id) {
		return entityManager.find(FuncionalCedido.class, id);
	}


	@Override
	public FuncionalCedido getByCodigo(Long codigo) {
		try {
			Query query = entityManager.createQuery("Select c from FuncionalCedido c where c.codigo = :codigo");
			query.setParameter("codigo", codigo);
			return (FuncionalCedido) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<FuncionalCedido> findAll() {
		return entityManager.createQuery("SELECT c FROM FuncionalCedido c ORDER BY c.id").getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<FuncionalCedido> findByCodigo(Long codigo) {
		Query query = entityManager.createQuery("Select c from FuncionalCedido c where c.nivel = :nivel AND c.codigo like :codigo order by c.codigo");
		query.setParameter("codigo", codigo + "%");
		return query.getResultList();
	}

}
