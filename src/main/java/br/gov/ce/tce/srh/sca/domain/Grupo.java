package br.gov.ce.tce.srh.sca.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="GRUPO", schema="SCA")
public class Grupo implements Serializable {

	private static final long serialVersionUID = 937394715296920532L;

	@Id
	private Long id;

	@Column(name="NOME")
	private String nome;
	
	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name = "SISTEMA")	
	private Sistema sistema;
	
	@OneToMany
	@JoinColumn(name = "GRUPO", referencedColumnName = "id")
	private List<Permissao> permissoes;	

	public List<Permissao> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Permissao> permissoes) {
		this.permissoes = permissoes;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}
