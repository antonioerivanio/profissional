package br.gov.ce.tce.srh.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.sca.domain.Usuario;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.util.SRHUtils;

@Repository
public class FuncionalDAOImpl implements FuncionalDAO {

	static Logger logger = Logger.getLogger(FuncionalDAOImpl.class);

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

	
	@Override
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
	
	@Override
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

	@Override
	public Funcional salvar(Funcional entidade) {

		if (entidade == null || entidade.getId() == null) {
			entidade.setId(getMaxId());
		}

		entidade.setNome(entidade.getPessoal().getNome());
		entidade.setNomeCompleto(entidade.getPessoal().getNomeCompleto());
		entidade.setNomePesquisa(entidade.getPessoal().getNomePesquisa());

		return entityManager.merge(entidade);
	}


	@Override
	public void excluir(Funcional entidade) {		
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}


	@Override
	public int count(Long pessoal, String orderBy) {
		Query query = entityManager.createQuery("SELECT count (f) "
												+ "FROM Funcional f "
												+ "WHERE f.pessoal.id = :idPessoal "
												+ "ORDER BY f.id " + orderBy);
		query.setParameter("idPessoal", pessoal);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Funcional> search(Long pessoal, String orderBy, int first, int rows) {
		Query query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.tipoMovimentoEntrada, f.ocupacao, f.exercicio, f.saida) "
												+ "FROM Funcional f "
												+ "WHERE f.pessoal.id = :idPessoal "
												+ "ORDER BY f.id " + orderBy);
		query.setParameter("idPessoal", pessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Funcional> searchForReclassificacao(Long pessoal, String orderBy, int first, int rows) {
		Query query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.tipoMovimentoEntrada, f.ocupacao, f.exercicio, f.saida, f.posse) "
												+ "FROM Funcional f "
												+ "WHERE f.pessoal.id = :idPessoal "
												+ "ORDER BY f.id " + orderBy);
		query.setParameter("idPessoal", pessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	

	@Override
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
			Query query = entityManager.createQuery(sql.toString());
			query.setParameter("id", idPessoa);
			return (Funcional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	@Override
	public Funcional getByPessoaAtivoFp(Long idPessoal) {
		try {
			Query query = entityManager.createQuery("SELECT f "
													+ "FROM Funcional f "
													+ "WHERE f.pessoal.id = :idPessoal "
													+ "AND f.saida is null "
													+ "AND f.status != 5 "
													+ "AND f.atipoFp = 0 "
													+ "AND (f.situacao.id = 5 OR f.situacao.id = 7) "
													+ "ORDER BY f.id");
			query.setParameter("idPessoal", idPessoal);
			return (Funcional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	@Override
	public Funcional getByMatriculaAtivo(String matricula) {
		try {
			Query query = entityManager.createQuery("SELECT f "
													+ "FROM Funcional f "
													+ "WHERE upper( f.matricula ) = :matricula "
													+ "AND f.saida IS NULL "
													+ "AND f.status != 5 ");
			query.setParameter("matricula", matricula);
			return (Funcional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public Funcional getMatriculaAndNomeByCpf(String cpf) {

		Query query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nomeCompleto, f.setor, f.ocupacao, f.exercicio) "
												+ "FROM Funcional f "
												+ "WHERE f.pessoal.cpf = :cpf "
												+ "ORDER BY f.id desc");
		query.setParameter("cpf", SRHUtils.removerMascara( cpf ));
		List<Funcional> lista = query.getResultList(); 
		if (lista.size() > 0)
			return lista.get(0);
		

		return null;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Funcional> getMatriculaAndNomeByCpfList(String cpf) {

		Query query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nomeCompleto, f.setor, f.ocupacao, f.exercicio) "
												+ "FROM Funcional f "
												+ "WHERE f.pessoal.cpf = :cpf "
												+ "ORDER BY f.id desc");
		query.setParameter("cpf", SRHUtils.removerMascara( cpf ));
		
		List<Funcional> resultList = query.getResultList();
		
		Map<String, Funcional> returnMap = new HashMap<String, Funcional>();
		
		for (Funcional funcional : resultList) {
			if(!returnMap.containsKey(funcional.getMatricula()))
				returnMap.put(funcional.getMatricula(), funcional);
		}		
		
		return new ArrayList<Funcional>(returnMap.values());
		
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public Funcional getMatriculaAndNomeByCpfAtiva(String cpf) {

		Query query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nomeCompleto, f.setor, f.ocupacao, f.exercicio) "
												+ "FROM Funcional f "
												+ "WHERE f.pessoal.cpf = :cpf "
												+ "AND f.saida IS NULL "
												+ "AND f.status != 5 "
												+ "ORDER BY f.id desc");
		query.setParameter("cpf", SRHUtils.removerMascara( cpf ) );
		List<Funcional> lista = query.getResultList(); 
		if (lista.size() > 0)
			return lista.get(0);
		

		return null;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public Funcional getCpfAndNomeByMatricula(String matricula) {
		try {
			Query query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nomeCompleto, f.setor, f.ocupacao, f.exercicio) "
													+ "FROM Funcional f "
													+ "WHERE upper( f.matricula ) = :matricula "
													+ "ORDER BY f.id desc");
			query.setParameter("matricula", matricula);
			List<Funcional> lista = query.getResultList(); 
			if (lista.size() > 0)
				return lista.get(0);
			
			return null;
		} catch (NoResultException e) {
			return null;
		}
	}
	

	@Override
	public Funcional getCpfAndNomeByMatriculaAtiva(String matricula) {
		try {
			Query query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.nomeCompleto, f.setor, f.ocupacao, f.exercicio) "
													+ "FROM Funcional f "
													+ "WHERE upper( f.matricula ) = :matricula "
													+ "AND f.saida IS NULL "
													+ "AND f.status != 5 "
													+ "ORDER BY f.id desc");
			query.setParameter("matricula", matricula);
			return (Funcional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	@Override	
	public List<Funcional> findByNome(String nome) {
		Query query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.setor) "
																+ "FROM Funcional f "
																+ "WHERE upper( f.nomePesquisa ) like :nome "
																+ "AND f.saida IS NULL "
																+ "AND f.status != 5 "
																+ "ORDER BY f.id desc");
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		
		return query.getResultList();
	}	
	
	
	@SuppressWarnings("unchecked")
	@Override	
	public List<Funcional> findAllByNome(String nome) {
		Query query = entityManager.createQuery("SELECT new Funcional(f.id, f.matricula, f.pessoal, f.setor) "
																+ "FROM Funcional f "
																+ "WHERE upper( f.nomePesquisa ) like :nome "
																+ "ORDER BY f.id desc");
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");

		List<Funcional> resultList = query.getResultList();
		
		Map<String, Funcional> returnMap = new HashMap<String, Funcional>();
		
		for (Funcional funcional : resultList) {
			if(!returnMap.containsKey(funcional.getMatricula()))
				returnMap.put(funcional.getMatricula(), funcional);
		}
		
		return new ArrayList<Funcional>(returnMap.values());
	}
	
	
	@Override
	public List<Funcional> findByUsuariologado(Usuario usuarioLogado) {
		return findByNome(pessoalService.getByCpf(SRHUtils.removerMascara(usuarioLogado.getCpf())).getNomeCompleto());
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Funcional> findByPessoal(Long idPessoal, String orderBy) {
		Query query = entityManager.createQuery("SELECT f FROM Funcional f WHERE f.pessoal.id = :idPessoal order by f.id " + orderBy);
		query.setParameter("idPessoal", idPessoal);
		return query.getResultList();
	}

}