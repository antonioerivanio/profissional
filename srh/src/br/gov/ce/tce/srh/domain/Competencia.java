package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_COMPETENCIA
 * 
 * @since   : Ago 22, 2011, 10:55:10 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_COMPETENCIA", schema="SRH")
public class Competencia extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="DESCRICAO", length=150, nullable=false)
	private String descricao;

	@Column(name="ATIVO", nullable=false)
	private boolean ativo;

	@Column(name="TIPO", nullable=false)
	private Long tipo;

	
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public boolean getAtivo() {return ativo;}
	public void setAtivo(boolean ativo) {this.ativo = ativo;}

	public Long getTipo() {return tipo;}
	public void setTipo(Long tipo) {this.tipo = tipo;}


	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}
