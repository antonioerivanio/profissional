package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_TIPOMOVIMENTO
 * 
 * @since   : Sep 1, 2011, 18:10:09 AM
 * @author  : robson.castro@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_TIPOMOVIMENTO", schema="SRH")
public class TipoMovimento extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="DESCRICAO")
	private String descricao;

	@Column(name="ABREVIATURA")
	private String abreviatura;

	@Column(name="TIPO")
	private Long tipo;

	@Column(name="IDSITUACAO")
	private Long idSituacao;
	
	
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public String getAbreviatura() {return abreviatura;}
	public void setAbreviatura(String abreviatura) {this.abreviatura = abreviatura;}

	public Long getTipo() {return tipo;}
	public void setTipo(Long tipo) {this.tipo = tipo;}
	
	public Long getIdSituacao() {return idSituacao;}
	public void setIdSituacao(Long idSituacao) {this.idSituacao = idSituacao;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}