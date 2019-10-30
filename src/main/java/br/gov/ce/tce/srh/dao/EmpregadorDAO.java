package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.Empregador;

@Repository
public class EmpregadorDAO {
	
	static Logger logger = Logger.getLogger(EmpregadorDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from Empregador e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	@Transactional
	public Empregador salvar(Empregador entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	@Transactional
	public void excluir(Empregador entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public Empregador findById(Long id) {
		return entityManager.find(Empregador.class, id);
	}
	
	public List<Empregador> findAll(){
		TypedQuery<Empregador> query = 
				entityManager.createQuery("Select e from Empregador e left join fetch e.esocialVigencia v order by v.inicioValidade, e.id", Empregador.class);
		return query.getResultList(); 
	}

}
