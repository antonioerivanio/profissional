package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Recadastramento;

@Repository
public class RecadastramentoDAO {

	static Logger logger = Logger.getLogger(RecadastramentoDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public Recadastramento findById(Long id) {
		return entityManager.find(Recadastramento.class, id);
	}

	@SuppressWarnings("unchecked")
	public Recadastramento findMaisRecente() {
		List<Recadastramento> recadastramentos = entityManager.createQuery("SELECT r FROM Recadastramento r ORDER BY r.inicio DESC").getResultList(); 
		if (!recadastramentos.isEmpty())
			return recadastramentos.get(0);
		
		return null;
	}	
	

}
