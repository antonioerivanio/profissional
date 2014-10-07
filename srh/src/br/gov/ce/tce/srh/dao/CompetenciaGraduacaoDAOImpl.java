package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CompetenciaGraduacao;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class CompetenciaGraduacaoDAOImpl implements CompetenciaGraduacaoDAO {

	static Logger logger = Logger.getLogger(CompetenciaGraduacaoDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public CompetenciaGraduacao getById(Long id) {
		return entityManager.find(CompetenciaGraduacao.class, id);
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(c.id) from CompetenciaGraduacao c ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public void salvar(CompetenciaGraduacao entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}
	
		entityManager.merge(entidade);
		entityManager.flush();
	}


	@Override
	public void excluir(Long pessoal, Long cursoAcademica) {
		Query query = entityManager.createQuery("DELETE FROM CompetenciaGraduacao r WHERE r.pessoal.id = :pessoa AND r.cursoAcademica.id = :curso ");
		query.setParameter("pessoa", pessoal );
		query.setParameter("curso", cursoAcademica );
		query.executeUpdate();
	}
	
	@Override
	public void excluir(Long pessoal, Long cursoAcademica, Long competencia) {
		Query query = entityManager.createQuery("DELETE FROM CompetenciaGraduacao r WHERE r.pessoal.id = :pessoa AND r.cursoAcademica.id = :curso and r.competencia.id = :competencia ");
		query.setParameter("pessoa", pessoal );
		query.setParameter("curso", cursoAcademica );
		query.setParameter("competencia", competencia);
		query.executeUpdate();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CompetenciaGraduacao> findByPessoaCurso(Long pessoa, Long curso) {
		Query query = entityManager.createQuery("Select e from CompetenciaGraduacao e where e.pessoal.id = :pessoa AND e.cursoAcademica.id = :curso ORDER BY e.id ");
		query.setParameter("pessoa", pessoa);
		query.setParameter("curso", curso);
		return query.getResultList();
	}


	@Override
	public CompetenciaGraduacao getByPessoalCompetencia(Long pessoal, Long competencia) {
		try {
			Query query = entityManager.createQuery("Select e from CompetenciaGraduacao e where e.pessoal.id= :pessoal AND e.competencia.id = :competencia");
			query.setParameter("pessoal", pessoal);
			query.setParameter("competencia", competencia);
			return (CompetenciaGraduacao) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
