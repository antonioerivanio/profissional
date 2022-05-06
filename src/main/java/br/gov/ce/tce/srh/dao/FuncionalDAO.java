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
		return findByNome(pessoalService.getByCpf(SRHUtils.removerMascara(usuarioLogado.getCpf())).getNomeCompleto());
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
	
	public List<Funcional> findServidoresEvento2230() {
		try {	  
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nome) "
					+ "FROM Funcional f "
					+ "WHERE f.saida IS NULL "
					+ "AND f.status = 1 "
					+ "AND f.ocupacao.id not in (14,15) "
					//+ "AND f.id  NOT IN (SELECT a.funcional.id FROM AfastamentoESocial a) "
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
					+ "WHERE f.status = 5"				 
					+ "AND f.id  IN (SELECT a.funcional.id FROM Aposentadoria a) "
					+ "AND f.id  NOT IN (SELECT b.funcional.id FROM Beneficiario b) "
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

	public List<Funcional> findServidoresEvento1200(String anoReferencia, String mesReferencia) {
		try {
			TypedQuery<Funcional> query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nome) "
					+ "FROM Funcional f "
					+ "WHERE f.ocupacao.id = 33 "				 					
					+ "AND f.id  NOT IN (SELECT r.funcional.id FROM RemuneracaoTrabalhador r where r.referencia like :referencia) "
					+ "ORDER BY f.nome", Funcional.class);
			 
			 
			query.setParameter("referencia", "%" + anoReferencia+mesReferencia + "%");
			return query.getResultList();
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

}
