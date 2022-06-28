package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

/**
 * Referente a tabela: TB_AREASETOR
 * 
 * @since   : Ago 30, 2011, 10:19:27 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_AREASETOR", schema=DatabaseMetadata.SCHEMA_SRH)
public class AreaSetor extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "IDSETOR")
	private Setor setor;

	@Column(name="DESCRICAO", length=30, nullable=false)
	private String descricao;

	@Column(name="COMPETENCIAMINIMA", nullable=false)
	private boolean competenciaMinima;


	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public boolean getCompetenciaMinima() {return competenciaMinima;}
	public void setCompetenciaMinima(boolean competenciaMinima) {this.competenciaMinima = competenciaMinima;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}
