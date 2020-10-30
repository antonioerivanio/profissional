package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.RubricaESocial;
import br.gov.ce.tce.srh.domain.TipoDeficiencia;

@Repository
public class TipoDeficienciaDAO {

	static Logger logger = Logger.getLogger(TipoDeficienciaDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}	

	public RubricaESocial getById(Long id) {
		return entityManager.find(RubricaESocial.class, id);
	}
	
	public List<TipoDeficiencia> findAll(){
		TypedQuery<TipoDeficiencia> query = entityManager.createQuery("Select e from TipoDeficiencia e order by e.id", TipoDeficiencia.class);
		return query.getResultList(); 
	}
}
