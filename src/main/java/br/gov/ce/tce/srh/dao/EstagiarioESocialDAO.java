package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.EstagiarioESocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class EstagiarioESocialDAO {
	static Logger logger = Logger.getLogger(EstagiarioESocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public void excluir(EstagiarioESocial entidade) {
		entityManager.remove(entidade);
	}

	public EstagiarioESocial getEventoS2300ByEstagiario(Funcional estagiarioFuncional) {
		try {
			Query query = entityManager.createNativeQuery(getSQLEventoS2300(), EstagiarioESocial.class);
			query.setParameter("idFuncional",estagiarioFuncional.getId() );
			return (EstagiarioESocial) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public String getSQLEventoS2300() {
		StringBuffer sql = new StringBuffer();
		
		sql.append(" SELECT 0 as ID, ");
		sql.append(" tb_funcional.id||'-'||tb_ocupacao.id AS REFERENCIA, ");
		sql.append("  1     AS LTRAB_GERAL_TP_INSC, ");
		sql.append(" '09499757000146' AS LTRAB_GERAL_NR_INSC, ");
		sql.append("   NULL                  AS RETIFICAR_RECIBO, ");
		sql.append("   NULL                  AS OCORRENCIA_ID, ");
		sql.append("   NULL                  AS OCORRENCIA_COD_ESTADO, ");
		sql.append("    tb_pessoal.cpf as CPF_TRAB, ");
		sql.append(" 	tb_pessoal.datanascimento AS dt_nasc, ");
		sql.append("    tb_pessoal.nome as NM_TRAB, ");
		sql.append("    tb_funcional.id as IDFUNCIONAL, ");
		sql.append("    tb_pessoal.sexo as SEXO, ");
		sql.append("    tb_pessoal.idraca as RACA_COR, ");
		sql.append("    tb_estadocivil.codigoesocial as EST_CIV, ");
		sql.append("    tb_pessoal.idescolaridade as GRAU_INSTR, ");
		sql.append("    tb_pessoal.nomesocial as NM_SOC, ");
		sql.append("    pais_nascimento.codigo as PAIS_NASC, ");
		sql.append("    pais_nacionalidade.codigo as PAIS_NAC, ");
		sql.append("    esocial_tipologradouro.codigo as TP_LOGRAD, ");
		sql.append("    tb_pessoal.endereco as DSC_LOGRAD, ");
		sql.append("    tb_pessoal.numero as NR_LOGRAD, ");
		sql.append("    tb_pessoal.complemento as COMPLEMENTO, ");
		sql.append("    tb_pessoal.bairro as BAIRRO, ");
		sql.append("    TRIM(' ' from tb_pessoal.cep) as CEP, ");
		sql.append("    tb_municipio.cod_municipio as COD_MUNIC_END, ");
		sql.append("    tb_municipio.uf as UF_END, ");
		sql.append("    tb_pessoal.email as EMAIL_PRINC, ");
		sql.append("    '0'||tb_funcional.matricula as MATRICULA, ");
		sql.append("    tb_funcional.dataexercicio as DT_INICIO, ");
		sql.append("    2 as NIV_ESTAGIO, ");
		sql.append("    tb_ocupacao.nomenclatura as NM_CARGO, ");
		sql.append("    tb_ocupacao.cbo as CBO_CARGO, ");
		sql.append("    fp_cadastro.vpad as VR_SAL_FX, ");
		sql.append("    CASE WHEN fd.FLFISICA IS NULL THEN 'N' ELSE 'S' END AS DEF_FISICA, ");
		sql.append("	CASE WHEN fd.FLVISUAL IS NULL THEN 'N' ELSE 'S' END AS DEF_VISUAL, ");
		sql.append("	CASE WHEN fd.FLAUDITIVA IS NULL THEN 'N' ELSE 'S' END AS DEF_AUDITIVA, ");
		sql.append("	CASE WHEN fd.FLMENTAL IS NULL THEN 'N' ELSE 'S' END AS DEF_MENTAL, ");
		sql.append("	CASE WHEN fd.FLFISICA IS NULL THEN 'N' ELSE 'S' END AS DEF_INTELECTUAL, ");
		sql.append("	CASE WHEN fd.FLREADAPTADO IS NULL THEN 'N' ELSE 'S' END AS DEF_REAB_READAP, ");
		sql.append("	TRIM(' ' from tb_pessoal.TELEFONE) AS FONE_PRINC, ");
		sql.append("	CASE WHEN fp_cadastro.exercicio > TO_DATE('22/11/2021', 'dd/mm/yyyy') THEN 'N' ELSE 'S' END AS CAD_INI, ");
		sql.append("	901 AS COD_CATEG, ");
		sql.append("	5 AS UND_SAL_FIXO, ");
		sql.append("	'N' AS NAT_ESTAGIO, ");
		sql.append("	'07954514042120' AS CNPJ_INST_ENSINO,  ");
		sql.append("	tb_funcional.datasaida AS DT_TERMINO, ");
		sql.append("	ADD_MONTHS(tb_funcional.dataexercicio,12) AS DT_PREV_TERM ");
		sql.append("FROM   srh.tb_funcional ");
		sql.append("       INNER JOIN srh.fp_cadastro ");
		sql.append("               ON srh.tb_funcional.id = srh.fp_cadastro.idfuncional ");
		sql.append("       LEFT JOIN srh.tb_abonopermanencia ");
		sql.append("              ON srh.tb_funcional.id = srh.tb_abonopermanencia.idfuncional ");
		sql.append("       LEFT JOIN srh.tb_previdlimite ");
		sql.append("              ON srh.tb_funcional.id = srh.tb_previdlimite.idfuncional ");
		sql.append("	   LEFT JOIN srh.tb_funcionaldeficiencia fd ");
		sql.append("	   		  ON fd.idfuncional = tb_funcional.ID ");
		sql.append("       INNER JOIN srh.tb_ocupacao ");
		sql.append("               ON srh.tb_funcional.idocupacao = srh.tb_ocupacao.id ");
		sql.append("       INNER JOIN srh.tb_pessoal ");
		sql.append("               ON srh.tb_funcional.idpessoal = srh.tb_pessoal.id ");
		sql.append("       INNER JOIN srh.tb_estadocivil ");
		sql.append("               ON srh.tb_pessoal.idestadocivil = srh.tb_estadocivil.id ");
		sql.append("       INNER JOIN srh.esocial_pais PAIS_NASCIMENTO ");
		sql.append("               ON srh.tb_pessoal.paisnascimento = pais_nascimento.id ");
		sql.append("       INNER JOIN srh.esocial_pais PAIS_NACIONALIDADE ");
		sql.append("               ON srh.tb_pessoal.paisnacionalidade = pais_nacionalidade.id ");
		sql.append("       INNER JOIN srh.esocial_tipologradouro ");
		sql.append("               ON srh.tb_pessoal.tipologradouro = srh.esocial_tipologradouro.id ");
		sql.append("       INNER JOIN srh.tb_municipio ");
		sql.append("               ON srh.tb_pessoal.municipioendereco = srh.tb_municipio.id ");
		sql.append("WHERE  tb_funcional.id = :idFuncional");

		
		
		return sql.toString();
	}

	@Transactional
	public EstagiarioESocial salvar(EstagiarioESocial entidade) {
		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		entityManager.detach(entidade);
		
		return entityManager.merge(entidade);
	}
	
	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from EstagiarioESocial e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public int count(String nome, String cpf) {
		StringBuffer sql = new StringBuffer();

		sql.append(" Select count(e) FROM EstagiarioESocial e inner join e.funcional f WHERE 1=1 ");

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
	public List<EstagiarioESocial> search(String nome, String cpf, Integer first, Integer rows) {
		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT e FROM EstagiarioESocial e inner join fetch e.funcional f WHERE 1=1 ");

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
}
