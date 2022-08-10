package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.DadosPagtoPrestador;
import br.gov.ce.tce.srh.domain.DependenteEsocial;

@Repository
public class DadosPagtoPrestadorDAO {

	static Logger logger = Logger.getLogger(DadosPagtoPrestadorDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	

	public DadosPagtoPrestador getById(Long id) {
		return entityManager.find(DadosPagtoPrestador.class, id);
	}
	

	@SuppressWarnings("unchecked")
	public DadosPagtoPrestador findDadosPagtoPrestador(String competencia, Long idPagtoPrestador) {	
		Query query = entityManager.createNativeQuery(getSQLDadosPagtoPrestador(), DadosPagtoPrestador.class);
		query.setParameter("competencia", competencia);
		query.setParameter("idPagtoPrestador", idPagtoPrestador);
		return (DadosPagtoPrestador) query.getSingleResult();
	}

	
	public String getSQLDadosPagtoPrestador() {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT ");
		sql.append("dp.idpagtoprestador, ");
		sql.append("dp.nome, ");
		sql.append("dp.idprestador,   ");
		sql.append(" dp.novocbo, ");
		sql.append(" dp.valor_bruto,  ");
		sql.append(" dp.valor_inss,  ");
		sql.append("dp.valor_inss_origem,  ");
		sql.append(" dp.valor_iss,  ");
		sql.append(" dp.valor_irrf  ");
		sql.append("FROM  ");
		sql.append(" fp_dadospagtoprestador dp  ");
		   
		sql.append("WHERE  ");
		sql.append("  TO_CHAR(data_np, 'yyyymm') = :competencia  ");
		sql.append(" and dp.idpagtoprestador = :idPagtoPrestador  ");
		sql.append("ORDER BY  ");
		sql.append(" dp.data_np,  ");
		sql.append("dp.nome  ");
	    
	    return sql.toString();
	}

	@SuppressWarnings("unchecked")
	public List<DependenteEsocial> findDependenteEsocialByIdfuncional(Long idFuncional) {
		Query query = entityManager.createQuery("Select e from DependenteEsocial e where e.funcional.id = :idFuncional", DependenteEsocial.class);
		query.setParameter("idFuncional",idFuncional );
		return query.getResultList();
	}

}
