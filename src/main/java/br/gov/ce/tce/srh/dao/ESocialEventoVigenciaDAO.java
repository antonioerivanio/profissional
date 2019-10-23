package br.gov.ce.tce.srh.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.ProcessoESocial;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;

@Repository
public class ESocialEventoVigenciaDAO {

	static Logger logger = Logger.getLogger(ESocialEventoVigenciaDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) + 1 from ESocialEventoVigencia e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public ESocialEventoVigencia salvar(ESocialEventoVigencia entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(ProcessoESocial entidade) {
//		entidade = entityManager.merge(entidade);
//		entityManager.remove(entidade);
	}

	public ESocialEventoVigencia getById(Long id) {
		return entityManager.find(ESocialEventoVigencia.class, id);
	}

}
