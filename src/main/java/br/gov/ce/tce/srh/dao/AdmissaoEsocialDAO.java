package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Admissao;
import br.gov.ce.tce.srh.domain.AdmissaoVTO;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.service.AmbienteService;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class AdmissaoEsocialDAO {

	static Logger logger = Logger.getLogger(AdmissaoEsocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private AmbienteService ambienteService;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from Admissao e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public Admissao salvar(Admissao entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(Admissao entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public Admissao getById(Long id) {
		return entityManager.find(Admissao.class, id);
	}

	public int count(String nome, String cpf) {

		StringBuffer sql = new StringBuffer();

		sql.append(" Select count(a) FROM Admissao a inner join a.funcional f WHERE 1=1 ");

		if (nome != null && !nome.isEmpty()) {
			sql.append("  and upper( f.nome ) like :nome ");
		}

		if (cpf != null && !cpf.isEmpty()) {
			sql.append("  AND f.cpf = :cpf ");
		}

		Query query = entityManager.createQuery(sql.toString());

		if (nome != null && !nome.isEmpty()) {
			query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		}

		if (cpf != null && !cpf.isEmpty()) {
			query.setParameter("cpf", SRHUtils.removerMascara(cpf));
		}
		return ((Long) query.getSingleResult()).intValue();
	}

	@SuppressWarnings("unchecked")
	public List<Admissao> search(String nome, String cpf, Integer first, Integer rows) {

		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT e FROM Admissao e inner join fetch e.funcional f WHERE 1=1 ");

		if (nome != null && !nome.isEmpty()) {
			sql.append("  and upper( f.nome ) like :nome ");
		}

		if (cpf != null && !cpf.isEmpty())
			sql.append("  AND f.cpf = :cpf ");

		sql.append("  ORDER BY f.nome ");

		Query query = entityManager.createQuery(sql.toString());

		if (nome != null && !nome.isEmpty()) {
			query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		}

		if (cpf != null && !cpf.isEmpty()) {
			query.setParameter("cpf", SRHUtils.removerMascara(cpf));
		}
		if (first != null && first >= 0)
			query.setFirstResult(first);
		if (rows != null && rows > 0)
			query.setMaxResults(rows);

		return query.getResultList();
	}

	public Admissao getEventoS2200ByServidor(Funcional servidorFuncional, boolean possuiCargo) {
		try {
			Query query = entityManager.createNativeQuery(getSQLEventoS2200(possuiCargo), Admissao.class);
			query.setParameter("idFuncional", servidorFuncional.getId());
			return (Admissao) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}	


	public AdmissaoVTO getEventoS2200ByServidor(Funcional servidorFuncional) {
		try {
			
			String nomeTab = "SRH.ESOCIAL_ADMISSAO";
			String filtro = "SRH.ESOCIAL_ADMISSAO.idFuncional = :idFuncional";
			
			Query query = entityManager.createNativeQuery(getSQLEventoS2200(nomeTab, filtro), AdmissaoVTO.class);
			query.setParameter("idFuncional", servidorFuncional.getId());
			return (AdmissaoVTO) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public AdmissaoVTO getEventoS2200ConectorByReferencia(String referencia) {
		try {
			String nomeTab = "CONECTOR_ESOCIAL.CON_S2200_ADMISSAO_OLD";
			String filtro = "CONECTOR_ESOCIAL.CON_S2200_ADMISSAO_OLD.ID = :referencia";

			Query query = entityManager.createNativeQuery(getSQLEventoS2200(nomeTab, filtro), AdmissaoVTO.class);
			query.setParameter("referencia", referencia);
			return (AdmissaoVTO) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public String getSQLEventoS2200(boolean possuiCargo) {
		StringBuffer sql = new StringBuffer();
		String dataLimiteEsocial = "";

		if (ambienteService.ambiente().isProducao()) {
			dataLimiteEsocial = "21/11/2021";
		} else {
			dataLimiteEsocial = "28/02/2017";
		}

		sql.append(" SELECT ");
		sql.append(" * ");
		sql.append(" FROM ");
		sql.append(" ( ");
		sql.append("  SELECT ");
		sql.append(" f.id-10000 as id, ");
		sql.append("f.id||'-'||o.id AS referencia, ");
		sql.append(" f.id AS idfuncional, ");
		sql.append(" p.nome AS nm_trab, ");
		sql.append(" p.cpf AS cpf_trab, ");
		sql.append("  p.sexo AS sexo, ");
		sql.append("  p.idraca AS raca_cor, ");
		sql.append("  ec.codigoesocial AS est_civ, ");
		sql.append("  p.idescolaridade AS grau_instr, ");
		sql.append(" p.nomesocial AS NM_SOCIAL, ");
		sql.append(" p.datanascimento AS dt_nasc, ");
		sql.append(" pais_nascimento.codigo AS pais_nasc, ");
		sql.append(" pais_nacionalidade.codigo AS pais_nac, ");
		sql.append("  tl.codigo AS tp_lograd, ");
		sql.append("  p.endereco AS dsc_lograd, ");
		sql.append("  p.numero AS nr_lograd, ");
		sql.append("  p.complemento AS complemento, ");
		sql.append("  p.bairro AS bairro, ");
		sql.append(" TRIM (p.cep) AS cep, ");
		sql.append(" m.cod_municipio AS cod_munic_end, ");
		sql.append("  m.uf AS uf_end, ");
		sql.append("  TRIM (p.celular) AS fone_princ, ");
		sql.append("  TRIM (p.email) AS email_princ, ");
		sql.append(" '0'||f.matricula AS matricula, ");
		sql.append("  2 AS tp_reg_trab, ");
		sql.append(" Decode(c.contribui_inss,'S', 1, 2) AS tp_reg_prev, ");
		sql.append(" CASE c.FUNDO_PREVIDENCIARIO ");
		sql.append(" WHEN 1 THEN 2  ");
		sql.append(" WHEN 2 THEN 1  ");
		sql.append(" ELSE null  ");
		sql.append("END AS TP_PLAN_RP,");
		sql.append(" NULL  AS DSC_SAL_VAR, ");
		sql.append(" NULL  AS TP_CONTR, ");
		sql.append("  1     AS LTRAB_GERAL_TP_INSC, ");
		sql.append(" '09499757000146' AS LTRAB_GERAL_NR_INSC, ");
		sql.append("  NULL  AS LTRAB_GERAL_DESC_COMP, ");
		sql.append("  CASE ");
		sql.append("  WHEN c.exercicio > TO_DATE('" + dataLimiteEsocial + "', 'dd/mm/yyyy') THEN 'N' ");
		sql.append("  ELSE 'S' ");
		sql.append(" END AS cad_ini, ");
		sql.append("  c.exercicio AS dt_exercicio, ");
		sql.append("  CASE o.id ");
		sql.append("   WHEN 33 THEN 2 ");
		sql.append("  ELSE 1 ");
		sql.append(" END AS tp_prov, ");
		sql.append(" Decode(c.contribui_inss,'S', null, ");
		sql.append(" CASE ");
		sql.append("  WHEN pl.dtinicio IS NULL THEN 'N' ");
		sql.append("  ELSE 'S' ");
		sql.append(" END) AS IND_TETO_RGPS,  ");
		sql.append(" Decode(c.contribui_inss,'S', null,   ");
		sql.append("  CASE ");
		sql.append("  WHEN ap.dataimplantacao IS NULL THEN 'N' ");
		sql.append("  ELSE 'S' ");
		sql.append(" END) AS IND_ABONO_PERM, ");
		sql.append(" CASE c.contribui_inss ");
		sql.append("  WHEN 'S' THEN null ");
		sql.append("  ELSE ap.dataimplantacao ");
		sql.append(" END  AS DT_INI_ABONO, ");
		sql.append(" Decode(o.id,33, null, o.nomenclatura)  AS NM_CARGO, ");
		sql.append(" Decode(o.id,33, null, o.cbo) AS CBO_CARGO, ");
		sql.append("  CASE o.id ");
		sql.append("  WHEN 33 THEN null ");
		sql.append(" ELSE c.exercicio ");
		sql.append(" END  AS DT_INGR_CARGO,	 ");
		sql.append(" rc.nomenclatura AS NM_FUNCAO, ");
		sql.append(" rc.cbo AS CBO_FUNCAO, ");
		sql.append("  CASE ");
		sql.append("  WHEN o.tipoacumulacao = 4 THEN 'S' ");
		sql.append("  ELSE 'N' ");
		sql.append(" END AS ACUM_CARGO, ");
		sql.append("  CASE o.id ");
		sql.append("  WHEN 33 THEN 302 ");
		sql.append("  ELSE 301 ");
		sql.append(" END AS COD_CATEG, ");
		sql.append("  CASE o.id ");
		sql.append("  WHEN 33 THEN c.rep ");
		sql.append(" ELSE c.vpad ");
		sql.append("  END AS VR_SAL_FX, ");
		sql.append("  5 AS UND_SAL_FIXO, ");
		sql.append(" CASE ");
		sql.append("  WHEN fd.FLFISICA IS NULL THEN 'N' ");
		sql.append(" ELSE 'S' ");
		sql.append(" END AS DEF_FISICA, ");
		sql.append(" CASE ");
		sql.append(" WHEN fd.FLVISUAL IS NULL THEN 'N' ");
		sql.append("  ELSE 'S' ");
		sql.append(" END AS DEF_VISUAL, ");
		sql.append(" CASE ");
		sql.append(" WHEN fd.FLAUDITIVA IS NULL THEN 'N' ");
		sql.append(" ELSE 'S' ");
		sql.append("  END AS DEF_AUDITIVA, ");
		sql.append(" CASE ");
		sql.append(" WHEN fd.FLMENTAL IS NULL THEN 'N' ");
		sql.append(" ELSE 'S' ");
		sql.append(" END AS DEF_MENTAL, ");
		sql.append(" CASE ");
		sql.append("  WHEN fd.FLFISICA IS NULL THEN 'N' ");
		sql.append("  ELSE 'S' ");
		sql.append("  END AS DEF_INTELECTUAL, ");
		sql.append(" CASE ");
		sql.append("  WHEN fd.FLREADAPTADO IS NULL THEN 'N' ");
		sql.append(" ELSE 'S' ");
		sql.append(" END AS DEF_REAB_READAP, ");
		sql.append(" CASE ");
		sql.append(" WHEN fd.FLFISICA IS NULL THEN 'N' ");
		sql.append(" ELSE 'S' ");
		sql.append(" END AS DEF_INFO_COTA, ");
		sql.append(" CASE ");
		sql.append(" WHEN fd.FLFISICA IS NULL THEN 'N' ");
		sql.append(" ELSE 'S' ");
		sql.append(" END AS DEF_OBESERVACAO ");

		sql.append(" FROM srh.tb_funcional f ");
		sql.append(" INNER JOIN srh.fp_cadastro c ON f.id = c.idfuncional ");
		sql.append(" LEFT JOIN srh.tb_abonopermanencia ap ON f.id = ap.idfuncional ");
		sql.append(" LEFT JOIN srh.tb_previdlimite pl ON f.id = pl.idfuncional ");
		sql.append(" INNER JOIN srh.tb_ocupacao o ON f.idocupacao = o.id ");
		sql.append(" INNER JOIN srh.tb_pessoal p ON f.idpessoal = p.id ");
		sql.append(" INNER JOIN srh.tb_estadocivil ec ON p.idestadocivil = ec.id ");
		sql.append(" INNER JOIN srh.esocial_pais PAIS_NASCIMENTO ON p.paisnascimento = pais_nascimento.id ");
		sql.append(" INNER JOIN srh.esocial_pais PAIS_NACIONALIDADE ON p.paisnacionalidade = pais_nacionalidade.id ");
		sql.append(" INNER JOIN srh.esocial_tipologradouro tl ON p.tipologradouro = tl.id ");
		sql.append(" INNER JOIN srh.tb_municipio m ON p.municipioendereco = m.id ");
		// sql.append(" LEFT JOIN srh.tb_representacaofuncional rf ON rf.idfuncional =
		// f.id ");
		sql.append(
				" LEFT JOIN srh.tb_representacaofuncional rf ON rf.id in  (select id from srh.tb_representacaofuncional where idfuncional = :idFuncional and fim IS NULL and rf.tiponomeacao = 1) ");
		sql.append(" LEFT JOIN srh.tb_representacaocargo rc ON rf.idrepresentacaocargo = rc.id ");
		sql.append(" LEFT JOIN srh.tb_funcionaldeficiencia fd ON fd.idfuncional = f.id	 ");
		sql.append(" WHERE f.id = :idFuncional ");
		/*
		 * if(possuiCargo) {
		 * sql.append(" and (rf.tiponomeacao = 1 AND rf.fim IS NULL) "); }
		 */
		sql.append(" ) ");

		return sql.toString();
	}

	public String getSQLEventoS2200(String nomeTabela, String filtroWhere) {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT ");		
		if(nomeTabela.equals("SRH.ESOCIAL_ADMISSAO")) {
			sql.append(" REFERENCIA AS referencia ");
		}else {
			sql.append(" ID AS referencia ");
		}
		
		sql.append(" ,CPF_TRAB AS cpfTrab");
		sql.append(" ,NM_TRAB AS nmTrab");
		sql.append(" ,SEXO AS sexo");
		sql.append(" ,RACA_COR AS racaCor");
		sql.append(" ,EST_CIV AS estCiv");
		sql.append(" ,GRAU_INSTR AS grauInstr");
		sql.append(" ,DT_NASC AS dtNascto");
		sql.append(" ,NM_SOCIAL AS nmSoc ");
		sql.append(" ,PAIS_NASC AS paisNascto ");
		sql.append(" ,PAIS_NAC AS paisNac ");
		sql.append(" ,TP_LOGRAD AS tpLograd ");
		sql.append(" ,DSC_LOGRAD AS dscLograd ");
		sql.append(" ,NR_LOGRAD AS nrLograd ");
		sql.append(" ,COMPLEMENTO AS complemento");
		sql.append(" ,BAIRRO AS bairro ");
		sql.append(" ,CEP AS cep ");
		sql.append(" ,COD_MUNIC_END AS codMunic ");
		sql.append(" ,UF_END AS uf ");
		sql.append(" ,FONE_PRINC AS fonePrinc ");
		sql.append(" ,EMAIL_PRINC AS emailPrinc ");
		sql.append(" ,MATRICULA AS matricula ");
		sql.append(" ,TP_REG_TRAB AS tpRegTrab ");
		sql.append(" ,TP_REG_PREV AS tpRegPrev ");
		sql.append(" ,CAD_INI AS cadIni ");
		sql.append(" ,TP_PROV AS tpProv ");
		sql.append(" ,DT_EXERCICIO  AS dtExercicio ");
		sql.append(" ,TP_PLAN_RP AS tpPlanRP ");
		sql.append(" ,IND_TETO_RGPS  AS indTetoRGPS");
		sql.append(" ,IND_ABONO_PERM  indAbonoPerm ");
		sql.append(" ,DT_INI_ABONO AS dtIniAbono ");
		sql.append(" ,NM_CARGO AS nmCargo ");
		sql.append(" ,CBO_CARGO AS CBOCargo ");
		sql.append(" ,DT_INGR_CARGO AS dtIngrCargo ");
		sql.append(" ,NM_FUNCAO AS nmFuncao ");
		sql.append(" ,CBO_FUNCAO AS CBOFuncao ");
		sql.append(" ,ACUM_CARGO AS acumCargo ");
		sql.append(" ,COD_CATEG AS codCateg ");
		sql.append(" ,LTRAB_GERAL_TP_INSC AS tpInsc ");
		sql.append(" ,LTRAB_GERAL_NR_INSC AS nrInsc  ");
		sql.append(" ,LTRAB_GERAL_DESC_COMP AS descComp ");
		sql.append(" ,VR_SAL_FX  AS vrSalFx ");
		sql.append(" ,UND_SAL_FIXO AS undSalFixo "); // sql.append(" ,TP_CONTR ");
		sql.append(" ,DEF_FISICA AS defFisica ");
		sql.append(" ,DEF_VISUAL AS defVisual");
		sql.append(" ,DEF_AUDITIVA AS defAuditiva ");
		sql.append(" ,DEF_MENTAL  AS defMental ");
		sql.append(" ,DEF_INTELECTUAL AS defIntelectual ");
		sql.append(" ,DEF_REAB_READAP AS reabReadap ");
		sql.append(" ,DEF_INFO_COTA AS infoCota ");
		sql.append(" ,DEF_OBESERVACAO AS observacao ");
		sql.append(" ,DSC_SAL_VAR AS dscSalVar ");
		sql.append("  FROM ");
		sql.append(nomeTabela);
		sql.append("  WHERE ");
		sql.append(filtroWhere);
		//sql.append("  FROM CONECTOR_ESOCIAL.CON_S2200_ADMISSAO_OLD  ");
		//sql.append("  WHERE CONECTOR_ESOCIAL.CON_S2200_ADMISSAO_OLD.ID =:referencia ");

		return sql.toString();
	}

	public Admissao getByIdFuncional(Long idFuncional) {
		try {
			Query query = entityManager.createQuery("SELECT a FROM Admissao a where a.funcional.id = :idFuncional");
			query.setParameter("idFuncional", idFuncional);
			return (Admissao) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public String findReciboEventoS2200(String referencia) {
		try {

			StringBuffer sql = new StringBuffer();
			sql.append(
					"select to_char(TXT_NR_RECIBO) from CONECTOR_ESOCIAL.CON_S2200_ADMISSAO_OLD s2200 inner join  ESOCIAL_API_INTEGRACAO.EST_EVENTO even on s2200.OCORRENCIA_ID = even.COD_OCORRENCIA ");
			sql.append(" where s2200.id = :referencia");
			// sql.append(" and s2200.OCORRENCIA_COD_ESTADO = 3 ");

			Query query = entityManager.createNativeQuery(sql.toString());
			query.setParameter("referencia", referencia);
			return (String) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
