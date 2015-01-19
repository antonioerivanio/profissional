package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Dependente;

@Repository
public class DependenteDAOImpl implements DependenteDAO{

	static Logger logger = Logger.getLogger(CategoriaFuncionalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	private Long getMaxId() {
		Query query = entityManager
				.createQuery("SELECT MAX(d.id) FROM Dependente d");
		return query.getSingleResult() == null ? 1 : (Long) query
				.getSingleResult() + 1;
	}
	
	
	@Override
	public Dependente salvar(Dependente entidade) {
		if (entidade.getId() == null || entidade.getId() == 0) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	@Override
	public void excluir(Dependente entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dependente> findAll() {
		return entityManager.createNamedQuery("Dependente.findAll").getResultList();
	}

	@Override
	public Dependente getById(Long id) {
		return entityManager.find(Dependente.class, id);
	}

	@Override
	public int count(Long idPessoal) {
		
		Query query;
		
		if(idPessoal != null && idPessoal != 0){
			query = entityManager.createQuery("SELECT COUNT(d) FROM Dependente d WHERE d.responsavel.id = :idPessoal");
			query.setParameter("idPessoal", idPessoal);
		}else{
			query = entityManager.createQuery("SELECT COUNT(d) FROM Dependente d");
		}		
		
		return ((Long)query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Dependente> search(Long idPessoal, int first, int rows) {
		
		Query query;
		
		if(idPessoal != null && idPessoal != 0){
			query = entityManager.createQuery("SELECT d FROM Dependente d WHERE d.responsavel.id = :idPessoal ORDER BY d.dependente.nomeCompleto");
			query.setParameter("idPessoal", idPessoal);
		}else{
			query = entityManager.createQuery("SELECT d FROM Dependente d ORDER BY d.dependente.nomeCompleto");
		}
		
		query.setFirstResult(first);
		query.setMaxResults(rows);
		
		return query.getResultList();
	}


	@Override
	public Dependente findByResponsavelAndDependente(Long idResponsavel,Long idDependente) {
		
		try{		
			Query query = entityManager.createQuery("SELECT d FROM Dependente d WHERE d.responsavel.id = :idResponsavel AND d.dependente.id = :idDependente");
			
			query.setParameter("idResponsavel", idResponsavel);
			query.setParameter("idDependente", idDependente);
			
			return (Dependente) query.getSingleResult();
		}catch (Exception e){
			return null;
		}
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<Dependente> findByResponsavel(Long idResponsavel) {
					
		Query query = entityManager.createQuery("SELECT d FROM Dependente d WHERE d.responsavel.id = :idResponsavel ORDER BY d.dependente.nomeCompleto");
		query.setParameter("idResponsavel", idResponsavel);				
		
		return query.getResultList();
	}

}
