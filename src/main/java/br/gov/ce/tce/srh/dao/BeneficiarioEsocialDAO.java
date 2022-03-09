package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Beneficiario;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class BeneficiarioEsocialDAO {

	static Logger logger = Logger.getLogger(BeneficiarioEsocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from Beneficiario e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public Beneficiario salvar(Beneficiario entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(Beneficiario entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public Beneficiario getById(Long id) {
		return entityManager.find(Beneficiario.class, id);
	}
	
	public int count(String nome, String cpf) {
		
		StringBuffer sql = new StringBuffer();

		sql.append(" Select count(b) FROM Beneficiario b inner join b.funcional f WHERE 1=1 ");

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
			query.setParameter("cpf", SRHUtils.removerMascara( cpf ));
		}
		return ((Long) query.getSingleResult()).intValue();
	}
	
	@SuppressWarnings("unchecked")
	public List<Beneficiario> search(String nome, String cpf, Integer first, Integer rows) {

		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT b FROM Beneficiario b inner join fetch b.funcional f WHERE 1=1 ");

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
			query.setParameter("cpf", SRHUtils.removerMascara( cpf ));
		}
		if (first != null && first >= 0)
			query.setFirstResult(first);
		if (rows != null && rows > 0)
			query.setMaxResults(rows);

		return query.getResultList();
	}

	public Beneficiario getEventoS2400ByServidor(Funcional servidorFuncional) {
		try {
			Query query = entityManager.createNativeQuery(getSQLEventoS2400(), Beneficiario.class);
			query.setParameter("idFuncional",servidorFuncional.getId() );
			return (Beneficiario) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}	
	
	public String getSQLEventoS2400() {
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT ");
		sql.append(" * ");
		sql.append(" FROM ");
		sql.append(" ( ");
		sql.append("  SELECT ");
		sql.append(" 0 as id, ");
		sql.append( "f.id||'-'||o.id AS referencia, ");	
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
		sql.append(" Decode(c.contribui_inss,'S', null, 2) AS TP_PLAN_RP, ");
		sql.append(" NULL  AS DSC_SAL_VAR, ");
		sql.append(" NULL  AS TP_CONTR, ");
		sql.append("  1     AS LTRAB_GERAL_TP_INSC, ");
		sql.append(" '09499757000146' AS LTRAB_GERAL_NR_INSC, ");
		sql.append("  NULL  AS LTRAB_GERAL_DESC_COMP, ");
		sql.append("  CASE ");
		sql.append("  WHEN c.exercicio > TO_DATE('22/11/2021', 'dd/mm/yyyy') THEN 'N' ");
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
		sql.append(" LEFT JOIN srh.tb_representacaofuncional rf ON rf.idfuncional = f.id ");
		sql.append(" LEFT JOIN srh.tb_representacaocargo rc ON rf.idrepresentacaocargo = rc.id ");
		sql.append(" LEFT JOIN srh.tb_funcionaldeficiencia fd ON fd.idfuncional = f.id	 ");		
		sql.append(" WHERE f.id = :idFuncional ");
		
	    sql.append(" ) ");
	    
	    return sql.toString();
	}

}
