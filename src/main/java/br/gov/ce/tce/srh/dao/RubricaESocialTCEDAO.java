package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.RubricaESocialTCE;

@Repository
public class RubricaESocialTCEDAO {

	static Logger logger = Logger.getLogger(RubricaESocialTCEDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from RubricaESocialTCE e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public RubricaESocialTCE salvar(RubricaESocialTCE entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(RubricaESocialTCE entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public RubricaESocialTCE getById(Long id) {
		return entityManager.find(RubricaESocialTCE.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<RubricaESocialTCE> search(String descricao, Integer first, Integer rows) {

		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT e FROM RubricaESocialTCE e inner join fetch e.rubrica r");

		if (descricao != null && !descricao.isEmpty()) {
			sql.append(" WHERE upper( e.descricao ) like :descricao ");
		}

		sql.append(" ORDER BY r.descricao ");

		Query query = entityManager.createQuery(sql.toString());

		if (descricao != null && !descricao.isEmpty()) {
			query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");
		}

		if (first != null && first >= 0)
			query.setFirstResult(first);
		if (rows != null && rows > 0)
			query.setMaxResults(rows);

		return query.getResultList();
	}

}
