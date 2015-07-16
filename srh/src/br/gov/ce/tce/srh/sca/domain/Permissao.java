package br.gov.ce.tce.srh.sca.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="PERMISSAO", schema="SCA")
public class Permissao {

	@Id
	private Long id;

	@ManyToOne
	@JoinColumn(name = "SISTEMA")	
	private Sistema sistema;

	@Column(name="GRUPO")
	private Long grupo;

	@ManyToOne
	@JoinColumn(name = "SECAO")	
	private Secao secao;

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

	public Long getGrupo() {
		return grupo;
	}

	public void setGrupo(Long grupo) {
		this.grupo = grupo;
	}

	public Secao getSecao() {
		return secao;
	}

	public void setSecao(Secao secao) {
		this.secao = secao;
	}

}
