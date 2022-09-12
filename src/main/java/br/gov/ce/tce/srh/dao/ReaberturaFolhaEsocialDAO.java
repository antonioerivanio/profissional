package br.gov.ce.tce.srh.dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import br.gov.ce.tce.srh.domain.FechamentoFolhaEsocial;
import br.gov.ce.tce.srh.domain.ReaberturaFolhaEsocial;
import br.gov.ce.tce.srh.service.AmbienteService;

@Repository
public class ReaberturaFolhaEsocialDAO {

	static Logger logger = Logger.getLogger(ReaberturaFolhaEsocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private AmbienteService ambienteService;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(e.id) FROM ReaberturaFolhaEsocial e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public ReaberturaFolhaEsocial salvar(ReaberturaFolhaEsocial entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(ReaberturaFolhaEsocial entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public ReaberturaFolhaEsocial getById(Long id) {
		return entityManager.find(ReaberturaFolhaEsocial.class, id);
	}
	
	public int count(String periodo) {
		
		StringBuffer sql = new StringBuffer();

		sql.append(" Select count(f) FROM ReaberturaFolhaEsocial f WHERE 1=1 ");

		if (periodo != null && !periodo.isEmpty()) {
			sql.append("  and upper( f.periodoApuracao ) like :periodoApuracao ");
		}
	
						
		Query query = entityManager.createQuery(sql.toString());

		if (periodo != null && !periodo.isEmpty()) {
			query.setParameter("periodoApuracao", "%" + periodo + "%");
		}
		
		
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<ReaberturaFolhaEsocial> search(String periodo, Integer first, Integer rows) {

		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT f FROM ReaberturaFolhaEsocial f WHERE 1=1 ");

		if (periodo != null && !periodo.isEmpty()) {
			sql.append("  and upper( f.periodoApuracao ) like :periodoApuracao ");
		}
		

		sql.append("  ORDER BY f.id DESC ");

		Query query = entityManager.createQuery(sql.toString());

		if (periodo != null && !periodo.isEmpty()) {
			query.setParameter("periodoApuracao", "%" + periodo.toUpperCase() + "%");
		}
		
		if (first != null && first >= 0)
			query.setFirstResult(first);
		if (rows != null && rows > 0)
			query.setMaxResults(rows);

		return query.getResultList();
	}
}

