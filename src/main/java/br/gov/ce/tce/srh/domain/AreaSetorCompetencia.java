package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Referente a tabela: TB_AREASETORCOMPETENCIA
 * 
 * @since   : Ago 31, 2011, 10:11:17 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_AREASETORCOMPETENCIA", schema=DatabaseMetadata.SCHEMA_SRH)
public class AreaSetorCompetencia extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "IDAREASETOR")
	private AreaSetor areaSetor;

	@ManyToOne
	@JoinColumn(name = "IDCOMPETENCIA")
	private Competencia competencia;

	@Temporal(TemporalType.DATE) 
	@Column(name="DATAINICIO")
	private Date dataInicio;

	@Column(name="MOTIVOINICIO")
	private String motivoInicio;

	@Temporal(TemporalType.DATE)
	@Column(name="DATAFIM")
	private Date dataFim;

	@Column(name="MOTIVOFIM")
	private String motivoFim;


	public AreaSetor getAreaSetor() {return areaSetor;}
	public void setAreaSetor(AreaSetor areaSetor) {this.areaSetor = areaSetor;}

	public Competencia getCompetencia() {return competencia;}
	public void setCompetencia(Competencia competencia) {this.competencia = competencia;}

	public Date getDataInicio() {return dataInicio;}
	public void setDataInicio(Date dataInicio) {this.dataInicio = dataInicio;}

	public String getMotivoInicio() {return motivoInicio;}
	public void setMotivoInicio(String motivoInicio) {this.motivoInicio = motivoInicio;}

	public Date getDataFim() {return dataFim;}
	public void setDataFim(Date dataFim) {this.dataFim = dataFim;}

	public String getMotivoFim() {return motivoFim;}
	public void setMotivoFim(String motivoFim) {this.motivoFim = motivoFim;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}

