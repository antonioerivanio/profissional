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
		Query query = entityManager.createQuery("Select count(c) from CarreiraPessoal c where c.pessoal.id = :pessoal");
		query.setParameter("pessoal", pessoal);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CarreiraPessoal> search(Long pessoal, int first, int rows) {
		Query query = entityManager.createQuery("Select c from CarreiraPessoal c where c.pessoal.id = :pessoal ORDER BY c.inicioCarreira DESC");
		query.setParameter("pessoal", pessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CarreiraPessoal> findByPessoal(Long idPessoa) {
		Query query = entityManager.createQuery("Select c from CarreiraPessoal c where c.pessoal.id = :pessoal ORDER BY c.inicioCarreira DESC");
		query.setParameter("pessoal", idPessoa);
		return query.getResultList();
	}

}