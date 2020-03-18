package br.gov.ce.tce.srh.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Ferias;

@Repository
public class FeriasDAO {
	
	static Logger logger = Logger.getLogger(FeriasDAO.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(f.id) FROM Ferias f");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public Ferias salvar(Ferias entidade) {
		
		if (entidade.getId() == null || entidade.getId() == 0){
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(Ferias entidade) {
		
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);		
	}

	public int count(Long idPessoal) {
		Query query = entityManager.createQuery("SELECT count (f) FROM Ferias f WHERE f.funcional.pessoal.id = :pessoal order by f.anoReferencia desc, f.inicio desc ");
		query.setParameter("pessoal", idPessoal);
		return ((Long) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Ferias> search(Long idPessoal, int first, int rows) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT f FROM Ferias f ");
		sql.append("         WHERE f.funcional.pessoal.id = :pessoal ");
		sql.append("         ORDER BY  f.anoReferencia desc, f.inicio DESC ");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("pessoal", idPessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Ferias> findByPessoal(Long idPessoal) {
		Query query = entityManager.createQuery("SELECT f FROM Ferias f WHERE f.funcional.pessoal.id = :pessoal order by f.anoReferencia desc, f.inicio desc ");
		query.setParameter("pessoal", idPessoal);
		return query.getResultList();
	}

	public Ferias findMaisRecenteByPessoal(Long idPessoal) {
		Query query = entityManager.createQuery("SELECT f FROM Ferias f WHERE f.funcional.pessoal.id = :pessoal AND f.fim IS NOT NULL ORDER BY f.fim DESC");
		query.setParameter("pessoal", idPessoal);
		query.setMaxResults(1);
		return (Ferias) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<Ferias> findByPessoalPeriodoReferencia(Long idPessoal, Long periodo, Long anoReferencia, Long tipo) {
		Query query = entityManager.createQuery("SELECT f FROM Ferias f WHERE f.funcional.pessoal.id = :pessoal AND f.periodo = :periodo AND f.anoReferencia = :ano AND f.tipoFerias.id = :tipo");
		query.setParameter("pessoal", idPessoal);
		query.setParameter("periodo", periodo);
		query.setParameter("ano", anoReferencia);
		query.setParameter("tipo", tipo);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Ferias> findByPessoalPeriodoReferencia(Long idPessoal, Long periodo, Long anoReferencia) {
		Query query = entityManager.createQuery("SELECT f FROM Ferias f WHERE f.funcional.pessoal.id = :pessoal AND f.periodo = :periodo AND f.anoReferencia = :ano");
		query.setParameter("pessoal", idPessoal);
		query.setParameter("periodo", periodo);
		query.setParameter("ano", anoReferencia);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Ferias> findByPessoalTipo(Long idPessoal, Long tipo) {
		Query query = entityManager.createQuery("SELECT f FROM Ferias f WHERE f.funcional.pessoal.id = :pessoal AND f.tipoFerias.id = :tipo");
		query.setParameter("pessoal", idPessoal);
		query.setParameter("tipo", tipo);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<Ferias> findByInicioETipo(Date inicio, List<Long> tiposId) {
		
		String consulta = "SELECT f FROM Ferias f WHERE f.inicio = :inicio AND f.tipoFerias.id in :tiposId ";
		
		Query query = entityManager.createQuery(consulta);
		query.setParameter("inicio", inicio);
		query.setParameter("tiposId", tiposId);		
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<Ferias> findByInicioETipo(Date inicio, Date fim, List<Long> tiposId) {
		
		String consulta = "SELECT f FROM Ferias f WHERE f.inicio >= :inicio AND f.inicio <= :fim AND f.tipoFerias.id in :tiposId ORDER BY f.inicio";
		
		Query query = entityManager.createQuery(consulta);
		query.setParameter("inicio", inicio);
		query.setParameter("fim", fim);
		query.setParameter("tiposId", tiposId);		
		
		return query.getResultList();
	}
	
	public List<Ferias> findFruicaoByPessoalEAno(Long idPessoal, String ano) {
		
		String consulta = "SELECT f FROM Ferias f "
				+ "WHERE f.funcional.pessoal.id = :pessoal "
				+ "AND to_char(f.inicio, 'YYYY') = :ano "
				+ "AND f.tipoFerias.id in (1, 3) "
				+ "ORDER BY f.inicio";
		
		TypedQuery<Ferias> query = entityManager.createQuery(consulta, Ferias.class);
		query.setParameter("inicio", idPessoal);
		query.setParameter("ano", ano);	
		
		return query.getResultList();
	
	}

}
