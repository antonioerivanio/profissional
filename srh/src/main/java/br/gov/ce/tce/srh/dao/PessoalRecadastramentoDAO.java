package br.gov.ce.tce.srh.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.PessoalRecadastramento;

@Repository
public class PessoalRecadastramentoDAO {

	static Logger logger = Logger.getLogger(PessoalRecadastramentoDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public PessoalRecadastramento salvar(PessoalRecadastramento entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}		
		
		return entityManager.merge(entidade);
	}
	
	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(r.id) from PessoalRecadastramento r ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}
	
	public PessoalRecadastramento findById(Long id) {
		return entityManager.find(PessoalRecadastramento.class, id);
	}
	
	public PessoalRecadastramento findByPessoalAndRecadastramento(Long idPessoal, Long idRecadastramento) {
		
		try {
			
			Query query = entityManager.createQuery("SELECT r FROM PessoalRecadastramento r join fetch r.pessoal WHERE r.pessoal.id = :idPessoal "
					+ " and r.id = :idRecadastramento");
			query.setParameter("idPessoal", idPessoal);
			query.setParameter("idRecadastramento", idRecadastramento);
			return (PessoalRecadastramento) query.getSingleResult();
		
		} catch (Exception e) {
			return null;
		}	
	}
	
	
	

}
