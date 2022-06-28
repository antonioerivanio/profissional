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
import javax.persistence.Transient;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_AFASTAMENTO", schema = DatabaseMetadata.SCHEMA_SRH)
public class AfastamentoESocial extends BasicEntity<Long> implements Serializable {
	@Id
	@Column(name = "ID")
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;

	@Column(name = "REFERENCIA")
	private String referencia;

	@Column(name = "RETIFICAR_RECIBO")
	private String retificarRecibo;

	@Column(name = "OCORRENCIA_ID")
	private Long ocorrenciaId;

	@Column(name = "OCORRENCIA_COD_ESTADO")
	private Long ocorrenciaCodEstado;

	@Column(name = "CPF_TRAB")
	private String cpfTrab;

	@Column(name = "MATRICULA")
	private String matricula;

//	@Column(name = "COD_CATEG")
//	private Long codCateg;

//	@Column(name = "TP_INSC")
//	private String tpInsc;

//	@Column(name = "NR_INSC")
//	private String nrInsc;

	@Column(name = "DT_INI_AFAST")
	@Temporal(TemporalType.DATE)
	private Date dtIniAfast;

	@Column(name = "COD_MOT_AFAST")
	private String codMotAfast;

	@Column(name = "OBSERVACAO")
	private String observacao;

	@Column(name = "DT_TERM_AFAST")
	@Temporal(TemporalType.DATE)
	private Date dtTermAfast;
	
	@Transient
	private Licenca licenca;

	public Long getId() {	
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Funcional getFuncional() {
		return funcional;
	}

	public void setFuncional(Funcional funcional) {
		this.funcional = funcional;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getRetificarRecibo() {
		return retificarRecibo;
	}

	public void setRetificarRecibo(String retificarRecibo) {
		this.retificarRecibo = retificarRecibo;
	}

	public Long getOcorrenciaId() {
		return ocorrenciaId;
	}

	public void setOcorrenciaId(Long ocorrenciaId) {
		this.ocorrenciaId = ocorrenciaId;
	}

	public Long getOcorrenciaCodEstado() {
		return ocorrenciaCodEstado;
	}

	public void setOcorrenciaCodEstado(Long ocorrenciaCodEstado) {
		this.ocorrenciaCodEstado = ocorrenciaCodEstado;
	}

	public String getCpfTrab() {
		return cpfTrab;
	}

	public void setCpfTrab(String cpfTrab) {
		this.cpfTrab = cpfTrab;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Date getDtIniAfast() {
		return dtIniAfast;
	}

	public void setDtIniAfast(Date dtIniAfast) {
		this.dtIniAfast = dtIniAfast;
	}

	public String getCodMotAfast() {
		return codMotAfast;
	}

	public void setCodMotAfast(String codMotAfast) {
		this.codMotAfast = codMotAfast;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Date getDtTermAfast() {
		return dtTermAfast;
	}

	public void setDtTermAfast(Date dtTermAfast) {
		this.dtTermAfast = dtTermAfast;
	}

	public Licenca getLicenca() {
		if(licenca != null)
			return licenca;
		
		return null;
	}

	public void setLicenca(Licenca licenca) {
		this.licenca = licenca;
	}

}
