package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.math.BigDecimal;
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

import org.hibernate.validator.constraints.Length;


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_REMUNERACAOTRABALHADOR", schema=DatabaseMetadata.SCHEMA_SRH)
public class RemuneracaoTrabalhador extends BasicEntity<Long> implements Serializable{

	@Id
	@Column(name = "ID")	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
	
	@Column(name = "REFERENCIA")
	private String referencia;
	
	@Column(name = "IND_APURACAO")
	private Byte indApuracao;
	
	@Column(name = "PER_APUR")
	private String perApur;
	
	@Column(name = "CPF_TRAB")
	private String cpfTrab;
	
	@Column(name = "IND_MV")
	private Byte indMV;
	
	@Column(name = "TP_INSC_OUTR_EMPR")
	private Byte tpInscOutrEmpr;
	
	@Column(name = "NR_INSC_OUTR_EMPR")
	private String nrInscOutrEmpr;
	
	@Column(name = "COD_CATEG_OUTR_EMPR")
	private Integer codCategOutrEmpr;
	
	@Column(name="VLR_REMUN_OE")
	private BigDecimal vlrRemunOE;
	
	@Column(name = "NM_TRAB")
	private String nmTrab;
	
	@Column(name = "DT_NASCTO")
	@Temporal(TemporalType.DATE)
	private Date dtNascto;
	
	@Column(name = "TP_INSC_SUC_VINC")
	private Byte tpInscSucVinc;
	
	@Column(name = "NR_INSC_SUC_VINC")
	private String nrInscSucVinc;

	@Column(name = "MATRIC_ANT")
	private String matriculaAnt;	
	
	@Column(name = "DT_ADM")
	@Temporal(TemporalType.DATE)
	private Date dtAdm;
		
	@Column(name = "OBSERVACAO")
	@Length(min = 1, max = 255, message = "Tamanho fora do permitido para Observação")
	private String observacao;
	
	@Column(name = "TP_TRIB")
	private Byte tpTrib;
	
	@Column(name = "NR_PROC_JUD")
	private String nrProcJud;
	
	@Column(name = "COD_SUSP")
	private Integer codSusp;
	@Transient
	private String nome;

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

	public Byte getIndApuracao() {
		return indApuracao;
	}

	public void setIndApuracao(Byte indApuracao) {
		this.indApuracao = indApuracao;
	}

	public String getPerApur() {
		return perApur;
	}

	public void setPerApur(String perApur) {
		this.perApur = perApur;
	}

	public String getCpfTrab() {
		return cpfTrab;
	}

	public void setCpfTrab(String cpfTrab) {
		this.cpfTrab = cpfTrab;
	}

	public Byte getIndMV() {
		return indMV;
	}

	public void setIndMV(Byte indMV) {
		this.indMV = indMV;
	}

	public Byte getTpInscOutrEmpr() {
		return tpInscOutrEmpr;
	}

	public void setTpInscOutrEmpr(Byte tpInscOutrEmpr) {
		this.tpInscOutrEmpr = tpInscOutrEmpr;
	}

	public String getNrInscOutrEmpr() {
		return nrInscOutrEmpr;
	}

	public void setNrInscOutrEmpr(String nrInscOutrEmpr) {
		this.nrInscOutrEmpr = nrInscOutrEmpr;
	}

	public Integer getCodCategOutrEmpr() {
		return codCategOutrEmpr;
	}

	public void setCodCategOutrEmpr(Integer codCategOutrEmpr) {
		this.codCategOutrEmpr = codCategOutrEmpr;
	}

	public BigDecimal getVlrRemunOE() {
		return vlrRemunOE;
	}

	public void setVlrRemunOE(BigDecimal vlrRemunOE) {
		this.vlrRemunOE = vlrRemunOE;
	}

	public String getNmTrab() {
		return nmTrab;
	}

	public void setNmTrab(String nmTrab) {
		this.nmTrab = nmTrab;
	}

	public Date getDtNascto() {
		return dtNascto;
	}

	public void setDtNascto(Date dtNascto) {
		this.dtNascto = dtNascto;
	}

	public Byte getTpInscSucVinc() {
		return tpInscSucVinc;
	}

	public void setTpInscSucVinc(Byte tpInscSucVinc) {
		this.tpInscSucVinc = tpInscSucVinc;
	}

	public String getNrInscSucVinc() {
		return nrInscSucVinc;
	}

	public void setNrInscSucVinc(String nrInscSucVinc) {
		this.nrInscSucVinc = nrInscSucVinc;
	}

	public String getMatriculaAnt() {
		return matriculaAnt;
	}

	public void setMatriculaAnt(String matriculaAnt) {
		this.matriculaAnt = matriculaAnt;
	}

	public Date getDtAdm() {
		return dtAdm;
	}

	public void setDtAdm(Date dtAdm) {
		this.dtAdm = dtAdm;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public Byte getTpTrib() {
		return tpTrib;
	}

	public void setTpTrib(Byte tpTrib) {
		this.tpTrib = tpTrib;
	}

	public String getNrProcJud() {
		return nrProcJud;
	}

	public void setNrProcJud(String nrProcJud) {
		this.nrProcJud = nrProcJud;
	}

	public Integer getCodSusp() {
		return codSusp;
	}

	public void setCodSusp(Integer codSusp) {
		this.codSusp = codSusp;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}	
	
}
