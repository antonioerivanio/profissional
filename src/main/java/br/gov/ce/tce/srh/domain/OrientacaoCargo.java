package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_ORIENTACAOCARGO
 * 
 * @since   : Jan 29, 2014, 10:00:00 AM
 * @author  : andre.santos@tce.ce.gov.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_ORIENTACAOCARGO", schema=DatabaseMetadata.SCHEMA_SRH)
public class OrientacaoCargo extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "IDESPECIALIDADE")
	private Especialidade especialidade;

	@JoinColumn(name = "DESCRICAO")
	private String descricao;
	
	@JoinColumn(name = "QTDE")
	private Long qtde;

	public Especialidade getEspecialidade() {return especialidade;}
	public void setEspecialidade(Especialidade especialidade) {this.especialidade = especialidade;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}
	
	public Long getQtde() {return qtde;}
	public void setQtde(Long qtde) {this.qtde = qtde;}
	
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}