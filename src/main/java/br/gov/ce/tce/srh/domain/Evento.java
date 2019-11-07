package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: ESOCIAL_NOTIFICAO
 * 
 * @since : Tue 05, 2019, 15:24:56 PM
 * @author : esmayk.alves@tce.ce.gov.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_EVENTO", schema = "SRH")
public class Evento extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name = "ID")
	private Long id;

	@Column(name = "CODIGO")
	private String codigo;
	
	@Column(name = "DESCRICAO")
	private String descricao;

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
}
