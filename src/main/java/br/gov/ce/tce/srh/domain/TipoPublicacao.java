package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_TIPOPUBLICACAO
 * 
 * @since   : Nov 20, 2011, 12:55:02 AM
 * @author  : robson.castro@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_TIPOPUBLICACAO", schema=DatabaseMetadata.SCHEMA_SRH)
public class TipoPublicacao extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="DESCRICAO")
	private String descricao;

	@Column(name="PREFIXO")
	private String prefixo;


	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public String getPrefixo() {return prefixo;}
	public void setPrefixo(String prefixo) {this.prefixo = prefixo;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}