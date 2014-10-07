package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Municipio;

/**
*
* @author robstown
* 
*/
@Repository
public class MunicipioDAOImpl implements MunicipioDAO {

	static Logger logger = Logger.getLogger(MunicipioDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Municipio> findByUF(String uf) {
		Query query = entityManager.createQuery("Select e from Municipio e where e.uf.id = :uf order by e.nome ");
		query.setParameter("uf", uf);
		return query.getResultList();
	}

}
