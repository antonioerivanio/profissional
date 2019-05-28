package br.gov.ce.tce.srh.sca.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.sca.domain.Permissao;
import br.gov.ce.tce.srh.sca.domain.Usuario;

/**
 *
 * @author Robson Castro, em 09/04/2012
 * 
 */
@Repository
public class UsuarioDAOImpl implements UsuarioDAO {

	static Logger logger = Logger.getLogger(UsuarioDAOImpl.class);

	@PersistenceContext
	private EntityManager entityManager;
	
	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}


	@Override
	public Usuario getById(Long id) {
		return entityManager.find(Usuario.class, id);
	}


	@Override
	@SuppressWarnings("unchecked")
	public List<Usuario> findAll() {
		return entityManager.createQuery("SELECT DISTINCT e FROM Usuario e join e.grupoUsuario gu join gu.grupo g join g.sistema s WHERE s.sigla = 'SRH' ORDER BY e.nome ASC").getResultList();
	}

	
	@Override
	public Usuario findByUsername(String username) {
		try {
			String sql = "SELECT usu FROM Usuario usu WHERE UPPER(usu.username) = UPPER(:username)";
			return entityManager.createQuery(sql, Usuario.class)
					.setParameter("username", username).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public List<Permissao> findPermissoesByUsuarioAndSistema(Usuario usuario, String siglaSistema) {
		
		String sql = "SELECT p FROM GrupoUsuario AS gu "
				+ "INNER JOIN gu.grupo AS g "
				+ "INNER JOIN g.permissoes AS p";
		sql += " WHERE g.sistema.sigla = :sigla AND p.sistema.sigla = :sigla AND gu.usuario = :usuario";
		
		TypedQuery<Permissao> query = entityManager.createQuery(sql, Permissao.class);
		
		query.setParameter("sigla", siglaSistema);
		query.setParameter("usuario", usuario.getId());
		List<Permissao> permissaoList = query.getResultList();		
		return permissaoList;
	} 

	
	@Override
	public Usuario findByCpf(String cpf) {
		try {
			String sql = "SELECT usu FROM Usuario usu WHERE UPPER(usu.cpf) = UPPER(:cpf)";
			return entityManager.createQuery(sql, Usuario.class)
					.setParameter("cpf", cpf).getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	
	@Override
	public Usuario salvar(Usuario entidade) {		
		return entityManager.merge(entidade);
	}
}
