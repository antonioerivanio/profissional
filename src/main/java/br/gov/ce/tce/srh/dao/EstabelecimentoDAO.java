package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Estabelecimento;

@Repository
public class EstabelecimentoDAO {

	static Logger logger = Logger.getLogger(EstabelecimentoDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}	

	public Estabelecimento getById(Long id) {
		return entityManager.find(Estabelecimento.class, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<Estabelecimento> findAll() {
		Query query = entityManager.createQuery("SELECT e FROM Estabelecimento e ORDER BY e.tipoInscricao, e.numeroInscricao");
		return query.getResultList();
	}

}
