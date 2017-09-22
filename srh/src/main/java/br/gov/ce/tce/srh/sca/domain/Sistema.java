package br.gov.ce.tce.srh.sca.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="SISTEMA", schema="SCA")
public class Sistema {

	@Id
	private Long id;

	@Column(name="NOME")
	private String nome;

	@Column(name="SIGLA")
	private String sigla;


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

	public String getSigla() {
		return sigla;
	}

	public void setSigla(String sigla) {
		this.sigla = sigla;
	}

}
