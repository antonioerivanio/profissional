package br.com.votacao.sindagri.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.validator.constraints.Email;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Audited
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "usuarios")
@NamedQueries({@NamedQuery(name = "Usuario.findByUsernome", query = "SELECT usu FROM Usuario usu WHERE UPPER(usu.nome) = UPPER(:nome)"), 
	@NamedQuery(name = "Usuario.findByUsermatricula", query = "SELECT usu FROM Usuario usu WHERE usu.matricula = :matricula"), 
	@NamedQuery(name = "Usuario.findByUsercpf", query = "SELECT usu FROM Usuario usu WHERE usu.cpf = :cpf"),
	@NamedQuery(name = "Usuario.findByUserlogin", query = "SELECT usu FROM Usuario usu WHERE usu.login = :login")})
public class Usuario implements UserDetails {
  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

@Id
  @SequenceGenerator(name = "usuarios_id_seq", sequenceName = "usuarios_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarios_id_seq")
  @Column(name = "id")
  private Integer id;
  
 @Email(message = "endereço de email invalido")
  @Column
  private String email;
  
 @NotNull
  @Column
  private String password;
 @NotNull
  @Column
  private String login;  
 @NotNull
  @Column
  private String nome;
  
  @Column
  private String matricula;
  
  @Column
  private String cpf;
  
  @Column
  private boolean enabled;
  
  @Transient
  private boolean isMembroComissao;

  @NotAudited
  @ManyToMany
  @JoinTable(
    name = "grupos_usuarios", 
    joinColumns = @JoinColumn(name = "usuario_id"), 
    inverseJoinColumns = @JoinColumn(name = "grupo_id"))
  private Set<Grupo> grupoUsuario;
  
  
  public Collection<? extends GrantedAuthority> getAuthorities() {
	  Set<Grupo> userGroups = this.getGrupoUsuario();
      Collection<GrantedAuthority> authorities = new ArrayList<>(userGroups.size());
      for(Grupo userGroup : userGroups){
          authorities.add(new SimpleGrantedAuthority(userGroup.getNome().toUpperCase()));
      }
      
      return authorities;
  }
  
  public String getUsername() {
    return this.nome;
  }
  
  public boolean isAccountNonExpired() {
    return true;
  }
  
  public boolean isAccountNonLocked() {
    return this.enabled;
  }
  
  public boolean isCredentialsNonExpired() {
    return true;
  }

  public String toString() {
    return "Usuario(id=" + getId() + ", email=" + getEmail() + ", password=" + getPassword() + ", nome=" + getNome() + ", matricula=" + getMatricula() + ", cpf=" + getCpf() + ", enabled=" + isEnabled() + ", isMembroComissao=" + isMembroComissao() + ", roles=" + getGrupoUsuario() + ")";
  }

public Usuario(String nome) {
	super();
	this.nome = nome;
}
  
}
