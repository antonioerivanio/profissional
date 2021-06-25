package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Folga;

@Repository
public class FolgaDAO {
	
	static Logger logger = Logger.getLogger(FolgaDAO.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}
	
	private Long getMaxId() {		
		Query query = entityManager.createQuery("SELECT MAX(f.id) FROM Folga f ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}
	
	public Folga salvar(Folga entidade) {
		
		if (entidade.getId() == null || entidade.getId() == 0){
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(Folga entidade) {		
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);		
	}

	public int count(Long idPessoal) {
		Query query = entityManager
				.createQuery("SELECT count(f) FROM Folga f WHERE f.pessoal.id = :idPessoal order by f.inicio desc");
		query.setParameter("idPessoal", idPessoal);
		return ((Long) query.getSingleResult()).intValue();
	}
	
	public Double saldoTotal(Long idPessoal) {
		Query query = entityManager
				.createQuery("SELECT sum(f.saldoFinal) FROM Folga f WHERE f.pessoal.id = :idPessoal");
		query.setParameter("idPessoal", idPessoal);
		return (Double) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Folga> search(Long idPessoal, Integer first, Integer rows) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT f FROM Folga f ");
		sql.append(" WHERE f.pessoal.id = :idPessoal ");
		sql.append(" ORDER BY f.inicio DESC ");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("idPessoal", idPessoal);
		
		if(first != null) {			
			query.setFirstResult(first);
		}
		
		if(rows != null) {			
			query.setMaxResults(rows);
		}
		
		return query.getResultList();
	}
	
	public Folga findById(Long id) {
		return entityManager.find(Folga.class, id);
	}	

}
