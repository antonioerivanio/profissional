package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Municipio;

@Repository
public class MunicipioDAO {

	static Logger logger = Logger.getLogger(MunicipioDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@SuppressWarnings("unchecked")
	public List<Municipio> findByUF(String uf) {
		Query query = entityManager.createQuery("Select e from Municipio e where e.uf.id = :uf order by e.nome ");
		query.setParameter("uf", uf);
		return query.getResultList();
	}
	
	
	public Municipio findByCodigoIBGE(String codigoIBGE) {
		Query query = entityManager.createQuery("Select e from Municipio e where e.codigoIBGE = :codigoIBGE ");
		query.setParameter("codigoIBGE", codigoIBGE);
		return (Municipio) query.getSingleResult();
	}

}
