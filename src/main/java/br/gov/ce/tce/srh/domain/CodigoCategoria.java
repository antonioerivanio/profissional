package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@SuppressWarnings("serial")
@Table(name="TB_CODIGOCATEGORIA", schema=DatabaseMetadata.SCHEMA_SRH)
public class CodigoCategoria extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="CODIGO")
	private String codigo;
	
	@Column(name="DESCRICAO")
	private String descricao;

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

	@Override
	public Long getId() {
		// TODO Auto-generated method stub
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
		// TODO Auto-generated method stub
		
	}

}
