package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.AmbienteTrabalho;

@Repository
public class AmbienteTrabalhoDAO {

	static Logger logger = Logger.getLogger(AmbienteTrabalhoDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from AmbienteTrabalho e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public AmbienteTrabalho salvar(AmbienteTrabalho entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(AmbienteTrabalho entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public AmbienteTrabalho getById(Long id) {
		return entityManager.find(AmbienteTrabalho.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<AmbienteTrabalho> search(String nome, Integer first, Integer rows) {

		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT a FROM AmbienteTrabalho a ");

		if (nome != null && !nome.isEmpty()) {
			sql.append(" WHERE upper( a.nome ) like :nome ");
		}

		sql.append(" ORDER BY a.nome ");

		Query query = entityManager.createQuery(sql.toString());

		if (nome != null && !nome.isEmpty()) {
			query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		}

		if (first != null && first >= 0)
			query.setFirstResult(first);
		if (rows != null && rows > 0)
			query.setMaxResults(rows);

		return query.getResultList();
	}

}
