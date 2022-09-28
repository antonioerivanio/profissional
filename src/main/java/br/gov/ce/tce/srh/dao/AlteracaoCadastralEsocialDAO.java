package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.AlteracaoCadastral;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.service.AmbienteService;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class AlteracaoCadastralEsocialDAO {

	static Logger logger = Logger.getLogger(AlteracaoCadastralEsocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private AmbienteService ambienteService;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from AlteracaoCadastral e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public AlteracaoCadastral salvar(AlteracaoCadastral entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(AlteracaoCadastral entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public AlteracaoCadastral getById(Long id) {
		return entityManager.find(AlteracaoCadastral.class, id);
	}
	
	public int count(String nome, String cpf) {
		
		StringBuffer sql = new StringBuffer();

		sql.append(" Select count(a) FROM AlteracaoCadastral a inner join a.funcional f WHERE 1=1 ");

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
	public List<AlteracaoCadastral> search(String nome, String cpf, Integer first, Integer rows) {

		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT e FROM AlteracaoCadastral e inner join fetch e.funcional f WHERE 1=1 ");

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

	public AlteracaoCadastral getEventoS2205ByServidor(Funcional servidorFuncional, boolean possuiCargo) {
		try {
			Query query = entityManager.createNativeQuery(getSQLEventoS2205(possuiCargo), AlteracaoCadastral.class);
			query.setParameter("idFuncional",servidorFuncional.getId() );
			return (AlteracaoCadastral) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}	
	
	public String getSQLEventoS2205(boolean possuiCargo) {
		StringBuffer sql = new StringBuffer();
		String dataLimiteEsocial="";
		
		if(ambienteService.ambiente().isProducao()) {
			dataLimiteEsocial = "21/11/2021";
		}
		else {
			dataLimiteEsocial = "28/02/2017";
		}

		sql.append(" SELECT ");
		sql.append(" * ");
		sql.append(" FROM ");
		sql.append(" ( ");
		sql.append("  SELECT ");
		sql.append(" f.id-10000 as id, ");
		sql.append( "f.id||'-'||o.id AS referencia, ");	
		sql.append(" f.id AS idfuncional, ");
		sql.append("  1     AS TP_INSC, ");
		sql.append(" '09499757000146' AS NR_INSC, ");
		sql.append(" p.nome AS nm_trab, ");
		sql.append(" p.cpf AS cpf_trab, ");
		sql.append("  p.sexo AS sexo, ");
		sql.append("  p.idraca AS raca_cor, ");
		sql.append("  ec.codigoesocial AS est_civ, ");
		sql.append("  p.idescolaridade AS grau_instr, ");
		sql.append(" p.nomesocial AS NM_SOCIAL, ");		
		sql.append(" pais_nacionalidade.codigo AS pais_nac, ");
		sql.append("  tl.codigo AS tp_lograd, ");
		sql.append("  p.endereco AS dsc_lograd, ");
		sql.append("  p.numero AS nr_lograd, ");
		sql.append("  p.complemento AS complemento, ");
		sql.append("  p.bairro AS bairro, ");
		sql.append(" TRIM (p.cep) AS cep, ");
		sql.append(" m.cod_municipio AS cod_munic_end, ");
		sql.append("  m.uf AS uf, ");
		sql.append("  TRIM (p.celular) AS fone_princ, ");
		sql.append("  TRIM (p.email) AS email_princ, ");
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
		//sql.append(" LEFT JOIN srh.tb_representacaofuncional rf ON rf.idfuncional = f.id ");
		sql.append(" LEFT JOIN srh.tb_representacaofuncional rf ON rf.id in  (select id from srh.tb_representacaofuncional where idfuncional = :idFuncional and fim IS NULL and rf.tiponomeacao = 1) ");
		sql.append(" LEFT JOIN srh.tb_representacaocargo rc ON rf.idrepresentacaocargo = rc.id ");
		sql.append(" LEFT JOIN srh.tb_funcionaldeficiencia fd ON fd.idfuncional = f.id	 ");		
		sql.append(" WHERE f.id = :idFuncional ");
        /*
         * if(possuiCargo) { sql.append(" and (rf.tiponomeacao = 1 AND rf.fim IS NULL) "); }
         */
	    sql.append(" ) ");
	    
	    return sql.toString();
	}

	public AlteracaoCadastral getByIdFuncional(Long idFuncional) {
		try {
			Query query = entityManager.createQuery("SELECT a FROM AlteracaoCadastral a where a.funcional.id = :idFuncional");
			query.setParameter("idFuncional", idFuncional);
			return (AlteracaoCadastral) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public String findReciboEventoS2205(String referencia) {
		try {
			
			StringBuffer sql = new StringBuffer();
			sql.append("select to_char(TXT_NR_RECIBO) from CONECTOR_ESOCIAL.CON_S2205_AlteracaoCadastral_OLD s2205 inner join  ESOCIAL_API_INTEGRACAO.EST_EVENTO even on s2205.OCORRENCIA_ID = even.COD_OCORRENCIA ");
			sql.append(" where s2205.id = :referencia");
			//sql.append(" and s2200.OCORRENCIA_COD_ESTADO = 3 ");
			
			Query query = entityManager.createNativeQuery(sql.toString());
			query.setParameter("referencia", referencia);
			return (String) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

}
