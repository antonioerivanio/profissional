package br.com.votacao.sindagri.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.votacao.sindagri.domain.Grupo;
import br.com.votacao.sindagri.domain.Permissao;
import br.com.votacao.sindagri.domain.Pessoal;
import br.com.votacao.sindagri.domain.Usuario;
import br.com.votacao.sindagri.domain.Voto;

@Repository
public class VotoDAO {
	static Logger logger = Logger.getLogger(VotoDAO.class);

	@PersistenceContext
	private EntityManager entityManager;

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public Voto getById(Long id) {
		return (Voto) this.entityManager.find(Voto.class, id);
	}

	public List<Voto> findAll() {
		return this.entityManager.createQuery(
				"SELECT DISTINCT e FROM Voto e join e.grupoVoto gu join gu.grupo g join g.sistema s WHERE s.sigla = 'SRH' ORDER BY e.nome ASC")
				.getResultList();
	}

	public Grupo findByGruponome(String nome) {
		try {

			return (Grupo) this.entityManager.createNamedQuery("Grupo.findByGruponome", Grupo.class)
					.setParameter("nome", nome).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Voto findByUsername(String username) {
		try {
			String sql = "SELECT usu FROM Voto usu WHERE UPPER(usu.username) = UPPER(:username)";
			return (Voto) this.entityManager.createQuery(sql, Voto.class).setParameter("username", username)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Voto findUsuarioByVotouser(Usuario user) {
		try {
			List<Voto> lista = this.entityManager.createNamedQuery("Voto.findByVotousuario", Voto.class)
					.setParameter("usuario", user).getResultList();
			if (lista == null || lista.size() == 0)
				return null;
			return  lista.get(0);
		} catch (NoResultException e) {
			return null;
		}
	}

	public Voto findByVotomatricula(String matricula) {
		try {
			return (Voto) this.entityManager.createNamedQuery("Voto.findByUsermatricula", Voto.class)
					.setParameter("matricula", matricula).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Voto findByVotocpf(String cpf) {
		try {
			return (Voto) this.entityManager.createNamedQuery("Voto.findByUsercpf", Voto.class)
					.setParameter("cpf", cpf).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public List<Permissao> findPermissoesByVotoAndSistema(Voto Voto, String siglaSistema) {
		String sql = "SELECT p FROM GrupoVoto AS gu INNER JOIN gu.grupo AS g INNER JOIN g.permissoes AS p";
		sql = String.valueOf(sql)
				+ " WHERE g.sistema.sigla = :sigla AND p.sistema.sigla = :sigla AND gu.Voto = :Voto";
		TypedQuery<Permissao> query = this.entityManager.createQuery(sql, Permissao.class);
		query.setParameter("sigla", siglaSistema);
		query.setParameter("Voto", Voto.getId());
		List<Permissao> permissaoList = query.getResultList();
		return permissaoList;
	}

	public Voto findByCpf(String cpf) {
		try {
			String sql = "SELECT usu FROM Voto usu WHERE UPPER(usu.cpf) = UPPER(:cpf)";
			return (Voto) this.entityManager.createQuery(sql, Voto.class).setParameter("cpf", cpf)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Pessoal findPessoalByCpf(String cpf) {
		try {
			return (Pessoal) this.entityManager.createNamedQuery("Pessoal.findByCpf", Pessoal.class)
					.setParameter("cpf", cpf).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public Pessoal findPessoalByMatricula(String matricula) throws Exception {
		try {
			return (Pessoal) this.entityManager.createNamedQuery("Pessoal.findByMatricula", Pessoal.class)
					.setParameter("matricula", matricula).getSingleResult();
		} catch (NoResultException e) {
			throw new Exception("Ops!. Nenhum usuário cadastrado com essa matricula!");
		}
	}

	@Transactional
	public Voto salvar(Voto entidade) throws Exception {
		this.entityManager.persist(entidade);
		entidade = (Voto) this.entityManager.find(Voto.class, entidade.getId());
		return entidade;
	}

}
