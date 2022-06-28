/**
 * 
 */
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
 * @author joel.barbosa
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_ATESTOPESSOA", schema=DatabaseMetadata.SCHEMA_SRH)
public class AtestoPessoa extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "IDPESSOA")
	private Pessoal pessoal;
	
	@ManyToOne
	@JoinColumn(name = "IDRESPONSAVEL")
	private RepresentacaoFuncional responsavel;
	
	@ManyToOne
	@JoinColumn(name = "IDCOMPETENCIA")
	private Competencia competencia;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "INICIO")
	private Date dataInicio;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "FIM")
	private Date dataFim;

	@Column(name="OBSERVACAO")
	private String observacao;

	
	public Pessoal getPessoal() {return pessoal;}
	public void setPessoal(Pessoal pessoal) {this.pessoal = pessoal;}
	
	public RepresentacaoFuncional getResponsavel() {return responsavel;}
	public void setResponsavel(RepresentacaoFuncional responsavel) {this.responsavel = responsavel;}

	public Competencia getCompetencia() {return competencia;}
	public void setCompetencia(Competencia competencia) {this.competencia = competencia;}

	public Date getDataInicio() {return dataInicio;}
	public void setDataInicio(Date dataInicio) {this.dataInicio = dataInicio;}

	public Date getDataFim() {return dataFim;}
	public void setDataFim(Date dataFim) {this.dataFim = dataFim;}

	public String getObservacao() {return observacao;}
	public void setObservacao(String observacao) {this.observacao = observacao;}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}