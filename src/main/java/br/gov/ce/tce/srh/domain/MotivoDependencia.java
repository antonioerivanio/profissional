package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_MOTIVODEPENDENCIA
 * 
 * @since   : Sep 9, 2011, 10:55:52 AM
 * @author  : robson.castro@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_MOTIVODEPENDENCIA", schema=DatabaseMetadata.SCHEMA_SRH)
public class MotivoDependencia extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="DESCRICAO", length=30, nullable=false)
	private String descricao;

	@Column(name="TIPO", length=1, nullable=false)
	private Long tipo;

	
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public Long getTipo() {return tipo;}
	public void setTipo(Long tipo) {this.tipo = tipo;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}