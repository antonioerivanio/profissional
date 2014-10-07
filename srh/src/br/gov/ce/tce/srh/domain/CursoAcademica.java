package br.gov.ce.tce.srh.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_CURSOFORMACAO
 * 
 * @since   : Sep 9, 2011, 14:19:20 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_CURSOFORMACAO", schema="SRH")
public class CursoAcademica extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID", nullable=false)
	private Long id;

	@ManyToOne//(fetch=FetchType.EAGER)
	@JoinColumn(name = "IDAREAFORMACAO")
	private AreaAcademica area;

	@Column(name="DESCRICAO", nullable=false, length=60)
	private String descricao;


	public AreaAcademica getArea() {return area;}
	public void setArea(AreaAcademica area) {this.area = area;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}
	
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}