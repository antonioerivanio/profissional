package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.GradeHorario;

@Repository
public class GradeHorarioDAO {
	
	static Logger logger = Logger.getLogger(GradeHorarioDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from GradeHorario e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	@Transactional
	public GradeHorario salvar(GradeHorario entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	@Transactional
	public void excluir(GradeHorario entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public GradeHorario findById(Long id) {
		return entityManager.find(GradeHorario.class, id);
	}
	
	public List<GradeHorario> findAll(){
		TypedQuery<GradeHorario> query = 
				entityManager.createQuery("Select e from GradeHorario e left join fetch e.esocialVigencia v order by v.inicioValidade, e.id", GradeHorario.class);
		return query.getResultList(); 
	}

}
