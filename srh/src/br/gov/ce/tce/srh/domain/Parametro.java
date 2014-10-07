package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import br.gov.ce.tce.srh.domain.BasicEntity;

/**
 * Referente a tabela: FWPARAMETER
 * 
 * @author Robstown Holanda, em 07/11/2011
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_PARAMETRO", schema="SRH")
public class Parametro extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID", nullable=false)
	private Long id;

	@Column(name="NOME", nullable=false, length=120)
	private String nome;

	@Column(name="DESCRICAO", nullable=false, length=200)
	private String descricao;

	@Column(name="VALOR", length=2000)
	private String valor;

 
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public String getValor() {return valor;}
	public void setValor(String valor) {this.valor = valor;}

	@Override
	public Long getId() {return this.id;}	
	@Override
	public void setId(Long id) {this.id = id;}

}