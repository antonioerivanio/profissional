package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CompetenciaCurso;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class CompetenciaCursoDAOImpl implements CompetenciaCursoDAO {

	static Logger logger = Logger.getLogger(CompetenciaCursoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public CompetenciaCurso getById(Long id) {
		return entityManager.find(CompetenciaCurso.class, id);
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(c.id) from CompetenciaCurso c ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public void salvar(CompetenciaCurso entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		entityManager.merge(entidade);
		entityManager.flush();
	}


	@Override
	public void excluir(Long curso) {
		Query query = entityManager.createQuery("DELETE FROM CompetenciaCurso r WHERE r.cursoProfissional.id=:id ");
		query.setParameter("id", curso);
		query.executeUpdate();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CompetenciaCurso> findByCurso(Long curso) {
		Query query = entityManager.createQuery("Select e from CompetenciaCurso e where e.cursoProfissional.id = :curso ORDER BY e.id ");
		query.setParameter("curso", curso);
		return query.getResultList();
	}


	@Override
	public CompetenciaCurso getByPessoalCompetencia(Long pessoal, Long competencia) {
		try {
			String sql = "";
			sql += "SELECT DISTINCT competenciaCurso ";
			sql += "  FROM PessoalCursoProfissional pcp ";
			sql += " JOIN pcp.cursoProfissional.competencias competenciaCurso ";
			sql += "WHERE pcp.pessoal.id = :idPessoal ";
			sql += "  AND competenciaCurso.competencia.id = :idCompetencia ";
			TypedQuery<CompetenciaCurso> query = entityManager.createQuery(sql, CompetenciaCurso.class);
			query.setParameter("idPessoal", pessoal);
			query.setParameter("idCompetencia", competencia);
			//Query query = entityManager.createQuery("Select e from  CompetenciaCurso e where e.cursoProfissional.pessoal.id = :pessoal AND e.competencia.id = :competencia");
			//query.setParameter("pessoal", pessoal);
			//query.setParameter("competencia", competencia);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
}
