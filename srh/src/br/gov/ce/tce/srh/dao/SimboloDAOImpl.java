package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Simbolo;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class SimboloDAOImpl implements SimboloDAO {

	static Logger logger = Logger.getLogger(SimboloDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(s.id) from Simbolo s ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Simbolo salvar(Simbolo entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(Simbolo entidade) {
		entityManager.createQuery("DELETE FROM Simbolo s WHERE s.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public void excluirAll(Long ocupacao) {
		entityManager.createQuery("DELETE FROM Simbolo s WHERE s.ocupacao.id=:id").setParameter("id", ocupacao).executeUpdate();
	}


	@Override
	public Simbolo getByOcupacaoSimbolo(Long ocupacao, String simbolo) {
		try {
			Query query = entityManager.createQuery("Select s from Simbolo s join fetch s.ocupacao where s.ocupacao.id = :ocupacao and upper( s.simbolo ) = :simbolo ");
			query.setParameter("ocupacao", ocupacao );
			query.setParameter("simbolo", simbolo.toUpperCase() );
			return (Simbolo) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Simbolo> findByOcupacao(Long ocupacao) {
		Query query = entityManager.createQuery("Select s from Simbolo s join fetch s.ocupacao where s.ocupacao.id = :ocupacao ");
		query.setParameter("ocupacao", ocupacao );
		return query.getResultList();
	}

}
