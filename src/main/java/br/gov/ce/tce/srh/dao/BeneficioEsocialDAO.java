package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Beneficio;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class BeneficioEsocialDAO {

	static Logger logger = Logger.getLogger(BeneficioEsocialDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
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
		StringBuffer sql = new StringBuffer();
	
		sql.append(" SELECT 0 as id, ");
		sql.append("  f.id idfuncional, ");
		sql.append(" f.id||'-'||o.id AS referencia, ");
		sql.append(" p.nome AS NM_BENEFIC, ");
		sql.append(" p.cpf AS CPF_BENEF, ");
		sql.append("  p.sexo, ");
		sql.append(" p.idraca AS RACA_COR, ");
		sql.append(" ec.codigoesocial AS EST_CIV, ");
		sql.append(" p.datanascimento as DT_NASCTO, ");
		sql.append(" tl.codigo as TP_LOGRAD, ");
		sql.append(" p.endereco AS dsc_lograd, ");
		sql.append(" p.numero as NR_LOGRAD, ");
		sql.append(" p.complemento, ");
		sql.append(" p.bairro, ");
		sql.append(" TRIM(' ' from p.cep) as CEP, ");
		sql.append(" m.cod_municipio AS COD_MUNIC_END, ");
		sql.append(" m.uf as UF_END, ");
		sql.append(" c.atoaposentadoria as DT_INICIO, ");
		sql.append(" 'N' AS INC_FIS_MEN, ");
		sql.append("  NULL as DT_INC_FIS_MEN, ");
		sql.append("  NULL   AS BAIRRO_EX, ");
		sql.append(" NULL   AS COD_POSTAL, ");
		sql.append(" NULL   AS NM_CID, ");
		sql.append("  NULL   AS DSC_LOGRAD_EX, ");
		sql.append("  NULL   AS NRLOGRAD_EX, ");
		sql.append("  NULL   AS COMPLEMENTO_EX, ");
		sql.append(" NULL   AS PAIS_RESID ");
		sql.append(" FROM srh.tb_funcional f ");
		sql.append(" INNER JOIN srh.fp_cadastro c ON f.id = c.idfuncional ");
		sql.append(" INNER JOIN srh.tb_pessoal p ON f.idpessoal = p.id ");
		sql.append(" INNER JOIN srh.tb_ocupacao o ON f.idocupacao = o.id ");
		sql.append(" INNER JOIN srh.tb_estadocivil ec ON p.idestadocivil = ec.id ");
		sql.append(" INNER JOIN srh.esocial_pais pn ON p.paisnascimento = pn.id ");
		sql.append(" INNER JOIN srh.esocial_pais pnd ON p.paisnacionalidade = pnd.id ");
		sql.append(" INNER JOIN srh.esocial_tipologradouro tl ON p.tipologradouro = tl.id ");
		sql.append(" INNER JOIN srh.tb_municipio m ON p.municipioendereco = m.id ");
		sql.append(" WHERE f.id = :idFuncional ");
			    
	    return sql.toString();
	}

}
