package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_ESPECIALIDADE
 *
 * @since   : Ago 29, 2011, 10:33:52 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="tb_especialidade", schema="SRH")
public class Especialidade extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="DESCRICAO", length=100, nullable=false)
	private String descricao;

	@Column(name="AREA", length=25, nullable=false)
	private String area;

	
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public String getArea() {return area;}
	public void setArea(String area) {this.area = area;}
	
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}
