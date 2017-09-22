package br.gov.ce.tce.srh.dao;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.RelatorioFerias;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

@Repository
public class RelatorioFeriasDAOImpl implements RelatorioFeriasDAO {
	
	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager){
		this.entityManager = entityManager;
	}
	
	@Override
	public int getCountFindByParameter(Setor setor,	List<String> tiposFerias, Date inicio, Date fim, TipoOcupacao tipoOcupacao) {
		return count(getQueryFindByParameter(setor, tiposFerias, inicio, fim, tipoOcupacao));
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<RelatorioFerias> findByParameter(Setor setor, List<String> tiposFerias, Date inicio, Date fim, TipoOcupacao tipoOcupacao, int firstResult, int maxResults) {		
		Query query = entityManager.createNativeQuery(getQueryFindByParameter(setor, tiposFerias, inicio, fim, tipoOcupacao), "RelatorioFerias"); 
		query.setFirstResult(firstResult);
		query.setMaxResults(maxResults);		
		return query.getResultList();
	}
	

	private String getQueryFindByParameter(Setor setor,	List<String> tiposFerias, Date inicio, Date fim, TipoOcupacao tipoOcupacao) {
		
		StringBuilder sql = new StringBuilder();

		sql.append("SELECT DISTINCT rownum idnum, TB_PESSOAL.NOMECOMPLETO nomeCompleto, ");
		sql.append("TB_FERIAS.ANOREFERENCIA anoReferencia, ");
		sql.append("TB_FERIAS.INICIO inicio, ");
		sql.append("TB_FERIAS.FIM fim, ");
		sql.append("tb_tipoferias.DESCRICAO tipoFerias, ");
		sql.append("TB_FERIAS.QTDEDIAS qtdeDias, ");
		sql.append("TB_FERIAS.PERIODO periodo, ");
		sql.append("TB_OCUPACAO.ORDEMOCUPACAO ");
		sql.append("FROM SRH.TB_FERIAS ");
		sql.append("INNER JOIN SRH.TB_FUNCIONAL ON TB_FERIAS.IDFUNCIONAL   = TB_FUNCIONAL.ID ");
		sql.append("INNER JOIN SRH.TB_PESSOAL ON TB_FUNCIONAL.IDPESSOAL  = TB_PESSOAL.ID ");
		sql.append("INNER JOIN SRH.tb_tipoferias ON tb_tipoferias.ID = TB_FERIAS.TIPOFERIAS ");
		sql.append("INNER JOIN SRH.TB_OCUPACAO ON TB_OCUPACAO.ID = TB_FUNCIONAL.IDOCUPACAO ");
		sql.append("WHERE 1=1 ");

		if (setor!=null && setor.getId() != null && setor.getId() != 0L){
			sql.append("and tb_funcional.idsetor = '" + setor.getId() + "' " );
		}
		
		if (tiposFerias != null && !tiposFerias.isEmpty()){
			boolean primeiro = true;
			String temp = "";
			temp += "and TB_FERIAS.TIPOFERIAS in (";
			for (String string : tiposFerias) {
				if(primeiro){
					primeiro = false;
				}else{
					temp += ", ";
				}
				temp += string;
			}
			temp += ") ";
			sql.append(temp);
		}
		
		if (inicio != null) {
			sql.append("and TB_FERIAS.INICIO >= to_date('" + dateToString(inicio) +"', 'dd/MM/yyyy') ");					
		}
		
		if (fim != null) {
			sql.append("and TB_FERIAS.INICIO <= to_date('" + dateToString(fim) +"', 'dd/MM/yyyy') ");					
		}
		
		if (tipoOcupacao != null) {
			sql.append("and TB_OCUPACAO.TIPOOCUPACAO = " + tipoOcupacao.getId());
		}
		
		// Se for escolhido o tipo ocupação Membros
		if (tipoOcupacao != null && tipoOcupacao.getId().intValue() == 1)			
			sql.append(" ORDER BY TB_OCUPACAO.ORDEMOCUPACAO, TB_PESSOAL.NOMECOMPLETO, TB_FERIAS.ANOREFERENCIA DESC, TB_FERIAS.INICIO DESC ");
		else
			sql.append(" ORDER BY TB_PESSOAL.NOMECOMPLETO, TB_FERIAS.ANOREFERENCIA DESC, TB_FERIAS.INICIO DESC ");

						
		return sql.toString();
	}
	
	private String dateToString(Date data) {
		return new SimpleDateFormat("dd/MM/yyyy").format(data);
	}
	
	private int count(String query) {
		String queryString = "SELECT COUNT(*) FROM (" + query + ")";
		BigDecimal count = (BigDecimal) entityManager.createNativeQuery(queryString).getSingleResult();
		return count.intValue();
	}

}
