package br.gov.ce.tce.srh.sca.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ce.tce.srh.domain.DatabaseMetadata;

@Entity
@Table(name="SISTEMA", schema=DatabaseMetadata.SCHEMA_SCA)
public class Sistema implements Serializable {

	/**
   * 
   */
  private static final long serialVersionUID = 1L;

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
