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
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;

/**
 *
 * @author robstown
 * 
 */
@Repository
public class PessoalCursoProfissionalDAOImpl implements PessoalCursoProfissionalDAO {

	static Logger logger = Logger.getLogger(PessoalCursoProfissionalDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public PessoalCursoProfissional salvar(PessoalCursoProfissional entidade) {
		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(Long curso) {
		Query query = entityManager.createQuery("DELETE FROM PessoalCursoProfissional p WHERE p.pk.cursoProfissional = :curso ");
		query.setParameter("curso", curso );
		query.executeUpdate();
	}


	@Override
	public int count(Long area, String curso) {
		Query query = entityManager.createQuery("Select count(*) from CursoProfissional c where c.area.id = :area AND upper( c.descricao ) like :curso ORDER BY c.descricao ");
		query.setParameter("area", area);
		query.setParameter("curso", "%" + curso.toUpperCase() + "%");
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@Override
	public int count(Long pessoal, Date inicio, Date fim, boolean areaAtuacao) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("Select count(*) from PessoalCursoProfissional e where e.pk.pessoal = :pessoal ");
		
		if(areaAtuacao)
			sql.append(" AND e.areaAtuacao = :areaAtuacao");
		
		if (inicio != null ) 	
			sql.append(" AND e.cursoProfissional.fim >= :inicio");					
		
		if (fim != null) 
			sql.append(" AND e.cursoProfissional.fim <= :fim ");
		
		sql.append(" ORDER BY e.cursoProfissional.inicio desc");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("pessoal", pessoal);
		if (inicio != null )
			query.setParameter("inicio", inicio);
		if (fim != null)
			query.setParameter("fim", fim);
		if(areaAtuacao)
			query.setParameter("areaAtuacao", areaAtuacao);		
		
		
		return ((Long) query.getSingleResult()).intValue();
	}
	

	@Override
	@SuppressWarnings("unchecked")
	public List<CursoProfissional> search(Long area, String curso, int first, int rows) {
		Query query = entityManager.createQuery("SELECT new CursoProfissional(c.id, c.area, c.descricao, c.inicio, c.fim) FROM CursoProfissional c WHERE c.area.id = :area AND upper(c.descricao) LIKE :curso ORDER BY c.descricao");
		query.setParameter("area", area);
		query.setParameter("curso", "%" + curso.toUpperCase() + "%");
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}


	@Override
	public PessoalCursoProfissional getByCurso(Long curso) {
		try {
			Query query = entityManager.createQuery("Select e from PessoalCursoProfissional e where e.pk.cursoProfissional = :curso ");
			query.setParameter("curso", curso );
			return (PessoalCursoProfissional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<PessoalCursoProfissional> findByCurso(Long cursoProfissional) {
		Query query = entityManager.createQuery("Select e from PessoalCursoProfissional e where e.pk.cursoProfissional = :curso");
		query.setParameter("curso", cursoProfissional);
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<PessoalCursoProfissional> search(Long pessoal, Date inicio, Date fim, boolean areaAtuacao, int first, int rows) {
		StringBuilder sql = new StringBuilder();
		sql.append("Select e from PessoalCursoProfissional e where e.pk.pessoal = :pessoal ");
		
		if(areaAtuacao)
			sql.append(" AND e.areaAtuacao = :areaAtuacao");
		
		if (inicio != null ) 	
			sql.append(" AND e.cursoProfissional.fim >= :inicio");					
		
		if (fim != null) 
			sql.append(" AND e.cursoProfissional.fim <= :fim ");
		
		sql.append(" ORDER BY e.cursoProfissional.inicio desc");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("pessoal", pessoal);
		if (inicio != null )
			query.setParameter("inicio", inicio);
		if (fim != null)
			query.setParameter("fim", fim);
		if(areaAtuacao)
			query.setParameter("areaAtuacao", areaAtuacao);		
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<PessoalCursoProfissional> getCursos(Long pessoal, Date inicio, Date fim, boolean areaAtuacao) {
		StringBuilder sql = new StringBuilder();
		sql.append("Select e from PessoalCursoProfissional e where e.pk.pessoal = :pessoal ");
		
		if(areaAtuacao)
			sql.append(" AND e.areaAtuacao = :areaAtuacao");
		
		if (inicio != null ) 	
			sql.append(" AND e.cursoProfissional.fim >= :inicio");					
		
		if (fim != null) 
			sql.append(" AND e.cursoProfissional.fim <= :fim ");
		
		sql.append(" ORDER BY e.cursoProfissional.inicio desc");
		
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("pessoal", pessoal);
		if (inicio != null )
			query.setParameter("inicio", inicio);
		if (fim != null)
			query.setParameter("fim", fim);
		if(areaAtuacao)
			query.setParameter("areaAtuacao", areaAtuacao);		

		return query.getResultList();
	}
}
