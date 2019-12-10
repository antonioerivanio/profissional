package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_UF
 * 
 * @since   : Sep 29, 2011, 12:02:00 AM
 * @author  : robson.castro@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_UF", schema=DatabaseMetadata.SCHEMA_SRH)
public class Uf extends BasicEntity<String> implements Serializable {

	@Id
	@Column(name="UF")
	private String id;

	@Column(name="NOME", length=25, nullable=false)
	private String nome;


	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	@Override
	public String getId() {return id;}
	@Override
	public void setId(String id) {this.id = id;}

}
