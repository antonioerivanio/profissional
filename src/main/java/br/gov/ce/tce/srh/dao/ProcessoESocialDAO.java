package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.ProcessoESocial;

@Repository
public class ProcessoESocialDAO {

	static Logger logger = Logger.getLogger(ProcessoESocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from ProcessoESocial e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public ProcessoESocial salvar(ProcessoESocial entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(ProcessoESocial entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public ProcessoESocial getById(Long id) {
		return entityManager.find(ProcessoESocial.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<ProcessoESocial> search(String numero, Integer first, Integer rows) {

		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT p FROM ProcessoESocial p ");

		if (numero != null && !numero.isEmpty()) {
			sql.append(" WHERE upper( p.numero ) like :numero ");
		}

		sql.append(" ORDER BY p.numero ");

		Query query = entityManager.createQuery(sql.toString());

		if (numero != null && !numero.isEmpty()) {
			query.setParameter("numero", "%" + numero.toUpperCase() + "%");
		}

		if (first != null && first >= 0)
			query.setFirstResult(first);
		if (rows != null && rows > 0)
			query.setMaxResults(rows);

		return query.getResultList();
	}

}
