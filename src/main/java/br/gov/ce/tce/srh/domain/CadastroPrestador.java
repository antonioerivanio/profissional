package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SuppressWarnings("serial")
@Table(name="FP_CADASTROPRESTADOR", schema=DatabaseMetadata.SCHEMA_SRH)
public class CadastroPrestador extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="IDPRESTADOR")
	private Long id;

	@Column(name="NOME")
	private String nome;

	@Column(name="CPF")
	private String cpf;
	
	@Column(name="NOVOCBO")
	private String novoCBO;
	
	@Temporal(TemporalType.DATE)
	  @Column(name = "DATANASCIMENTO")
	  private Date dataNascimento;

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

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getNovoCBO() {
		return novoCBO;
	}

	public void setNovoCBO(String novoCBO) {
		this.novoCBO = novoCBO;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}


}