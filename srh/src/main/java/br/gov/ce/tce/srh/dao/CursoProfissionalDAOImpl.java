package br.gov.ce.tce.srh.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CursoProfissional;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class CursoProfissionalDAOImpl implements CursoProfissionalDAO {

	static Logger logger = Logger.getLogger(CursoProfissionalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public CursoProfissional getById(Long id) {
		return entityManager.find(CursoProfissional.class, id);
	}


	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT max(c.id) FROM CursoProfissional c");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}


	@Override
	public CursoProfissional salvar(CursoProfissional entidade) {
		
		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(CursoProfissional entidade) {
		Query query = entityManager.createQuery("DELETE FROM CursoProfissional c WHERE c.id=:id");
		query.setParameter("id", entidade.getId());
		query.executeUpdate();
	}


	@Override
	public int count(String descricao, Long idArea, Date inicio, Date fim) {
		
		Query query = consultaDeCursos(descricao, idArea, inicio, fim, true);			
		
		return ((Long) query.getSingleResult()).intValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<CursoProfissional> search(String descricao, Long idArea, Date inicio, Date fim, int first, int rows) {
		
		Query query = consultaDeCursos(descricao, idArea, inicio, fim, false);
				
		if (first >= 0 && rows >= 0) {
			query.setFirstResult(first);
			query.setMaxResults(rows);
		}	
		
		return query.getResultList();
	}

	
	private Query consultaDeCursos(String descricao, Long idArea, Date inicio, Date fim, boolean somenteCount){
		
		
		StringBuilder consulta = new StringBuilder();
		
		if(somenteCount)
			consulta.append("SELECT count (c) ");
		else
			consulta.append("SELECT c ");
			
		consulta.append(" FROM CursoProfissional c WHERE TRIM(UPPER( c.descricao )) LIKE :descricao ");
		
		if(idArea != null)
			consulta.append(" AND c.area.id = :idArea ");
		if(inicio != null)
			consulta.append(" AND c.inicio >= :inicio ");
		if(fim != null)
			consulta.append(" AND c.inicio <= :fim ");	
		
		if(!somenteCount)
			consulta.append(" ORDER BY c.inicio DESC, c.descricao ");		
		
		
		Query query = entityManager.createQuery(consulta.toString());
		
		query.setParameter("descricao", "%" + descricao.toUpperCase() + "%");		
		
		if(idArea != null)
			query.setParameter("idArea", idArea);
		if(inicio != null)
			query.setParameter("inicio", inicio);
		if(fim != null)
			query.setParameter("fim", fim);
		
		return query;		
		
	}
	
	

	@Override
	public CursoProfissional getByCursoAreaInstituicao(String curso, Long area, Long instituicao) {
		try {
			Query query = entityManager.createQuery("SELECT c FROM CursoProfissional c join fetch c.area join fetch c.instituicao WHERE upper( c.descricao ) = :descricao AND c.area.id = :area AND c.instituicao.id = :instituicao");
			query.setParameter("descricao", curso.toUpperCase() );
			query.setParameter("area", area);
			query.setParameter("instituicao", instituicao);
			return (CursoProfissional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CursoProfissional> findByArea(Long area) {
		Query query = entityManager.createQuery("SELECT c FROM CursoProfissional c join fetch c.area  WHERE c.area.id = :area ORDER BY c.descricao ");
		query.setParameter("area", area);
		return query.getResultList();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<CursoProfissional> findAll() {
		return entityManager.createQuery("SELECT c FROM CursoProfissional c join fetch c.area join fetch c.instituicao ORDER BY c.area.descricao, c.descricao ").getResultList();
	}

}
