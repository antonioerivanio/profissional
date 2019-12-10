package br.gov.ce.tce.srh.sca.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ce.tce.srh.domain.DatabaseMetadata;

@Entity
@Table(name="SECAO", schema=DatabaseMetadata.SCHEMA_SCA)
public class Secao {

	@Id
	private Long id;

	@Column(name="NOME")
	private String nome;

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

}
