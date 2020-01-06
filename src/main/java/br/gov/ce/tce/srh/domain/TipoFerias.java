package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_TIPOFERIAS
 * 
 * @since   : Sep 1, 2011, 17:02:27 AM
 * @author  : robson.castro@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_TIPOFERIAS", schema=DatabaseMetadata.SCHEMA_SRH)
public class TipoFerias extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="DESCRICAO", length=100, nullable=false)
	private String descricao;

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
	public boolean consideraSomenteQtdeDias() {	
		// Férias ressalvadas (id = 4)
		// Férias contadas em dobro (id = 5)
		// Férias indenizadas (id = 6)
		if(id.intValue() >= 4 && id.intValue() <= 6)
			return true;
		
		return false;
	}
	
}