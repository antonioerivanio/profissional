package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_TIPODOCUMENTO
 * 
 * @since   : Sep 1, 2011, 17:30:20 AM
 * @author  : robson.castro@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_TIPODOCUMENTO", schema="SRH")
public class TipoDocumento extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="DESCRICAO", length=100, nullable=false)
	private String descricao;

	@Column(name="ESFERA", nullable=false)
	private Long esfera;

	@Column(name="DOCUMENTOFUNCIONAL", nullable=false)
	private boolean documentoFuncional;


	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public Long getEsfera() {return esfera;}
	public void setEsfera(Long esfera) {this.esfera = esfera;}

	public boolean isDocumentoFuncional() {return documentoFuncional;}
	public void setDocumentoFuncional(boolean documentoFuncional) {this.documentoFuncional = documentoFuncional;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}