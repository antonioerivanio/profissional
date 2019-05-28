package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Carreira;

@Repository
public class CarreiraDAO {

	static Logger logger = Logger.getLogger(CarreiraDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from Carreira e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public Carreira salvar(Carreira entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(Carreira entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public Carreira getById(Long id) {
		return entityManager.find(Carreira.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Carreira> search(String descricao, Integer first, Integer rows) {

		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT c FROM Carreira c ");

		if (descricao != null && !descricao.isEmpty()) {
			sql.append(" WHERE upper( c.descricao ) like :descricao ");
		}

		sql.append(" ORDER BY c.descricao ");

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
