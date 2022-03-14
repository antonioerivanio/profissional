package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_PESSOAJURIDICA
 * 
 * @since : Dez 08, 2021, 09:08:20 AM
 * @author : marcelo.viana@tce.ce.gov.br
 *
 */

@Entity
@SuppressWarnings("serial")
@Table(name = "TB_PESSOAJURIDICA", schema = DatabaseMetadata.SCHEMA_SRH)
public class PessoaJuridica extends BasicEntity<Long> implements Serializable {
	

	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "CNPJ", nullable = false)
	private String cnpj;

	@Column(name = "RAZAOSOCIAL", nullable = false)
	private String razaoSocial;

	@Column(name = "NOMEFANTASIA", nullable = false)
	private String nomeFantasia;

	public PessoaJuridica() {
	}

	public PessoaJuridica(Long id, String cnpj, String razaoSocial, String nomeFantasia) {
		this.id = id;
		this.cnpj = cnpj;
		this.razaoSocial = razaoSocial;
		this.nomeFantasia = nomeFantasia;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getNomeFantasia() {
		return nomeFantasia;
	}

	public void setNomeFantasia(String nomeFantasia) {
		this.nomeFantasia = nomeFantasia;
	}

	
}
