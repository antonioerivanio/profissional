package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_MUNICIPIO
 * 
 * @since   : Apr 17, 2011, 12:09:15 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_MUNICIPIO", schema="SRH")
public class Municipio extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name="UF")	
	private Uf uf;
	
	@Column(name="NOME", length=200, nullable=false)
	private String nome;
	
	@Column(name="COD_MUNICIPIO")
	private String codigoIBGE;


	public Uf getUf() {return uf;}
	public void setUf(Uf uf) {this.uf = uf;}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public String getCodigoIBGE() {	return codigoIBGE; }
	public void setCodigoIBGE(String codigoIBGE) { this.codigoIBGE = codigoIBGE; }
	
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}