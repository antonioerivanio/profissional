package br.gov.ce.tce.srh.dao;

import java.util.List;

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

/**
 *
 * @author robstown
 * 
 */
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


	/**
	 * Método responsável em pegar a última matrícula cadastrada e verificar qual é a próxima matrícula válida 
	 * utilizando-se para isso o validador da matrícula
	 * 
	 * @return
	 * 
	 */
	@Override
	public String getMaxMatricula() {

		Query query = entityManager.createQuery("Select max(l.matricula) from Funcional l ");
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
//		Query query = entityManager.createQuery("DELETE FROM Funcional l WHERE l.id=:id");
//		query.setParameter("id", entidade.getId());
//		query.executeUpdate();
		
		entidade = entityManager.merge(entidade);
		entityManager.remove(entidade);
	}


	@Override
	public int count(Long pessoal, String orderBy) {
		Query query = entityManager.createQuery("SELECT count (r) FROM Funcional r WHERE r.pessoal.id = :idPessoal order by r.id " + orderBy);
		query.setParameter("idPessoal", pessoal);
		return ((Long) query.getSingleResult()).intValue();
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Funcional> search(Long pessoal, String orderBy, int first, int rows) {
		Query query = entityManager.createQuery("SELECT new Funcional(r.id, r.matricula, r.tipoMovimentoEntrada, r.ocupacao, r.exercicio, r.saida) FROM Funcional r WHERE r.pessoal.id = :idPessoal order by r.id " + orderBy);
		query.setParameter("idPessoal", pessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Funcional> searchForReclassificacao(Long pessoal, String orderBy, int first, int rows) {
		Query query = entityManager.createQuery("SELECT new Funcional(r.id, r.matricula, r.tipoMovimentoEntrada, r.ocupacao, r.exercicio, r.saida, r.posse) FROM Funcional r WHERE r.pessoal.id = :idPessoal order by r.id " + orderBy);
		query.setParameter("idPessoal", pessoal);
		query.setFirstResult(first);
		query.setMaxResults(rows);
		return query.getResultList();
	}
	
	@Override
	public Funcional getById(Long id) {
		return entityManager.find(Funcional.class, id);
	}
	

	@Override
	public Funcional getByPessoaAtivo(Long idPessoa) {
		try {
			StringBuffer sql = new StringBuffer();
			sql.append("SELECT r FROM Funcional r ");
			sql.append("    left join fetch     r.setor ");
			sql.append("    left join fetch     r.classeReferencia ");
			sql.append("    left join fetch     r.especialidadeCargo ");
			sql.append("    left join fetch     r.tipoMovimentoEntrada ");
			sql.append("    left join fetch     r.tipoMovimentoSaida ");
			sql.append("    left join fetch     r.cbo ");
			sql.append("    left join fetch     r.folha ");
			sql.append("    left join fetch     r.tipoPublicacaoNomeacao ");
			sql.append("    left join fetch     r.tipoPublicacaoSaida ");
			sql.append("    left join fetch     r.situacao ");
			sql.append("    left join fetch     r.vinculo ");
			sql.append("    WHERE r.pessoal.id = :id AND r.saida IS NULL ");
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
			Query query = entityManager.createQuery("SELECT r FROM Funcional r WHERE r.pessoal.id = :idPessoal AND r.saida is null AND r.atipoFp = 0 AND (r.situacao.id = 5 OR r.situacao.id = 7) order by r.id");
			query.setParameter("idPessoal", idPessoal);
			return (Funcional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	@Override
	public Funcional getByMatriculaAtivo(String matricula) {
		try {
			Query query = entityManager.createQuery("SELECT r FROM Funcional r WHERE upper( r.matricula ) = :matricula AND r.saida IS NULL ");
			query.setParameter("matricula", matricula);
			return (Funcional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public Funcional getMatriculaAndNomeByCpf(String cpf) {

		// com mascara
		Query query = entityManager.createQuery("SELECT new Funcional(r.id, r.matricula, r.pessoal, r.nomeCompleto, r.setor, r.ocupacao, r.exercicio) FROM Funcional r WHERE r.pessoal.cpf = :cpf order by r.id desc");
		query.setParameter("cpf", cpf);
		List<Funcional> lista = query.getResultList(); 
		if (lista.size() > 0)
			return lista.get(0);

		// sem mascara
		query = entityManager.createQuery("SELECT new Funcional(r.id, r.matricula, r.pessoal, r.nomeCompleto, r.setor, r.ocupacao, r.exercicio) FROM Funcional r WHERE r.pessoal.cpf = '" + SRHUtils.removerMascara( cpf ) + "' order by r.id desc");
		lista = query.getResultList(); 
		if (lista.size() > 0)
			return lista.get(0);

		return null;
	}
	

	@Override
	public Funcional getCpfAndNomeByMatriculaAtiva(String matricula) {
		try {
			Query query = entityManager.createQuery("SELECT new Funcional(r.id, r.matricula, r.pessoal, r.nomeCompleto, r.setor, r.ocupacao, r.exercicio) FROM Funcional r WHERE upper( r.matricula ) = :matricula AND r.saida IS NULL ");
			query.setParameter("matricula", matricula);
			return (Funcional) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}


	@Override
	@SuppressWarnings("unchecked")
	public Funcional getMatriculaAndNomeByCpfAtiva(String cpf) {

		// com mascara
		Query query = entityManager.createQuery("SELECT new Funcional(r.id, r.matricula, r.pessoal, r.nomeCompleto, r.setor, r.ocupacao, r.exercicio) FROM Funcional r WHERE r.pessoal.cpf LIKE :cpf AND r.saida IS NULL order by r.id desc");
		query.setParameter("cpf", "%" + cpf + "%");
		List<Funcional> lista = query.getResultList(); 
		if (lista.size() > 0)
			return lista.get(0);

		// sem mascara
		query = entityManager.createQuery("SELECT new Funcional(r.id, r.matricula, r.pessoal, r.nomeCompleto, r.setor, r.ocupacao, r.exercicio) FROM Funcional r WHERE r.pessoal.cpf LIKE :cpf AND r.saida IS NULL order by r.id desc");
		query.setParameter("cpf", "%"+ SRHUtils.removerMascara( cpf ) + "%");
		lista = query.getResultList(); 
		if (lista.size() > 0)
			return lista.get(0);

		return null;
	}
	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> findByNome(String nome) {
		Query query = entityManager.createQuery("SELECT distinct( r.matricula ), r.id, p.id, p.nomeCompleto, p.nomeMae, p.cpf, p.rg, p.emissorRg, p.ufEmissorRg.id, r.setor.id, r.setor.nome FROM Funcional r join r.pessoal p WHERE upper( r.nomePesquisa ) like :nome AND r.saida IS NULL ");
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		return query.getResultList();
	}
	

	// Consultas replicadas sem o critério de datasaida nula para que se possa ver o histórico dos servidores que já saíram. 
	// By Zacarias - 06/08/2014
	
	@Override
	@SuppressWarnings("unchecked")
	public Funcional getCpfAndNomeByMatricula(String matricula) {
		try {
			Query query = entityManager.createQuery("SELECT new Funcional(r.id, r.matricula, r.pessoal, r.nomeCompleto, r.setor, r.ocupacao, r.exercicio) FROM Funcional r WHERE upper( r.matricula ) = :matricula AND r.atipoFp = true order by r.id desc");
			query.setParameter("matricula", matricula);
			List<Funcional> lista = query.getResultList(); 
			if (lista.size() > 0)
				return lista.get(0);
			
			return null;
		} catch (NoResultException e) {
			return null;
		}
	}
	

	/*@Override
	@SuppressWarnings("unchecked")
	public Funcional getMatriculaAndNomeByCpf(String cpf) {

		// com mascara
		Query query = entityManager.createQuery("SELECT new Funcional(r.id, r.matricula, r.pessoal, r.nomeCompleto, r.setor, r.ocupacao, r.exercicio) FROM Funcional r WHERE r.pessoal.cpf LIKE :cpf  ");
		query.setParameter("cpf", "%" + cpf + "%");
		List<Funcional> lista = query.getResultList(); 
		if (lista.size() > 0)
			return lista.get(0);

		// sem mascara
		query = entityManager.createQuery("SELECT new Funcional(r.id, r.matricula, r.pessoal, r.nomeCompleto, r.setor, r.ocupacao, r.exercicio) FROM Funcional r WHERE r.pessoal.cpf LIKE :cpf  ");
		query.setParameter("cpf", "%"+ SRHUtils.removerMascara( cpf ) + "%");
		lista = query.getResultList(); 
		if (lista.size() > 0)
			return lista.get(0);

		return null;
	}
*/	
	
	@Override
	@SuppressWarnings("unchecked")
	public List<String> findAllByNome(String nome) {
		Query query = entityManager.createQuery("SELECT distinct( r.matricula ), r.id, p.id, p.nomeCompleto, p.nomeMae, p.cpf, p.rg, p.emissorRg, p.ufEmissorRg.id, r.setor.id, r.setor.nome FROM Funcional r join r.pessoal p WHERE upper( r.nomePesquisa ) like :nome  AND r.atipoFp = true order by r.id desc");
		query.setParameter("nome", "%" + nome.toUpperCase() + "%");
		return query.getResultList();
	}
	
	
	@Override
	public List<String> findByUsuariologado(Usuario usuarioLogado) {
		return findByNome(pessoalService.getByCpf(SRHUtils.removerMascara(usuarioLogado.getCpf())).getNomeCompleto());
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Funcional> findByPessoal(Long idPessoal, String orderBy) {
		Query query = entityManager.createQuery("SELECT r FROM Funcional r WHERE r.pessoal.id = :idPessoal order by r.id " + orderBy);
		query.setParameter("idPessoal", idPessoal);
		return query.getResultList();
	}

}
