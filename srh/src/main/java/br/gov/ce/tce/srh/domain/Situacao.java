package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_SITUACAO
 * 
 * @since   : Dez 7, 2011, 18:35:00 AM
 * @author  : robson.castro@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_SITUACAO", schema="SRH")
public class Situacao extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	private Long id;
	
	@Column(name="DESCRICAO")
	private String descricao;


	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}