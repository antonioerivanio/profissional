package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;

@Repository
public class ESocialEventoVigenciaDAO {

	static Logger logger = Logger.getLogger(ESocialEventoVigenciaDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from ESocialEventoVigencia e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public ESocialEventoVigencia salvar(ESocialEventoVigencia entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}
	
	public ESocialEventoVigencia findById(Long id) {
		return entityManager.find(ESocialEventoVigencia.class, id);
	}
	
	public List<ESocialEventoVigencia> findByReferenciaAndTipoEvento(String referencia, TipoEventoESocial tipoEvento){
		TypedQuery<ESocialEventoVigencia> query = entityManager.createQuery("Select e from ESocialEventoVigencia e "
				+ "where e.referencia = :referencia and e.tipoEvento = :tipoEvento order by e.inicioValidade", ESocialEventoVigencia.class);
		query.setParameter("referencia", referencia);
		query.setParameter("tipoEvento", tipoEvento);
		return query.getResultList();
	}

}
