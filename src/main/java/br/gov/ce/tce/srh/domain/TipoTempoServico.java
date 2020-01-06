package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name="TB_TIPOTEMPOSERVICO", schema=DatabaseMetadata.SCHEMA_SRH)
public class TipoTempoServico extends BasicEntity<Long> implements Serializable{
	
	@Id
	@Column(name="IDTIPOTEMPOSERVICO")
	private Long id;

	@Column(name="DSTIPOTEMPOSERVICO")
	private String descricao;

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}	

}
