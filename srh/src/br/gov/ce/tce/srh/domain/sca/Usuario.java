package br.gov.ce.tce.srh.domain.sca;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.BatchSize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.ldap.userdetails.LdapUserDetails;

@Entity
@Table(name="USUARIO", schema="SCA")
@NamedQueries({
  @NamedQuery(name = "Usuario.findByUsername", 
              query = "SELECT usu FROM Usuario usu WHERE UPPER(usu.username) = UPPER(:username)")
})
public class Usuario implements Serializable, LdapUserDetails {

	private static final long serialVersionUID = -8451679170281063697L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column(name="NOME")
	private String nome;

	@Column(name="LOGIN", unique = true)
	private String username;

	@Column(name="SENHA")
	private String password;
	
	@Column(name="CPF")
	private String cpf;

	@Column(name="ATIVO")
	private boolean ativo;

	@Column(name="SENHAEXPIRADA")
	private boolean senhaExpirada;


	@OneToMany(mappedBy="usuario", targetEntity=GrupoUsuario.class, fetch=FetchType.EAGER)
	@BatchSize(size=1000)
	@OrderBy("grupo")
	private List<GrupoUsuario> grupoUsuario;

	@Transient
	private Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

	@Transient
	private String dn;
	
	@Transient
    public Collection<GrantedAuthority> getAuthorities() {
    	return this.authorities;
    }
    
    @Transient
    public boolean isEnabled() {
    	return ativo;
    }

    @Transient
    public boolean isAccountNonExpired() {
    	return !senhaExpirada;
    }

    @Transient
    public boolean isAccountNonLocked() {
    	return true;
    }

    @Transient
    public boolean isCredentialsNonExpired() {
    	return true;
    }

    public Long getId() {
    	return id;
    }

    public void setId(Long id) {
    	this.id = id;
    }
    
    public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getUsername() {
    	return username;
    }

    public void setUsername(String username) {
    	this.username = username;
    }

    public String getPassword() {
    	return password;
    }

    public void setPassword(String password) {
    	this.password = password;
    }
    
    public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public void setAtivo(boolean ativo) {
    	this.ativo = ativo;
    }

    public void setSenhaExpirada(boolean senhaExpirada){
    	this.senhaExpirada = senhaExpirada;
    }

    public List<GrupoUsuario> getGrupoUsuario() {
    	return grupoUsuario;
    }

    public void setGrupoUsuario(List<GrupoUsuario> grupoUsuario) {
    	this.grupoUsuario = grupoUsuario;
    }

    public void setAuthorities(Collection<GrantedAuthority> authorities) {
    	this.authorities = authorities;
    }
    
    public Boolean hasAuthority(String authority){
    	for (GrantedAuthority aut : authorities) {
			if(aut.getAuthority().equals(authority)){
				return true;
			}
		}
    	return false;
    }

	@Override
	public String getDn() {		
		return this.dn;
	}	

    public void setDn(String dn) {
		this.dn = dn;
	}

}
