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
import br.gov.ce.tce.srh.sapjava.domain.Setor;

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
	public List<Servidor> consultarServidoresPorSetor(Setor setor, Integer vinculo, Boolean ativoPortal, int firstResult, int maxResults) {
		Query query = getQueryServidoresPorSetor(setor, vinculo, ativoPortal);
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);
		query.setResultTransformer(Transformers.aliasToBean(Servidor.class));

		return query.list();
	}

	@Override
	public int getCountServidoresPorSetor(Setor setor, Integer vinculo, Boolean ativoPortal) {
		return count(getQueryServidoresPorSetor(setor, vinculo, ativoPortal));
	}


	protected int count(Query query) {
		String queryString = "SELECT COUNT(*) FROM (" + query.getQueryString() + ")";
		BigDecimal count = (BigDecimal) getSession().createSQLQuery(queryString).uniqueResult();
		return count.intValue();
	}
	
	private Query getQueryServidoresPorSetor(Setor setor, Integer vinculo, Boolean ativoPortal) {
		StringBuilder sql = new StringBuilder();
		
		sql.append("SELECT DISTINCT S.NMSETOR \"nomeSetor\", "
				+ "F.NOMECOMPLETO \"nomeCompleto\", "
				+ "O.NOMENCLATURA \"cargo\", "
				+ "decode(CR.REFERENCIA, null, ' ', 'REF' || CR.REFERENCIA) \"referencia\", "
				+ "RC.NOMENCLATURA || decode(RF.TIPONOMEACAO, '1', ' - Titular', decode(RF.TIPONOMEACAO, '2', ' - Substituto', decode(RF.TIPONOMEACAO, '3', ' - Designado', ''))) \"representacao\", "
				+ "RC.SIMBOLO \"simbolo\", "
				+ "S.NRORDEMSETORFOLHA \"nrOrdemSetorFolha\", "
				+ "F.IDFOLHA \"idFolha\", "
				+ "O.ORDEMOCUPACAO \"ordemOcupacao\"");
		
		sql.append("FROM SRH.TB_FUNCIONAL F ");
		sql.append("LEFT JOIN SRH.TB_OCUPACAO O ON F.IDOCUPACAO = O.ID ");
		sql.append("LEFT JOIN SRH.TB_TIPOOCUPACAO TOC ON O.TIPOOCUPACAO = TOC.ID ");
		sql.append("LEFT JOIN SRH.TB_CLASSEREFERENCIA CR ON F.IDCLASSEREFERENCIA = CR.ID ");
		sql.append("LEFT JOIN SAPJAVA.SETOR S ON F.IDSETOR = S.IDSETOR ");
		sql.append("LEFT JOIN SRH.TB_REPRESENTACAOFUNCIONAL RF ON RF.IDFUNCIONAL = F.ID AND RF.FIM IS NULL ");
		sql.append("LEFT JOIN SRH.TB_REPRESENTACAOCARGO RC ON RF.IDREPRESENTACAOCARGO = RC.ID ");
			
		sql.append("WHERE F.ATIVOFP = 1 ");		
		sql.append("AND F.IDSITUACAO < 4 ");
				
		if(ativoPortal){
			sql.append("AND F.FLPORTALTRANSPARENCIA = 1 ");
		}		
		
		if(setor != null){
			sql.append("AND S.IDSETOR = " + setor.getId() + " ");
		}
		
		if(vinculo == 1){ // MEMBROS
			sql.append("AND F.DATASAIDA IS NULL ");
			sql.append("AND F.STATUS = 1 ");
			sql.append("AND TOC.ID = 1 ");
			sql.append("ORDER BY O.ORDEMOCUPACAO, S.NRORDEMSETORFOLHA, F.NOMECOMPLETO");
		}else if(vinculo == 2){ // SERVIDORES ATIVOS
			sql.append("AND F.DATASAIDA IS NULL ");
			sql.append("AND F.STATUS = 1 ");
			sql.append("AND O.SITUACAO < 3 ");
			sql.append("AND TOC.ID IN (2, 3, 6) ");
//			sql.append("ORDER BY S.NRORDEMSETORFOLHA, F.IDFOLHA, O.ORDEMOCUPACAO, F.NOMECOMPLETO");
			sql.append("ORDER BY S.NRORDEMSETORFOLHA, RC.SIMBOLO, F.NOMECOMPLETO");
		}else if(vinculo == 3){ // SERVIDORES EFETIVOS
			sql.append("AND F.DATASAIDA IS NULL ");
			sql.append("AND F.STATUS = 1 ");
			sql.append("AND O.SITUACAO < 3 ");
			sql.append("AND (TOC.ID = 2) ");
			sql.append("ORDER BY S.NRORDEMSETORFOLHA, F.IDFOLHA, O.ORDEMOCUPACAO, F.NOMECOMPLETO");
		}else if(vinculo == 4){ // SERVIDORES INATIVOS
			sql.append("AND F.STATUS = 5 ");
			sql.append("ORDER BY O.ORDEMOCUPACAO, F.NOMECOMPLETO");
		}else if(vinculo == 5){ // OCUPANTES DE CARGO COMISSIONADO
			sql.append("AND F.DATASAIDA IS NULL ");
			sql.append("AND F.STATUS = 1 ");					
			sql.append("AND RF.ID IS NOT NULL ");
			sql.append("AND O.SITUACAO < 3 ");
			sql.append("ORDER BY S.NRORDEMSETORFOLHA, O.ORDEMOCUPACAO, F.NOMECOMPLETO");
		}else if(vinculo == 6){ // OCUPANTES SOMENTE CARGO COMISSIONADO
			sql.append("AND F.DATASAIDA IS NULL ");
			sql.append("AND F.STATUS = 1 ");
			sql.append("AND RF.ID IS NOT NULL ");
			sql.append("AND TOC.ID = 6 ");
			sql.append("ORDER BY S.NRORDEMSETORFOLHA, O.ORDEMOCUPACAO, F.NOMECOMPLETO");
		}else if(vinculo == 7){ // ESTAGIÁRIOS NÍVEL UNIVERSITÁRIO
			sql.append("AND F.DATASAIDA IS NULL ");
			sql.append("AND F.STATUS = 2 ");
			sql.append("AND TOC.ID = 5 ");
			sql.append("ORDER BY S.NRORDEMSETORFOLHA, F.NOMECOMPLETO");
		}else if(vinculo == 8){ // ESTAGIÁRIOS NÍVEL MÉDIO
			sql.append("AND F.DATASAIDA IS NULL ");
			sql.append("AND F.STATUS = 2 ");
			sql.append("AND TOC.ID = 4 ");
			sql.append("ORDER BY S.NRORDEMSETORFOLHA, F.NOMECOMPLETO");
		}else if(vinculo == 9){ // CESSÃO DE SERVIDOR SEM NENHUMA REMUNERAÇÃO
			sql.append("AND F.DATASAIDA IS NULL ");
			sql.append("AND O.SITUACAO < 3 ");	
			sql.append("AND TOC.ID = 8 ");
			sql.append("ORDER BY F.NOMECOMPLETO");
		}else{
			sql.append("AND F.DATASAIDA IS NULL ");
			sql.append("AND F.STATUS < 3 ");
			sql.append("AND O.SITUACAO < 3 ");				 
			sql.append("ORDER BY S.NRORDEMSETORFOLHA, O.ORDEMOCUPACAO, F.NOMECOMPLETO");
		}
		
		Query query = getSession().createSQLQuery(sql.toString());

		return query;
	}
	
	protected Session getSession() {
		return (Session) entityManager.getDelegate();
	}	
}
