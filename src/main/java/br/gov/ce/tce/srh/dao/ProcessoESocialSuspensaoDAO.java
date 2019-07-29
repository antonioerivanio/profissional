package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.ProcessoESocialSuspensao;

@Repository
public class ProcessoESocialSuspensaoDAO {

	static Logger logger = Logger.getLogger(ProcessoESocialSuspensaoDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from ProcessoESocialSuspensao e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public ProcessoESocialSuspensao salvar(ProcessoESocialSuspensao entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(ProcessoESocialSuspensao entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public ProcessoESocialSuspensao getById(Long id) {
		return entityManager.find(ProcessoESocialSuspensao.class, id);
	}

	@SuppressWarnings("unchecked")
	public List<ProcessoESocialSuspensao> getSuspensoes(Long idProcesso) {
		return entityManager.createQuery("Select e from ProcessoESocialSuspensao e where e.processo = :idProcesso order by id")
				.setParameter("idProcesso", idProcesso)
				.getResultList();
	}	

}
