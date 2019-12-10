package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ce.tce.srh.sapjava.domain.Setor;

/**
 * Referente a tabela: SRH.TB_COMPETENCIASETORIAL"
 * 
 * 
 * @author  : carlos.almeida@ivia.com.br
 *
 */
@Entity
@SuppressWarnings("serial")
@Table(name="TB_COMPETENCIASETORIAL", schema=DatabaseMetadata.SCHEMA_SRH)
public class CompetenciaSetorial extends BasicEntity<Long> implements Serializable {
	
	@Id
	@Column(name="ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IDSETOR")
	private Setor setor;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IDCOMPETENCIA")
	private Competencia competencia;
	
	@Column(name="ATIVA", nullable=false)
	private Long ativa;
	
	@Temporal(TemporalType.DATE)
	@Column(name="INICIO")
	private Date inicio;
	
	@Temporal(TemporalType.DATE)
	@Column(name="FIM")
	private Date fim;
	
	@Column(name="OBS", length=200)
	private String observacao;
	
	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Competencia getCompetencia() {
		return competencia;
	}

	public void setCompetencia(Competencia competencia) {
		this.competencia = competencia;
	}

	public Long getAtiva() {
		return ativa;
	}

	public void setAtiva(Long ativa) {
		this.ativa = ativa;
	}

	public Date getInicio() {
		return inicio;
	}

	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}

	public Date getFim() {
		return fim;
	}

	public void setFim(Date fim) {
		this.fim = fim;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
}
