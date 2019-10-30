package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.LotacaoTributaria;

@Repository
public class LotacaoTributariaDAO {
	
	static Logger logger = Logger.getLogger(LotacaoTributariaDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from LotacaoTributaria e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	@Transactional
	public LotacaoTributaria salvar(LotacaoTributaria entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	@Transactional
	public void excluir(LotacaoTributaria entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public LotacaoTributaria findById(Long id) {
		return entityManager.find(LotacaoTributaria.class, id);
	}
	
	public List<LotacaoTributaria> findAll(){
		TypedQuery<LotacaoTributaria> query = 
				entityManager.createQuery("Select e from LotacaoTributaria e left join fetch e.esocialVigencia v order by v.inicioValidade, e.id", LotacaoTributaria.class);
		return query.getResultList(); 
	}

}
