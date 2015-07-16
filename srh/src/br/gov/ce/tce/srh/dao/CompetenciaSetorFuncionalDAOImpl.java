package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CategoriaFuncional;
import br.gov.ce.tce.srh.domain.CompetenciaSetorFuncional;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

/**
 *
 * @author raphael.ferreira
 * 
 */
@Repository
public class CompetenciaSetorFuncionalDAOImpl implements CompetenciaSetorFuncionalDAO {

	static Logger logger = Logger.getLogger(CompetenciaSetorFuncionalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery(" Select max(c.id) from CompetenciaSetorFuncional c ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public CompetenciaSetorFuncional salvar(CompetenciaSetorFuncional entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(CompetenciaSetorFuncional entidade) {
		entityManager.createQuery("DELETE FROM CompetenciaSetorFuncional c WHERE c.id=:id").setParameter("id", entidade.getId()).executeUpdate();
	}


	@Override
	public int count(Setor setor, String tipo, CategoriaFuncional categoriaFuncional) {
		String queryString = "SELECT count (cs) as total FROM CompetenciaSetorFuncional cs WHERE 1=1";
		if (!tipo.equals("0")) {
			queryString = queryString + " and cs.competencia.tipo = " + tipo;
		}
		if (setor != null) {
			queryString = queryString + " and cs.categoria.setor.id = " + setor.getId();
		}

		if (categoriaFuncional != null) {
			queryString = queryString + " and cs.categoria.categoriaFuncional.id = " + categoriaFuncional.getId();
		}
		
		Query query = entityManager.createQuery(queryString);

		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CompetenciaSetorFuncional> search(Setor setor, String tipo, CategoriaFuncional categoriaFuncional, int first, int rows) {
		String queryString = "SELECT cs FROM CompetenciaSetorFuncional cs WHERE 1=1"; 
		if (!tipo.equals("0")) {
			queryString = queryString + " and cs.competencia.tipo = " + tipo;
		}
		if (setor != null) {
			queryString = queryString + " and cs.categoria.setor.id = " + setor.getId();
		}
		
		if (categoriaFuncional != null) {
			queryString = queryString + " and cs.categoria.categoriaFuncional.id = " + categoriaFuncional.getId();
		}

		queryString = queryString + " ORDER BY cs.inicio";
		Query query = entityManager.createQuery(queryString);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

}
