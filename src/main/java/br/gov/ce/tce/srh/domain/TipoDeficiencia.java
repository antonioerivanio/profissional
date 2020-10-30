package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name="TB_TIPODEFICIENCIA", schema=DatabaseMetadata.SCHEMA_SRH)
public class TipoDeficiencia extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID", nullable=false)
	private Long id;

	@Column(name="DESCRICAO", nullable=false)
	private String descricao;

	
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}


	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}