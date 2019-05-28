package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CarreiraPessoal;

@Repository
public class CarreiraPessoalDAOImpl implements CarreiraPessoalDAO {

	static Logger logger = Logger.getLogger(CarreiraPessoalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT max(c.id) FROM CarreiraPessoal c");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public CarreiraPessoal salvar(CarreiraPessoal entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)){
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(CarreiraPessoal entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}


	@Override
	public int count(Long pessoal) {
		
		String consulta = " Select count(c) from CarreiraPessoal c ";
		
		if (pessoal != null)
			consulta += " where c.pessoal.id = :pessoal ";		
		
		Query query = entityManager.createQuery(consulta);
		
		if (pessoal != null)
			query.setParameter("pessoal", pessoal);
		
		
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CarreiraPessoal> search(Long pessoal, Integer first, Integer rows) {
		
		String consulta = " Select c from CarreiraPessoal c ";
		
		if(pessoal != null)
			consulta += " where c.pessoal.id = :pessoal ";
		
		consulta += " ORDER BY c.pessoal.nomeCompleto, c.inicioCarreira DESC ";
		
		Query query = entityManager.createQuery(consulta);
		
		if (pessoal != null)		
			query.setParameter("pessoal", pessoal);
		
		if(first != null)
			query.setFirstResult(first);
		
		if(rows != null)
			query.setMaxResults(rows);
		
		return query.getResultList();
	}
	

}