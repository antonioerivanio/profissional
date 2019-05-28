package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Aposentadoria;

@Repository
public class AposentadoriaDAOImpl implements AposentadoriaDAO {
	
	static Logger logger = Logger.getLogger(AposentadoriaDAOImpl.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery(" SELECT MAX(a.id) FROM Aposentadoria a ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public Aposentadoria salvar(Aposentadoria entidade) {
		
		if (entidade.getId() == null || entidade.getId() == 0){
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}
	

	@Override
	public void excluir(Aposentadoria entidade) {

		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
		
	}

	@Override
	public int count() {
		Query query = entityManager.createQuery(" SELECT COUNT(a) FROM Aposentadoria a ");
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Aposentadoria> search(Long idPessoal, int first, int rows) {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT a FROM Aposentadoria a ");
		
		if (idPessoal != null && idPessoal > 0) {
			sql.append(" WHERE a.funcional.pessoal.id = :idPessoal ");
		}
		
		sql.append(" ORDER BY  a.funcional.pessoal.nomeCompleto ");
		
		
		Query query = entityManager.createQuery(sql.toString());
				
		if (idPessoal != null && idPessoal > 0) {
			query.setParameter("idPessoal", idPessoal);
		}	
		
		if (first >= 0)
			query.setFirstResult(first);
		if (rows > 0)
			query.setMaxResults(rows);
		
		return query.getResultList();
	}		

}
