package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_CBO
 * 
 * @since   : Out 3, 2011, 14:19:18 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_CBO", schema=DatabaseMetadata.SCHEMA_SRH)
public class Cbo extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="CODIGO")
	private String codigo;

	@Column(name="DESCRICAO")
	private String descricao;

	@Column(name="NIVEL")
	private Long nivel;


	public String getCodigo() {return codigo;}
	public void setCodigo(String codigo) {this.codigo = codigo;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public Long getNivel() {return nivel;}
	public void setNivel(Long nivel) {this.nivel = nivel;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}