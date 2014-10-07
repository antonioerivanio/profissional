package br.gov.ce.tce.srh.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import br.gov.ce.tce.srh.domain.sca.Usuario;

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

}
