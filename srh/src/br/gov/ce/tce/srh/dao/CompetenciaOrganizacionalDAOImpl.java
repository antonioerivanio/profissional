package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CompetenciaOrganizacional;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class CompetenciaOrganizacionalDAOImpl implements CompetenciaOrganizacionalDAO {

	static Logger logger = Logger.getLogger(CompetenciaOrganizacionalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery(" Select max(c.id) from CompetenciaOrganizacional c ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public CompetenciaOrganizacional salvar(CompetenciaOrganizacional entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(CompetenciaOrganizacional entidade) {
		entityManager.createQuery("DELETE FROM CompetenciaOrganizacional c WHERE c.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(String tipo) {
		String queryString;
		if (tipo.equals("0")) {
			queryString = "SELECT count (co) as total FROM CompetenciaOrganizacional co ";
		}else{
			queryString = "SELECT count (co) as total FROM CompetenciaOrganizacional co WHERE co.competencia.tipo = " + tipo;
		}
		Query query = entityManager.createQuery(queryString);

		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CompetenciaOrganizacional> search(String tipo, int first, int rows) {
		String queryString;
		if (tipo.equals("0")) {
			queryString = "SELECT co FROM CompetenciaOrganizacional co  ORDER BY co.inicio";
		}else{
			queryString = "SELECT co FROM CompetenciaOrganizacional co WHERE co.competencia.tipo = " + tipo + " ORDER BY co.inicio";
		}

		Query query = entityManager.createQuery(queryString);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

}
