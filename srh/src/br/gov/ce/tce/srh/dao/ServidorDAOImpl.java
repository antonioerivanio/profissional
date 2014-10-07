package br.gov.ce.tce.srh.dao;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.Transformers;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Servidor;
import br.gov.ce.tce.srh.domain.sapjava.Setor;

/**
*
* @author Raphael P Ferreira - rpf1404@gmail.com
* 
*/
@Repository
public class ServidorDAOImpl implements ServidorDAO {

	static Logger logger = Logger.getLogger(ServidorDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Servidor> consultarServidoresPorSetor(Setor setor, int firstResult, int maxResults) {
		Query query = getQueryServidoresPorSetor(setor);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		query.setResultTransformer(Transformers.aliasToBean(Servidor.class));

		return query.list();
	}

	@Override
	public int getCountServidoresPorSetor(Setor setor) {
		return count(getQueryServidoresPorSetor(setor));
	}


	protected int count(Query query) {
		String queryString = "SELECT COUNT(*) FROM (" + query.getQueryString() + ")";
		BigDecimal count = (BigDecimal) getSession().createSQLQuery(queryString).uniqueResult();
		return count.intValue();
	}
	
	private Query getQueryServidoresPorSetor(Setor setor) {
		StringBuilder sql = new StringBuilder();

//		sql.append("SELECT S.NMSETOR \"nomeSetor\", ");
//		sql.append("F.NOMECOMPLETO \"nomeCompleto\", ");
//		sql.append("O.NOMENCLATURA \"cargo\", ");
//		sql.append("decode(CR.REFERENCIA, null, ' ', 'REF' || CR.REFERENCIA) \"referencia\", ");
//		sql.append("RC.NOMENCLATURA \"representacao\", ");
//		sql.append("RC.SIMBOLO \"simbolo\" ");
//		sql.append("FROM SRH.TB_REPRESENTACAOFUNCIONAL RF RIGHT JOIN SRH.TB_FUNCIONAL F ON RF.IDFUNCIONAL = F.ID INNER JOIN SAPJAVA.SETOR S ON F.IDSETOR = S.IDSETOR INNER JOIN SRH.TB_OCUPACAO O ON F.IDOCUPACAO = O.ID LEFT JOIN SRH.TB_REPRESENTACAOCARGO RC ON RF.IDREPRESENTACAOCARGO = RC.ID LEFT JOIN SRH.TB_CLASSEREFERENCIA CR ON F.IDCLASSEREFERENCIA = CR.ID ");
//		sql.append("WHERE F.IDTIPOMOVIMENTOSAIDA Is Null AND F.ATIVOFP=1 AND RF.IDTIPOMOVIMENTOFIM Is Null ");
//		sql.append("AND S.IDSETOR = " + setor.getId());
//		sql.append("ORDER BY S.NRORDEMSETORFOLHA, F.NOMECOMPLETO ");
		
		sql.append("SELECT DISTINCT S.NMSETOR \"nomeSetor\", F.NOMECOMPLETO \"nomeCompleto\", O.NOMENCLATURA \"cargo\", decode(CR.REFERENCIA, null, ' ', 'REF' || CR.REFERENCIA) \"referencia\", RC.NOMENCLATURA \"representacao\", RC.SIMBOLO \"simbolo\", RS.HIERARQUIA \"hierarquia\", S.NRORDEMSETORFOLHA \"nrOrdemSetorFolha\" ");
		sql.append("FROM SRH.TB_FUNCIONAL F ");
		sql.append("LEFT JOIN SRH.TB_REPRESENTACAOCARGO RC ON F.IDREPRESENTACAOCARGO = RC.ID ");
		sql.append("LEFT JOIN SAPJAVA.SETOR S ON F.IDSETOR = S.IDSETOR ");
		sql.append("LEFT JOIN SRH.TB_OCUPACAO O ON F.IDOCUPACAO = O.ID ");
		sql.append("LEFT JOIN SRH.TB_CLASSEREFERENCIA CR ON F.IDCLASSEREFERENCIA = CR.ID ");
		sql.append("LEFT JOIN SRH.TB_REPRESENTACAOSETOR RS ON RS.IDSETOR = S.IDSETOR AND RS.IDREPRESENTACAOCARGO = RC.ID ");
		sql.append("WHERE 1=1 ");
		sql.append("AND F.ATIVOFP           = 1 ");
		sql.append("AND F.STATUS            < 3 ");
		sql.append("AND F.IDSITUACAO        < 4 ");
		sql.append("AND F.DATASAIDA        IS NULL ");
		sql.append("AND F.DOESAIDA         IS NULL ");
		if (setor != null)
			sql.append("AND S.IDSETOR = " + setor.getId());
		sql.append("ORDER BY S.NRORDEMSETORFOLHA, F.NOMECOMPLETO");

		Query query = getSession().createSQLQuery(sql.toString());

		return query;
	}
	
	protected Session getSession() {
		return (Session) entityManager.getDelegate();
	}	
}
