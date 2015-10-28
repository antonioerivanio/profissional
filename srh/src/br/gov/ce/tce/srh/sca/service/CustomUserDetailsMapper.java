package br.gov.ce.tce.srh.sca.service;

import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.ldap.userdetails.LdapUserDetailsMapper;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.sca.domain.GrupoUsuario;
import br.gov.ce.tce.srh.sca.domain.Permissao;
import br.gov.ce.tce.srh.sca.domain.Usuario;

@Service("customUserDetailsMapper")
public class CustomUserDetailsMapper extends LdapUserDetailsMapper {
	
	@PersistenceContext
	private EntityManager entityManager;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public UserDetails mapUserFromContext(DirContextOperations ctx, String username, Collection authority) {
		
		try {

			Usuario usuario;

			// pegando o usuario
			List<Usuario> lista = entityManager.createNamedQuery("Usuario.findByUsername", Usuario.class).setParameter("username", username).getResultList();

			// verificando a lista de usuarios
			if (lista == null || lista.size() == 0)
				throw new RuntimeException("Usuário não tem acesso aos sistemas!");

			usuario = lista.get(0);
			
			int totalDeGruposDoSRH = 0;

			for (GrupoUsuario grupoUsuarios : usuario.getGrupoUsuario()) {
				
				// verifica se o grupo pertence ao sistema SRH
				if (grupoUsuarios.getGrupo().getSistema().getSigla().equalsIgnoreCase("SRH")){					

					if (grupoUsuarios.getGrupo().getId() != 49 || (grupoUsuarios.getGrupo().getId() == 49 && totalDeGruposDoSRH == 0)){
					
						totalDeGruposDoSRH++;
						
						// pegando a lista de permissoes
						Query query = entityManager.createQuery("Select p from Permissao p join fetch p.secao join fetch p.sistema where p.grupo = :grupo ");
						query.setParameter("grupo", grupoUsuarios.getGrupo().getId() );
						List<Permissao> listaPermissao = query.getResultList();
	
						// dando grant
						for (Permissao permissao : listaPermissao) {
	
							// verifica se a permissao pertence ao sistema SRH
							if (permissao.getSistema().getSigla().equalsIgnoreCase("SRH")) {
								GrantedAuthority result = new SimpleGrantedAuthority(permissao.getSecao().getNome());
								usuario.getAuthorities().add(result);
							}
	
						}
					
					}

				}

			}

			return usuario;

		} catch (NoResultException e) {
			throw new UsernameNotFoundException("Usuario nao encontrado");
		}

	}

}
