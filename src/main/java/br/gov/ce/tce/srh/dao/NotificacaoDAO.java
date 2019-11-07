package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Notificacao;

/**
 * 
 * @author esmayk.alves@tce.ce.gov.br
 *
 */
@Repository
public class NotificacaoDAO {
	
	static Logger logger = Logger.getLogger(NotificacaoDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	/*
	 * private Long getMaxId() { Query query =
	 * entityManager.createQuery("Select max(e.id) from Notificacao e "); return
	 * query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1; }
	 */
	
	public Notificacao salvar(Notificacao entidade) {

		/*
		 * if (entidade.getId() == null || entidade.getId().equals(0l)) {
		 * entidade.setId(getMaxId()); }
		 */

		return entityManager.merge(entidade);
	}
	
	public void excluir(Notificacao entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	@SuppressWarnings("unchecked")
	public List<Notificacao> findAll() {
		Query query = entityManager.createQuery("SELECT n FROM Notificacao n");
		return query.getResultList();
	}
	
	public Notificacao getById(Long id) {
		return entityManager.find(Notificacao.class, id);
	}
	
	public Notificacao findByEventoIdAndTipo(long idEvento) {
		Query query = entityManager.createQuery("SELECT n FROM Notificacao n WHERE n.evento.id = :idEvento AND n.tipo = N");
		query.setParameter("idEvento", idEvento);
		try {
			return (Notificacao) query.getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Notificacao> search(String descricao, Integer first, Integer rows) {

		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT c FROM Notificacao c ");

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
