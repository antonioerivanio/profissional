package br.com.votacao.sindagri.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import br.com.votacao.sindagri.domain.Usuario;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
  @PersistenceContext
  private EntityManager entityManager;
  
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return (UserDetails)findByUsername(username);
  }
  
  private Usuario findByUsername(String login) {
    try {
    	
    	List<Usuario> lista = this.entityManager.createNamedQuery("Usuario.findByUserlogin", Usuario.class)
        .setParameter("login", login).getResultList();
      if (lista == null || lista.size() == 0)
        return null; 
      Usuario usuario = lista.get(0);
      int totalDeGruposDoSRH = 0;
		/*
		 * for (GrupoUsuario grupo : usuario.getGrupoUsuario()) {
		 * 
		 * }
		 */
      return usuario;
    } catch (NoResultException e) {
      throw new UsernameNotFoundException("Usuario nao encontrado");
    } 
  }
}
