package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Beneficio;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.service.AmbienteService;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class BeneficioEsocialDAO {

	static Logger logger = Logger.getLogger(BeneficioEsocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	@Autowired
	private AmbienteService ambienteService;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Long getMaxId() {
		Query query = entityManager.createQuery("Select max(e.id) from Beneficio e ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public Beneficio salvar(Beneficio entidade) {

		if (entidade.getId() == null || entidade.getId().equals(0l)) {
			entidade.setId(getMaxId());
		}

		return entityManager.merge(entidade);
	}

	public void excluir(Beneficio entidade) {
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public Beneficio getById(Long id) {
		return entityManager.find(Beneficio.class, id);
	}
	
	public int count(String nome, String cpf) {
		
		StringBuffer sql = new StringBuffer();

		sql.append(" Select count(b) FROM Beneficio b inner join b.funcional f WHERE 1=1 ");

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
	public List<Beneficio> search(String nome, String cpf, Integer first, Integer rows) {

		StringBuffer sql = new StringBuffer();

		sql.append("  SELECT b FROM Beneficio b inner join fetch b.funcional f WHERE 1=1 ");

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

	public Beneficio getEventoS2410ByServidor(Funcional servidorFuncional) {
		try {
			Query query = entityManager.createNativeQuery(getSQLEventoS2410(), Beneficio.class);
			query.setParameter("idFuncional",servidorFuncional.getId() );
			return (Beneficio) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}

	}	
	
	public String getSQLEventoS2410() {
		
		
		String dataLimiteEsocial="";
		if(ambienteService.ambiente().isProducao()) {
			dataLimiteEsocial = "21/11/2021";
		}
		else {
			dataLimiteEsocial = "31/04/2020";
		}
		StringBuffer sql = new StringBuffer();

		sql.append(" SELECT    ");
		sql.append("   0 as id, ");
		sql.append("  f.id idfuncional,  ");
		sql.append("  f.id||'-'||o.id AS referencia,  ");
		sql.append("  p.cpf as CPF_BENEF, ");
		sql.append("  f.matricula AS MATRICULA, ");
		sql.append(" NULL AS CNPJ_ORIGEM, ");
		sql.append(" CASE  ");
		sql.append(" WHEN a.datainiciobeneficio  > TO_DATE('"+dataLimiteEsocial+"', 'dd/mm/yyyy') THEN 'N'  ");
		sql.append(" ELSE 'S' ");
		sql.append(" END AS CAD_INI, ");
		sql.append(" NULL AS INC_SIT_BENEF, ");
		sql.append(" NULL AS NR_BENEFICIO, ");
		
		//sql.append(" a.datainiciobeneficio AS DT_INI_BENEFICIO, ");
		
		sql.append(" CASE  ");
		sql.append(" WHEN a.datainiciobeneficio  > TO_DATE('"+dataLimiteEsocial+"', 'dd/mm/yyyy') THEN a.datainiciobeneficio  ");
		sql.append(" ELSE TO_DATE('"+dataLimiteEsocial+"', 'dd/mm/yyyy') ");
		sql.append(" END AS DT_INI_BENEFICIO, ");		
		sql.append("  a.datapublicacaoato AS DT_PUBLIC, ");
		sql.append("  '0101' AS TP_BENEFICIO, ");
		sql.append(" 2 AS TP_PLAN_RP, ");
		sql.append("  NULL AS DSC, ");
		sql.append("  CASE  ");
		sql.append("  WHEN a.datainiciobeneficio > TO_DATE('"+dataLimiteEsocial+"', 'dd/mm/yyyy') THEN 'N' ");
		sql.append("  ELSE NULL ");
		sql.append("  END AS IND_DEC_JUD, ");
		sql.append(" NULL AS TP_PEN_MORTE, ");
		sql.append("  NULL AS CPF_INST, ");
		sql.append("  NULL AS DT_INST, ");
		sql.append("  NULL AS CNPJ_ORGAO_ANT, ");
		sql.append("  NULL AS NR_BENEFICIO_ANT_SUC, ");
		sql.append("  NULL AS DT_TRANSF, ");
		sql.append("  NULL AS OBSERVACAO_SUC, ");
		sql.append("  NULL AS CPF_ANT, ");
		sql.append("  NULL AS NR_BENEFICIO_ANT_MUD, ");
		sql.append("   NULL AS DT_ALT_CPF, ");
		sql.append("  NULL AS OBSERVACAO_MUD, ");
		sql.append("  NULL AS DT_TERM_BENEFICIO, ");
		sql.append("  NULL AS MTV_TERMINO ");
		   
		sql.append(" FROM srh.tb_funcional f ");
		sql.append(" INNER JOIN srh.fp_cadastro c ON f.id = c.idfuncional ");
		sql.append(" INNER JOIN srh.tb_pessoal p ON f.idpessoal = p.id ");
		sql.append(" INNER JOIN srh.tb_ocupacao o ON f.idocupacao = o.id ");
		sql.append(" INNER JOIN srh.tb_estadocivil ec ON p.idestadocivil = ec.id ");
		sql.append(" INNER JOIN srh.esocial_pais PAIS_NASCIMENTO ON p.paisnascimento = pais_nascimento.id ");
		sql.append(" INNER JOIN srh.esocial_pais PAIS_NACIONALIDADE ON p.paisnacionalidade = pais_nacionalidade.id ");
		sql.append(" INNER JOIN srh.tb_aposentadoria a ON f.idaposentadoria = a.id ");
		sql.append(" INNER JOIN srh.esocial_tipologradouro tpl ON p.tipologradouro = tpl.id ");
		sql.append(" INNER JOIN srh.tb_municipio m ON p.municipioendereco = m.id ");
		sql.append(" WHERE f.id = :idFuncional ");
	    return sql.toString();
	}

}
