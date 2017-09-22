package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name="TB_SUBTIPOTEMPOSERVICO", schema="SRH")
public class SubtipoTempoServico extends BasicEntity<Long> implements Serializable{
	
	@Id
	@Column(name="IDSUBTIPOTEMPOSERVICO")
	private Long id;
	
	@Column(name="IDTIPOTEMPOSERVICO")
	private Long idTipoTempoServico;	

	@Column(name="DSSUBTIPOTEMPOSERVICO")
	private String descricao;

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}

	public Long getIdTipoTempoServico() {return idTipoTempoServico;}
	public void setIdTipoTempoServico(Long idTipoTempoServico) {this.idTipoTempoServico = idTipoTempoServico;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}	

}
