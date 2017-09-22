package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_INSTITUICAO
 * 
 * @since   : Sep 29, 2011, 16:12:19 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_INSTITUICAO", schema="SRH")
public class Instituicao extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="DESCRICAO", length=60, nullable=false)
	private String descricao;

	@Column(name="CURSOSUPERIOR", nullable=false)
	private boolean cursoSuperior;

	
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public boolean isCursoSuperior() {return cursoSuperior;}
	public void setCursoSuperior(boolean cursoSuperior) {this.cursoSuperior = cursoSuperior;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}
