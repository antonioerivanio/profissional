package br.com.votacao.sindagri.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
@NamedQueries({@NamedQuery(name = "Usuario.findByUsernome", query = "SELECT usu FROM Usuario usu WHERE UPPER(usu.nome) = UPPER(:nome)"), @NamedQuery(name = "Usuario.findByUsermatricula", query = "SELECT usu FROM Usuario usu WHERE usu.matricula = :matricula"), @NamedQuery(name = "Usuario.findByUsercpf", query = "SELECT usu FROM Usuario usu WHERE usu.cpf = :cpf")})
public class Usuario implements UserDetails {
  @Id
  @SequenceGenerator(name = "users_user_id_seq", sequenceName = "users_user_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
  @Column(name = "user_id")
  private Integer id;
  
  @Column
  private String email;
  
  @Column(name = "senha")
  private String password;
  
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
  
  
  
  public String toString() {
    return "Usuario(id=" + getId() + ", email=" + getEmail() + ", password=" + getPassword() + ", nome=" + getNome() + ", matricula=" + getMatricula() + ", cpf=" + getCpf() + ", enabled=" + isEnabled() + ", isMembroComissao=" + isMembroComissao() + ", roles=" + getRoles() + ")";
  }
  
  @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
  @JoinTable(name = "users_roles", joinColumns = {@JoinColumn(name = "user_id")}, inverseJoinColumns = {@JoinColumn(name = "role_id")})
  private Set<Role> roles = new HashSet<>();
  
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return null;
  }
  
  public String getUsername() {
    return null;
  }
  
  public boolean isAccountNonExpired() {
    return false;
  }
  
  public boolean isAccountNonLocked() {
    return false;
  }
  
  public boolean isCredentialsNonExpired() {
    return false;
  }
}
