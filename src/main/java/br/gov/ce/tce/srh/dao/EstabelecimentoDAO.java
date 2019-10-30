package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.Estabelecimento;

@Repository
public class EstabelecimentoDAO {

	static Logger logger = Logger.getLogger(EstabelecimentoDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from Estabelecimento e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	@Transactional
	public Estabelecimento salvar(Estabelecimento entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	@Transactional
	public void excluir(Estabelecimento entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public Estabelecimento findById(Long id) {
		return entityManager.find(Estabelecimento.class, id);
	}
	
	public List<Estabelecimento> findAll() {
		TypedQuery<Estabelecimento> query = 
				entityManager.createQuery("SELECT e FROM Estabelecimento e left join fetch e.esocialVigencia v "
						+ "ORDER BY v.inicioValidade, e.id", Estabelecimento.class);
		return query.getResultList();
	}

}
