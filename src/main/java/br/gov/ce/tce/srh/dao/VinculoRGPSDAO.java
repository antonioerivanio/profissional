package br.gov.ce.tce.srh.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.VinculoRGPS;

@Repository
public class VinculoRGPSDAO {
	
	static Logger logger = Logger.getLogger(VinculoRGPSDAO.class);
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("SELECT MAX(v.id) FROM VinculoRGPS  v");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public VinculoRGPS salvar(VinculoRGPS entidade) {
		
		if (entidade.getId() == null || entidade.getId() == 0){
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(VinculoRGPS entidade) {
		
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);		
	}

	public int count(Long idPessoal) {
		Query query = entityManager.createQuery("SELECT count (v) FROM VinculoRGPS v WHERE v.funcional.pessoal.id = :pessoal order by v.anoReferencia desc, v.inicio desc ");
		query.setParameter("pessoal", idPessoal);
		return ((Long) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<VinculoRGPS> search(Long idPessoal, int first, int rows) {
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT v FROM VinculoRGPS v ");
		sql.append("         WHERE v.funcional.pessoal.id = :pessoal ");
		sql.append("         ORDER BY  v.anoReferencia desc, v.inicio DESC ");
		Query query = entityManager.createQuery(sql.toString());
		query.setParameter("pessoal", idPessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<VinculoRGPS> findByPessoal(Long idPessoal) {
		Query query = entityManager.createQuery("SELECT v FROM VinculoRGPS v WHERE v.funcional.pessoal.id = :pessoal order by v.anoReferencia desc, v.inicio desc ");
		query.setParameter("pessoal", idPessoal);
		return query.getResultList();
	}

	public VinculoRGPS findMaisRecenteByPessoal(Long idPessoal) {
		Query query = entityManager.createQuery("SELECT v FROM VinculoRGPS v WHERE v.funcional.pessoal.id = :pessoal AND v.fim IS NOT NULL ORDER BY v.fim DESC");
		query.setParameter("pessoal", idPessoal);
		query.setMaxResults(1);
		return (VinculoRGPS) query.getSingleResult();
	}

	@SuppressWarnings("unchecked")
	public List<VinculoRGPS> findByPessoalPeriodoReferencia(Long idPessoal, Long periodo, Long anoReferencia, Long tipo) {
		Query query = entityManager.createQuery("SELECT v FROM VinculoRGPS v WHERE v.funcional.pessoal.id = :pessoal AND v.periodo = :periodo AND v.anoReferencia = :ano AND v.tipoVinculoRGPS.id = :tipo");
		query.setParameter("pessoal", idPessoal);
		query.setParameter("periodo", periodo);
		query.setParameter("ano", anoReferencia);
		query.setParameter("tipo", tipo);
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<VinculoRGPS> findByPessoalPeriodoReferencia(Long idPessoal, Long periodo, Long anoReferencia) {
		Query query = entityManager.createQuery("SELECT v FROM VinculoRGPS v WHERE v.funcional.pessoal.id = :pessoal AND v.periodo = :periodo AND v.anoReferencia = :ano");
		query.setParameter("pessoal", idPessoal);
		query.setParameter("periodo", periodo);
		query.setParameter("ano", anoReferencia);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<VinculoRGPS> findByPessoalTipo(Long idPessoal, Long tipo) {
		Query query = entityManager.createQuery("SELECT v FROM VinculoRGPS v WHERE v.funcional.pessoal.id = :pessoal AND v.tipoVinculoRGPS.id = :tipo");
		query.setParameter("pessoal", idPessoal);
		query.setParameter("tipo", tipo);
		return query.getResultList();
	}

	@SuppressWarnings("unchecked")
	public List<VinculoRGPS> findByInicioETipo(Date inicio, List<Long> tiposId) {
		
		String consulta = "SELECT v FROM VinculoRGPS v WHERE v.inicio = :inicio AND v.tipoVinculoRGPS.id in :tiposId ";
		
		Query query = entityManager.createQuery(consulta);
		query.setParameter("inicio", inicio);
		query.setParameter("tiposId", tiposId);		
		
		return query.getResultList();
	}
	
	@SuppressWarnings("unchecked")
	public List<VinculoRGPS> findByInicioETipo(Date inicio, Date fim, List<Long> tiposId) {
		
		String consulta = "SELECT v FROM VinculoRGPS v WHERE v.inicio >= :inicio AND v.inicio <= :fim AND v.tipoVinculoRGPS.id in :tiposId ORDER BY v.inicio";
		
		Query query = entityManager.createQuery(consulta);
		query.setParameter("inicio", inicio);
		query.setParameter("fim", fim);
		query.setParameter("tiposId", tiposId);		
		
		return query.getResultList();
	}
	
	public List<VinculoRGPS> findFruicaoByPessoalEAno(Long idPessoal, String ano) {
		
		String consulta = "SELECT v FROM VinculoRGPS v "
				+ "WHERE v.funcional.pessoal.id = :pessoal "
				+ "AND to_char(v.inicio, 'YYYY') = :ano "
				+ "AND v.tipoVinculoRGPS.id in (1, 3) "
				+ "ORDER BY v.inicio";
		
		TypedQuery<VinculoRGPS> query = entityManager.createQuery(consulta, VinculoRGPS.class);
		query.setParameter("pessoal", idPessoal);
		query.setParameter("ano", ano);	
		
		return query.getResultList();
	
	}

}
