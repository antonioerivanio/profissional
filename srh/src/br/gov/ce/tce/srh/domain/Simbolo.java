package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_SIMBOLO
 * 
 * @since   : Sep 13, 2011, 15:14:00 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_SIMBOLO", schema="SRH")
public class Simbolo extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "IDOCUPACAO")
	private Ocupacao ocupacao;

	@Column(name="SIMBOLO", length=5, nullable=false)
	private String simbolo;

	public Ocupacao getOcupacao() {return ocupacao;}
	public void setOcupacao(Ocupacao ocupacao) {this.ocupacao = ocupacao;}

	public String getSimbolo() {return simbolo;}
	public void setSimbolo(String simbolo) {this.simbolo = simbolo;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}

}