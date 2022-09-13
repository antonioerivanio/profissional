package br.gov.ce.tce.srh.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.CadastroPrestador;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.sca.domain.Usuario;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class FuncionalDAO {

	static Logger logger = Logger.getLogger(FuncionalDAO.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	@Autowired
	private PessoalService pessoalService;

	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	private Long getMaxId() {
		Query query = entityManager.createQuery("Select max(l.id) from Funcional l ");
		return query.getSingleResult() == null ? 1 : (Long) query.getSingleResult() + 1;
	}

	public Funcional getById(Long id) {
		return entityManager.find(Funcional.class, id);
	}

	/**
	 * Método responsável em pegar a última matrícula cadastrada e verificar qual é a próxima matrícula válida 
	 * utilizando-se para isso o validador da matrícula
	 * 
	 * @return
	 * 
	 */		

	public String getMaxMatricula() {

		Query query = entityManager.createQuery("Select max(f.matricula) from Funcional f ");
		String matricula = (String) query.getSingleResult();

		if (matricula != null) {

			do {

				Long mat = Long.parseLong(matricula.substring(0,4)+matricula.substring(5,6));
				
				mat = mat + 1;				

				//adicionando quantidades de zeros restante para comparar a matrícula no padrão NNNN-D onde N é um número e D é o dígito verificador
				matricula = String.format("%5s", mat).replace(' ', '0');
				
				//adicionando separador do dígito verificador
				matricula = matricula.substring(0,4)+"-"+matricula.substring(4,5);
				
				if( SRHUtils.validarMatricula(matricula)) {
					return matricula;
				}

			} while(true);
		}
		return null;
	}
	
	public Funcional salvar(Funcional entidade) {

		if (entidade == null || entidade.getId() == null) {
			entidade.setId(getMaxId());
		}

		entidade.setNome(entidade.getPessoal().getNome());
		entidade.setNomeCompleto(entidade.getPessoal().getNomeCompleto());
		entidade.setNomePesquisa(entidade.getPessoal().getNomePesquisa());

		return entityManager.merge(entidade);
	}

	public void excluir(Funcional entidade) {		
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}

	public int count(Long pessoal, String orderBy) {
		Query query = entityManager.createQuery("SELECT count (f) "
												+ "FROM Funcional f "
												+ "WHERE f.pessoal.id = :idPessoal "
												+ "ORDER BY f.id " + orderBy);
		query.setParameter("idPessoal", pessoal);
		return ((Long) query.getSingleResult()).intValue();
	}

	public List<Funcional> search(Long pessoal, String orderBy, int first, int rows) {
		TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.tipoMovimentoEntrada, f.ocupacao, f.exercicio, f.saida) "
												+ "FROM Funcional f "
												+ "WHERE f.pessoal.id = :idPessoal "
												+ "ORDER BY f.id " + orderBy, Funcional.class);
		query.setParameter("idPessoal", pessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	public List<Funcional> searchForReclassificacao(Long pessoal, String orderBy, int first, int rows) {
		TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.tipoMovimentoEntrada, f.ocupacao, f.exercicio, f.saida, f.posse) "
												+ "FROM Funcional f "
												+ "WHERE f.pessoal.id = :idPessoal "
												+ "ORDER BY f.id " + orderBy, Funcional.class);
		query.setParameter("idPessoal", pessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}

	public Funcional getByPessoaAtivo(Long idPessoa) {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT f FROM Funcional f ");
			sql.append("    left join fetch     f.setor ");
			sql.append("    left join fetch     f.classeReferencia ");
			sql.append("    left join fetch     f.especialidadeCargo ");
			sql.append("    left join fetch     f.tipoMovimentoEntrada ");
			sql.append("    left join fetch     f.tipoMovimentoSaida ");
			sql.append("    left join fetch     f.cbo ");
			sql.append("    left join fetch     f.folha ");
			sql.append("    left join fetch     f.tipoPublicacaoNomeacao ");
			sql.append("    left join fetch     f.tipoPublicacaoSaida ");
			sql.append("    left join fetch     f.situacao ");
			sql.append("    left join fetch     f.vinculo ");
			sql.append("    WHERE f.pessoal.id = :id AND f.saida IS NULL AND f.status != 5 ");
			TypedQuery<Funcional> query = entityManager.createQuery(sql.toString(), Funcional.class);
			query.setParameter("id", idPessoa);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Funcional getByPessoaAtivoFp(Long idPessoal) {
		try {
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT f "
													+ "FROM Funcional f "
													+ "WHERE f.pessoal.id = :idPessoal "
													+ "AND f.saida is null "
													+ "AND f.status != 5 "
													+ "AND f.atipoFp = 0 "
													+ "AND (f.situacao.id = 5 OR f.situacao.id = 7) "
													+ "ORDER BY f.id", Funcional.class);
			query.setParameter("idPessoal", idPessoal);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Funcional> findAllAtivos() {
		try {
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal) "
					+ "FROM Funcional f "
					+ "WHERE f.saida IS NULL "
					+ "AND f.status = 1 "
					+ "AND f.setor.id != 113 "
					+ "ORDER BY f.pessoal.nome", Funcional.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Funcional getByMatriculaAtivo(String matricula) {
		try {
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT f "
													+ "FROM Funcional f "
													+ "WHERE upper( f.matricula ) = :matricula "
													+ "AND f.saida IS NULL "
													+ "AND f.status != 5 ", Funcional.class);
			query.setParameter("matricula", matricula);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public Funcional getMatriculaAndNomeByCpf(String cpf) {

		TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nomeCompleto, f.setor, f.ocupacao, f.exercicio) "
												+ "FROM Funcional f "
												+ "WHERE f.pessoal.cpf = :cpf "
												+ "ORDER BY f.id desc", Funcional.class);
		query.setParameter("cpf", SRHUtils.removerMascara( cpf ));
		List<Funcional> lista = query.getResultList(); 
		if (lista.size() > 0)
			return lista.get(0);
		

		return null;
	}

	public List<Funcional> getMatriculaAndNomeByCpfList(String cpf) {

		TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nomeCompleto, f.setor, f.ocupacao, f.exercicio) "
												+ "FROM Funcional f "
												+ "WHERE f.pessoal.cpf = :cpf "
												+ "ORDER BY f.id desc", Funcional.class);
		query.setParameter("cpf", SRHUtils.removerMascara( cpf ));
		
		List<Funcional> resultList = query.getResultList();
		
		Map<String, Funcional> returnMap = new HashMap<String, Funcional>();
		
		for (Funcional funcional : resultList) {
			if(!returnMap.containsKey(funcional.getMatricula()))
				returnMap.put(funcional.getMatricula(), funcional);
		}		
		
		return new ArrayList<Funcional>(returnMap.values());
		
	}

	public Funcional getMatriculaAndNomeByCpfAtiva(String cpf) {

		TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nomeCompleto, f.setor, f.ocupacao, f.exercicio) "
												+ "FROM Funcional f "
												+ "WHERE f.pessoal.cpf = :cpf "
												+ "AND f.saida IS NULL "
												+ "AND f.status != 5 "
												+ "ORDER BY f.id desc", Funcional.class);
		query.setParameter("cpf", SRHUtils.removerMascara( cpf ) );
		List<Funcional> lista = query.getResultList(); 
		if (lista.size() > 0)
			return lista.get(0);
		

		return null;
	}

	public Funcional getCpfAndNomeByMatricula(String matricula) {
		try {
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nomeCompleto, f.setor, f.ocupacao, f.exercicio) "
													+ "FROM Funcional f "
													+ "WHERE upper( f.matricula ) = :matricula "
													+ "ORDER BY f.id desc", Funcional.class);
			query.setParameter("matricula", matricula);
			List<Funcional> lista = query.getResultList(); 
			if (lista.size() > 0)
				return lista.get(0);
			
			return null;
		} catch (NoResultException e) {
			return null;
		}
	}

	public Funcional getCpfAndNomeByMatriculaAtiva(String matricula) {
		try {
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nomeCompleto, f.setor, f.ocupacao, f.exercicio) "
													+ "FROM Funcional f "
													+ "WHERE upper( f.matricula ) = :matricula "
													+ "AND f.saida IS NULL "
													+ "AND f.status != 5 "
													+ "ORDER BY f.id desc", Funcional.class);
			query.setParameter("matricula", matricula);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}	
	
	
	public List<Funcional> findByNome(String nome) {
		TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.setor, f.ocupacao, f.vinculo) "
																+ "FROM Funcional f "
																+ "WHERE upper( f.nomePesquisa ) like :nome "
																+ "AND f.saida IS NULL "
																+ "AND f.status != 5 "
																+ "ORDER BY f.id desc", Funcional.class);
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		
		return query.getResultList();
	}	
	
		
	public List<Funcional> findAllByNome(String nome) {
		TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.setor) "
																+ "FROM Funcional f "
																+ "WHERE upper( f.nomePesquisa ) like :nome "
																+ "ORDER BY f.id desc", Funcional.class);
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");

		List<Funcional> resultList = query.getResultList();
		
		Map<String, Funcional> returnMap = new HashMap<String, Funcional>();
		
		for (Funcional funcional : resultList) {
			if(!returnMap.containsKey(funcional.getMatricula()))
				returnMap.put(funcional.getMatricula(), funcional);
		}
		
		return new ArrayList<Funcional>(returnMap.values());
	}
	
	public List<Funcional> findByUsuariologado(Usuario usuarioLogado) {
	  try {
		return findByNome(pessoalService.getByCpf(SRHUtils.removerMascara(usuarioLogado.getCpf())).getNomeCompleto());
	  }catch (Exception e) {
        throw new NullPointerException("Ops!. Usuario não tem permissão"); 
      }
	}
	
	public List<Funcional> findByPessoal(Long idPessoal, String orderBy) {
		TypedQuery<Funcional> query = entityManager.createQuery("SELECT f FROM Funcional f WHERE f.pessoal.id = :idPessoal order by f.id " + orderBy, Funcional.class);
		query.setParameter("idPessoal", idPessoal);
		return query.getResultList();
	}
	
	public int countResponsavelSetor(long idFuncional, Long idSetor) {
		
		try {			
			String queryString = "SELECT count(*) FROM srh.TB_RESPONSAVELSETOR WHERE FLCHEFEATIVO = 1 AND (IDCHEFE = :idFuncional OR IDCHEFEIMEDIATO = :idFuncional)"; 
			
			if(idSetor != null) {
				queryString += " AND IDSETOR = :idSetor";
			}
			
			Query query = entityManager.createNativeQuery(queryString);
			
			query.setParameter("idFuncional", idFuncional);
			
			if(idSetor != null) {
				query.setParameter("idSetor", idSetor);
			}
			
			return ((BigDecimal) query.getSingleResult()).intValue();
		} catch (Exception e) {
			return 0;
		}
	}
	/*** 
	 * Ocupação 14 - Estagiário Nível Médio 
	 * Ocupação 15 - Estagiário Nível Universitário
	 * @return
	 */
	public List<Funcional> findServidoresEvento2200() {
		try {
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nome) "
					+ "FROM Funcional f "
					+ "WHERE f.saida IS NULL "
					+ "AND f.status = 1 "
					+ "AND f.ocupacao.id not in (14,15) "
					+ "AND f.id  NOT IN (SELECT a.funcional.id FROM Admissao a) "
					+ "ORDER BY f.nome", Funcional.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Funcional> findServidoresEvento2299() {
	     TypedQuery<Funcional> query = entityManager.createQuery("SELECT DISTINCT new Funcional(f.id, f.matricula, f.pessoal, f.nome) "
	                               + "FROM Funcional f "
	                               + "WHERE "
	                               //+ " f.status < 3 " //pegar todos que sao 1,2
	                               + "  (f.id IN (SELECT a.funcional.id FROM Admissao a) " //2200
	                               + "  AND f.saida > to_date('21/11/2021', 'dd/mm/yyyy') ) "
	                               + " OR (f.id IN (SELECT a.funcional.id FROM Aposentadoria a) AND f.id NOT IN (SELECT b.funcional.id FROM Beneficiario b)) "
	                               + "   AND f.id  NOT IN (SELECT a.funcional.id FROM Desligamento a) "
	                               + "ORDER BY f.nome", Funcional.class);
	     return query.getResultList();
	}
	
	public List<Funcional> findServidoresEvento2399() {
         TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nome) "
                               + "FROM Funcional f "
                               + "WHERE "
                              // + " f.status < 3 " //pegar todos que sao 1,2
                               + "  f.id IN (SELECT a.funcional.id FROM EstagiarioESocial a) "
                               + " AND f.saida > to_date('21/11/2021', 'dd/mm/yyyy') " 
                               + " AND f.id  NOT IN (SELECT a.funcional.id FROM TerminoVinculo a) "
                               + "ORDER BY f.nome", Funcional.class);
         return query.getResultList();
    }
	
	@SuppressWarnings("unchecked")
	public List<Funcional> findServidoresEvento2230() {
		try {		  
		  StringBuffer sql =  new StringBuffer(" SELECT distinct f.* FROM tb_licenca l INNER JOIN tb_tipolicenca tl on l.idtipolicenca = tl.id ");
		  sql.append(" INNER JOIN tb_pessoal p on l.idpessoal = p.id ");
		  sql.append(" INNER JOIN tb_funcional f on f.idpessoal = p.id ");
		  sql.append(" WHERE fim > to_date('21/08/2022', 'dd/mm/yyyy') ");
		  sql.append(" AND to_char(inicio, 'dd/mm/yyyy') < to_char(sysdate, 'dd/mm/yyyy') ");
		  sql.append(" AND tl.codigoesocial is not null ");
		  sql.append(" AND f.datasaida is null ");
		  sql.append(" AND f.id  NOT IN (SELECT e.IDFUNCIONAL FROM ESOCIAL_AFASTAMENTO e  where e.dt_ini_afast = l.inicio) ");
		  sql.append(" order by f.nome ");
		  Query query = entityManager.createNativeQuery(sql.toString(), Funcional.class);
		  return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Funcional> findServidoresEventoAuxilioSaude() {
	  try {     
	            TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nome) "
	                    + "FROM Funcional f "
	                    + "WHERE f.saida IS NULL "
	                    + "AND f.status = 1 "
	                    + " OR f.ocupacao.id in (14,15)"	                   
	                    + "ORDER BY f.nome", Funcional.class);
	            return query.getResultList();
	        } catch (NoResultException e) {
	            return null;
       }
	}

	public List<Funcional> findBeneficiariosEvento2400() {	
		
		try {
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nome) "
					+ "FROM Funcional f "
					+ "WHERE f.status = 5 "				 
					+ "AND f.pessoal.id  IN (SELECT a.funcional.pessoal.id FROM Aposentadoria a) "
					+ "AND f.id  NOT IN (SELECT b.funcional.id FROM Beneficiario b) "
					+ "AND  f.saida is null "
					+ "ORDER BY f.nome", Funcional.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Funcional> findEstagiariosEventos2300() {
		try {
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nome) "
					+ "FROM Funcional f "
					+ "WHERE f.saida IS NULL "
					+ "AND f.status = 2 "
					+ "AND f.ocupacao.id in (14,15) "
					+ "AND f.id  NOT IN (SELECT e.funcional.id FROM EstagiarioESocial e) "
					+ "ORDER BY f.nome", Funcional.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Funcional> findBeneficiariosEvento2410() {
		try {
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nome) "
					+ "FROM Funcional f "
					+ "WHERE f.status = 5"				 
					+ "AND f.id  IN (SELECT b.funcional.id FROM Beneficiario b) "
					+ "AND f.id  NOT IN (SELECT b.funcional.id FROM Beneficio b) "
					+ "ORDER BY f.nome", Funcional.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}


	@SuppressWarnings("unchecked")
	public List<Funcional> findServidoresEvento1200(String anoReferencia, String mesReferencia) {
			
		StringBuffer sql = new StringBuffer();

		sql.append(" select distinct f.ID, f.IDPESSOAL, f.IDORGAOORIGEM, f.IDSETOR, f.IDOCUPACAO, f.IDCLASSEREFERENCIA,   ");
		sql.append(" f.idespecialidadecargo, f.idorientacaocargo, f.IDTIPOMOVIMENTOENTRADA, f.IDTIPOMOVIMENTOSAIDA, ");
		sql.append(" f.IDCBO, f.IDFOLHA, f.IDTIPOPUBLICACAONOMEACAO, IDTIPOPUBLICACAOSAIDA, f.IDSITUACAO, f.IDTIPOVINCULO, ");
		sql.append(" f.NOME, f.NOMECOMPLETO, f.NOMEPESQUISA, f.MATRICULA, f.MATRICULAESTADUAL, f.CALCULOPCC, f.QTDQUINTOS, ");
		sql.append(" f.LEIINCORPORACAO, f.PONTO, f.STATUS, f.ATIVOFP, f.FLPORTALTRANSPARENCIA, f.IRRF, f.SUPSECINTEGRAL, ");
		sql.append(" f.PROPORCIONALIDADE, f.SALARIOORIGEM, f.ABONOPREVIDENCIARIO, f.DATAPOSSE, f.DATAEXERCICIO, f.DATASAIDA, ");
		sql.append(" f.DOENOMEACAO, f.DOESAIDA, f.DESCRICAONOMEACAO, f.DESCRICAOSAIDA, f.PREVIDENCIA, f.REGIME, f.IDREPRESENTACAOCARGO, ");
		sql.append(" f.IDSETORDESIGNADO, f.IDPESSOAJURIDICA, f.IDAPOSENTADORIA, f.CODCATEGORIA ");
		
		sql.append("  from tb_funcional f  "); 
		sql.append(" inner join fp_dadospagto dp on f.id = dp.idfuncional  ");
		sql.append(" inner join fp_pagamentos pg on pg.arquivo = dp.arquivo  ");
		sql.append(" where previdencia = 3  ");
		sql.append(" and pg.ano_esocial = '"+anoReferencia+"'");
		sql.append(" and pg.mes_esocial = '"+mesReferencia+"'");
		
		sql.append(" and f.id not in (Select IDFUNCIONAL from ESOCIAL_REMUNERACAOTRABALHADOR where IDFUNCIONAL is not null) ");
		sql.append(" and f.id  in (Select IDFUNCIONAL from ESOCIAL_ADMISSAO ) ");
		sql.append(" ORDER BY f.NOME ");
		
		try {
			Query query = entityManager.createNativeQuery(sql.toString(), Funcional.class);
			
			return  query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	public List<Funcional> findServidorEvento2300() {
		try {
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nome) "
					+ "FROM Funcional f "
					+ "WHERE f.saida IS NULL "
					+ "AND f.status = 1 "
					+ "AND f.id IN (SELECT fc.funcional.id FROM FuncionalCedido fc)"
					+ "AND f.id  NOT IN (SELECT e.funcional.id FROM EstagiarioESocial e) "
					+ "ORDER BY f.nome", Funcional.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<CadastroPrestador> findRGPAEvento1200(String anoReferencia, String mesReferencia) {
		
		StringBuffer sql = new StringBuffer();
		
		sql.append("select   ");
		sql.append("fp_cadastroprestador.idprestador,  "); 
		sql.append("fp_dadospagtoprestador.nome,  ");
		//sql.append("fp_cadastroprestador.cpf,  ");
		sql.append("translate(fp_cadastroprestador.cpf,' .-', ' ') as cpf, ");
		sql.append("fp_cadastroprestador.datanascimento,   ");
		sql.append("fp_dadospagtoprestador.novocbo  ");
		
		sql.append("from fp_dadospagtoprestador   ");
		sql.append("inner join fp_cadastroprestador on fp_dadospagtoprestador.idprestador = fp_cadastroprestador.idprestador   ");
		sql.append("where to_char(data_np,'yyyymm') = '"+anoReferencia+mesReferencia+"'  ");
		sql.append(" and  fp_cadastroprestador.idprestador not in (Select IDPRESTADOR from ESOCIAL_REMUNERACAOTRABALHADOR where IDPRESTADOR is not null) ");
		sql.append("order by fp_dadospagtoprestador.data_np, fp_dadospagtoprestador.nome  ");
		
	
		
		try {
			Query query = entityManager.createNativeQuery(sql.toString(), CadastroPrestador.class);
			
			return  query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Funcional> findEstagiarioEvento1200(String anoReferencia, String mesReferencia) {
		StringBuffer sql = new StringBuffer();

		sql.append(" select distinct f.ID, f.IDPESSOAL, f.IDORGAOORIGEM, f.IDSETOR, f.IDOCUPACAO, f.IDCLASSEREFERENCIA,   ");
		sql.append(" f.idespecialidadecargo, f.idorientacaocargo, f.IDTIPOMOVIMENTOENTRADA, f.IDTIPOMOVIMENTOSAIDA, ");
		sql.append(" f.IDCBO, f.IDFOLHA, f.IDTIPOPUBLICACAONOMEACAO, IDTIPOPUBLICACAOSAIDA, f.IDSITUACAO, f.IDTIPOVINCULO, ");
		sql.append(" f.NOME, f.NOMECOMPLETO, f.NOMEPESQUISA, f.MATRICULA, f.MATRICULAESTADUAL, f.CALCULOPCC, f.QTDQUINTOS, ");
		sql.append(" f.LEIINCORPORACAO, f.PONTO, f.STATUS, f.ATIVOFP, f.FLPORTALTRANSPARENCIA, f.IRRF, f.SUPSECINTEGRAL, ");
		sql.append(" f.PROPORCIONALIDADE, f.SALARIOORIGEM, f.ABONOPREVIDENCIARIO, f.DATAPOSSE, f.DATAEXERCICIO, f.DATASAIDA, ");
		sql.append(" f.DOENOMEACAO, f.DOESAIDA, f.DESCRICAONOMEACAO, f.DESCRICAOSAIDA, f.PREVIDENCIA, f.REGIME, f.IDREPRESENTACAOCARGO, ");
		sql.append(" f.IDSETORDESIGNADO, f.IDPESSOAJURIDICA, f.IDAPOSENTADORIA, f.CODCATEGORIA ");
		
		sql.append("  from tb_funcional f  "); 
		sql.append(" inner join fp_dadospagto dp on f.id = dp.idfuncional  ");
		sql.append(" inner join fp_pagamentos pg on pg.arquivo = dp.arquivo  ");
		sql.append(" where f.IDOCUPACAO in (14,15)  ");
		sql.append(" and pg.ano_esocial = '"+anoReferencia+"'");
		sql.append(" and pg.mes_esocial = '"+mesReferencia+"'");
		sql.append(" and f.id not in (Select IDFUNCIONAL from ESOCIAL_REMUNERACAOTRABALHADOR where IDFUNCIONAL is not null) ");
		sql.append(" ORDER BY f.NOME ");
		
		try {
			Query query = entityManager.createNativeQuery(sql.toString(), Funcional.class);
			
			return  query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Funcional> findServidoresEvento1202(String anoReferencia, String mesReferencia) {
		StringBuffer sql = new StringBuffer();

		sql.append(" select distinct f.ID, f.IDPESSOAL, f.IDORGAOORIGEM, f.IDSETOR, f.IDOCUPACAO, f.IDCLASSEREFERENCIA,   ");
		sql.append(" f.idespecialidadecargo, f.idorientacaocargo, f.IDTIPOMOVIMENTOENTRADA, f.IDTIPOMOVIMENTOSAIDA, ");
		sql.append(" f.IDCBO, f.IDFOLHA, f.IDTIPOPUBLICACAONOMEACAO, IDTIPOPUBLICACAOSAIDA, f.IDSITUACAO, f.IDTIPOVINCULO, ");
		sql.append(" f.NOME, f.NOMECOMPLETO, f.NOMEPESQUISA, f.MATRICULA, f.MATRICULAESTADUAL, f.CALCULOPCC, f.QTDQUINTOS, ");
		sql.append(" f.LEIINCORPORACAO, f.PONTO, f.STATUS, f.ATIVOFP, f.FLPORTALTRANSPARENCIA, f.IRRF, f.SUPSECINTEGRAL, ");
		sql.append(" f.PROPORCIONALIDADE, f.SALARIOORIGEM, f.ABONOPREVIDENCIARIO, f.DATAPOSSE, f.DATAEXERCICIO, f.DATASAIDA, ");
		sql.append(" f.DOENOMEACAO, f.DOESAIDA, f.DESCRICAONOMEACAO, f.DESCRICAOSAIDA, f.PREVIDENCIA, f.REGIME, f.IDREPRESENTACAOCARGO, ");
		sql.append(" f.IDSETORDESIGNADO, f.IDPESSOAJURIDICA, f.IDAPOSENTADORIA, f.CODCATEGORIA ");
		
		sql.append("  from tb_funcional f  "); 
		sql.append(" inner join fp_dadospagto dp on f.id = dp.idfuncional  ");
		sql.append(" inner join fp_pagamentos pg on pg.arquivo = dp.arquivo  ");
		sql.append(" where previdencia in (2,4)  ");
		sql.append(" and pg.ano_esocial = '"+anoReferencia+"'");
		sql.append(" and pg.mes_esocial = '"+mesReferencia+"'");
		
		sql.append(" and f.id not in (Select IDFUNCIONAL from ESOCIAL_REMUNERACAOSERVIDOR where IDFUNCIONAL is not null) ");
		sql.append(" and f.id  in (Select IDFUNCIONAL from ESOCIAL_ADMISSAO ) ");
		sql.append(" ORDER BY f.NOME ");
		
		try {
			Query query = entityManager.createNativeQuery(sql.toString(), Funcional.class);
			
			return  query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Funcional> findBeneficioesEvento1207(String anoReferencia, String mesReferencia) {
		
		StringBuffer sql = new StringBuffer();

		sql.append(" select distinct f.ID, f.IDPESSOAL, f.IDORGAOORIGEM, f.IDSETOR, f.IDOCUPACAO, f.IDCLASSEREFERENCIA,   ");
		sql.append(" f.idespecialidadecargo, f.idorientacaocargo, f.IDTIPOMOVIMENTOENTRADA, f.IDTIPOMOVIMENTOSAIDA, ");
		sql.append(" f.IDCBO, f.IDFOLHA, f.IDTIPOPUBLICACAONOMEACAO, IDTIPOPUBLICACAOSAIDA, f.IDSITUACAO, f.IDTIPOVINCULO, ");
		sql.append(" f.NOME, f.NOMECOMPLETO, f.NOMEPESQUISA, f.MATRICULA, f.MATRICULAESTADUAL, f.CALCULOPCC, f.QTDQUINTOS, ");
		sql.append(" f.LEIINCORPORACAO, f.PONTO, f.STATUS, f.ATIVOFP, f.FLPORTALTRANSPARENCIA, f.IRRF, f.SUPSECINTEGRAL, ");
		sql.append(" f.PROPORCIONALIDADE, f.SALARIOORIGEM, f.ABONOPREVIDENCIARIO, f.DATAPOSSE, f.DATAEXERCICIO, f.DATASAIDA, ");
		sql.append(" f.DOENOMEACAO, f.DOESAIDA, f.DESCRICAONOMEACAO, f.DESCRICAOSAIDA, f.PREVIDENCIA, f.REGIME, f.IDREPRESENTACAOCARGO, ");
		sql.append(" f.IDSETORDESIGNADO, f.IDPESSOAJURIDICA, f.IDAPOSENTADORIA, f.CODCATEGORIA ");
		
		sql.append("  from tb_funcional f  "); 
		sql.append(" inner join fp_dadospagto dp on f.id = dp.idfuncional  ");
		sql.append(" inner join fp_pagamentos pg on pg.arquivo = dp.arquivo  ");
		sql.append(" where ");
		sql.append(" pg.ano_esocial = '"+anoReferencia+"'");
		sql.append(" and pg.mes_esocial = '"+mesReferencia+"'");
		
		sql.append(" and f.id  in (Select IDFUNCIONAL from ESOCIAL_BENEFICIO ) ");
		sql.append(" and f.id not in (Select IDFUNCIONAL from ESOCIAL_REMUNERACAOBENEFICIO ) ");
		
		sql.append(" ORDER BY f.NOME ");
		
		try {
			Query query = entityManager.createNativeQuery(sql.toString(), Funcional.class);
			
			return  query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Funcional> findBeneficioesEvento1210(String anoReferencia, String mesReferencia) {
		try {
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nome) "
					+ "FROM Funcional f "
					+ "WHERE 1 = 1 "				 
					+ "AND ("
					+ "f.id  IN (SELECT rt.funcional.id FROM RemuneracaoTrabalhador rt) "
					+ "OR (f.id  IN (SELECT rs.funcional.id FROM RemuneracaoServidor rs) "
					+ "OR (f.id  IN (SELECT rb.funcional.id FROM RemuneracaoBeneficio rb) "
					+ " )"
					+ "AND f.id  NOT IN (SELECT p.funcional.id FROM Pagamentos p) "
					+ "ORDER BY f.nome", Funcional.class);
			return query.getResultList();
		} catch (NoResultException e) {
			return null;
		}
	}
	

}
