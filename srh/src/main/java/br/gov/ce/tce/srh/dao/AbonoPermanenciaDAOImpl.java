package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.AbonoPermanencia;

@Repository
public class AbonoPermanenciaDAOImpl implements AbonoPermanenciaDAO{

	static Logger logger = Logger.getLogger(AbonoPermanenciaDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(a.id) FROM AbonoPermanencia a");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}
	
	
	@Override
	public AbonoPermanencia salvar(AbonoPermanencia entidade) {
		if (entidade.getId() == null || entidade.getId() == 0) {
			entidade.setId(getMaxId());
		}
		return entityManager.merge(entidade);
	}

	@Override
	public void excluir(AbonoPermanencia entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<AbonoPermanencia> findAll() {
		return entityManager.createNamedQuery("AbonoPermanencia.findAll").getResultList();
	}

	@Override
	public AbonoPermanencia getById(Long id) {
		return entityManager.find(AbonoPermanencia.class, id);
	}
	
	@Override
	public AbonoPermanencia getByPessoalId(Long pessoalId) {
		
		try {
			
			Query query = entityManager.createQuery("SELECT a FROM AbonoPermanencia a JOIN FETCH a.funcional f JOIN FETCH f.pessoal p WHERE p.id = :pessoalId ORDER BY f.nomeCompleto");		
			query.setParameter("pessoalId", pessoalId);
			return (AbonoPermanencia) query.getSingleResult();
		
		}catch (NoResultException e){
			return null;
		}
	}	

	@SuppressWarnings("unchecked")
	@Override
	public List<AbonoPermanencia> search( String cpf,int first, int rows) {		
		Query query;
		if( cpf != null && !cpf.isEmpty() ) {
			query = entityManager.createQuery("SELECT a FROM AbonoPermanencia a JOIN FETCH a.funcional f JOIN FETCH f.pessoal p WHERE p.cpf = :cpf");
			query.setParameter("cpf", cpf);
		} else {
			query = entityManager.createQuery("SELECT a FROM AbonoPermanencia a ORDER BY a.funcional.nomeCompleto");
		}
		query.setFirstResult(first);
		query.setMaxResults(rows);		
		return query.getResultList();
	}
	

}
