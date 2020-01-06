package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_VINCULO
 * 
 * @since   : Dez 19, 2011, 11:30:55 AM
 * @author  : robson.castro@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_VINCULO", schema=DatabaseMetadata.SCHEMA_SRH)
public class Vinculo extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="DESCRICAO")
	private String descricao;
	
	@Column(name="FLORGAOORIGEM")
	private Boolean orgaoOrigem;

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}
	
	public Boolean getOrgaoOrigem() {return orgaoOrigem;}
	public void setOrgaoOrigem(Boolean orgaoOrigem) {this.orgaoOrigem = orgaoOrigem;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}

}