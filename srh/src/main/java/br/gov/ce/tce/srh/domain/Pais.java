package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name="ESOCIAL_PAIS", schema="SRH")
public class Pais extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="CODIGO")
	private String codigo;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@Column(name="ORDENACAO")
	private Integer ordenacao;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getOrdenacao() {
		return ordenacao;
	}

	public void setOrdenacao(Integer ordenacao) {
		this.ordenacao = ordenacao;
	}
	
}
