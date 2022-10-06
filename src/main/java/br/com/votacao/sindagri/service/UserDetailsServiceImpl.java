package br.com.votacao.sindagri.service;

import br.com.votacao.sindagri.domain.Role;
import br.com.votacao.sindagri.domain.Usuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
  @PersistenceContext
  private EntityManager entityManager;
  
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return (UserDetails)findByUsername(username);
  }
  
  private Usuario findByUsername(String username) {
    try {
      List<Usuario> lista = this.entityManager.createNamedQuery("Usuario.findByUsername", Usuario.class)
        .setParameter("username", username).getResultList();
      if (lista == null || lista.size() == 0)
        return null; 
      Usuario usuario = lista.get(0);
      int totalDeGruposDoSRH = 0;
      for (Role role : usuario.getRoles());
      return usuario;
    } catch (NoResultException e) {
      throw new UsernameNotFoundException("Usuario nao encontrado");
    } 
  }
}
