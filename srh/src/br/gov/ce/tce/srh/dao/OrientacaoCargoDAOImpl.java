package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Especialidade;
import br.gov.ce.tce.srh.domain.OrientacaoCargo;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class OrientacaoCargoDAOImpl implements OrientacaoCargoDAO {

	static Logger logger = Logger.getLogger(OrientacaoCargoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(o.id) from OrientacaoCargo o ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public int count(String descricao) {
		Query query = entityManager.createQuery("Select count (o) from OrientacaoCargo o where upper( o.descricao ) LIKE :descricao ORDER BY o.descricao");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<OrientacaoCargo> search(String descricao, int first, int rows) {
		Query query = entityManager.createQuery("Select o from OrientacaoCargo o where upper( o.descricao ) LIKE :descricao ORDER BY o.descricao");
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	
	@Override
	public OrientacaoCargo salvar(OrientacaoCargo entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	
	@Override
	public void excluir(OrientacaoCargo entidade) {
		Query query = entityManager.createQuery("DELETE FROM OrientacaoCargo e WHERE e.id=:id");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Especialidade> findAll() {
		return entityManager.createQuery("SELECT e FROM Especialidade e ORDER BY e.id").getResultList();
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<OrientacaoCargo> findByEspecialidade(Long especialidade) {
		Query query = entityManager.createQuery("Select o from OrientacaoCargo o join fetch o.especialidade where o.especialidade.id = :especialidade");
		query.setParameter("especialidade", especialidade);
		return query.getResultList();
	}

}
