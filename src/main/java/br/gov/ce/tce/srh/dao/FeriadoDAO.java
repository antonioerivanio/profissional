package br.gov.ce.tce.srh.dao;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Feriado;

@Repository
public class FeriadoDAO {

	static Logger logger = Logger.getLogger(FeriadoDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Feriado findByData(Date data) {

		try {
			return (Feriado) entityManager.createQuery("SELECT f FROM Feriado f WHERE f.dia = :data")
					.setParameter("data", data).setMaxResults(1).getSingleResult();
		}catch (NoResultException e){
			return null;
		}
		
	}

}
