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


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_BENEFICIO", schema=DatabaseMetadata.SCHEMA_SRH)
public class Beneficio extends BasicEntity<Long> implements Serializable{
 
	@Id
	@Column(name = "ID")	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
	
	@Column(name = "REFERENCIA")
	private String referencia;
	
	@Column(name = "CPF_BENEF")
	private String cpfBenef;
	
	@Column(name = "MATRICULA")
	private String matricula;
	
	@Column(name = "CNPJ_ORIGEM")
	private String cnpjOrigem;
	
	@Column(name = "CAD_INI")
	private Character cadIni;
	
	@Column(name = "INC_SIT_BENEF")
	private Byte indSitBenef;
	
	@Column(name = "NR_BENEFICIO")
	private String nrBeneficio;
	
	@Column(name = "DT_INI_BENEFICIO")
	@Temporal(TemporalType.DATE)
	private Date dtIniBeneficio;
	
	@Column(name = "DT_PUBLIC")
	@Temporal(TemporalType.DATE)
	private Date dtPublic;
	
	@Column(name = "TP_BENEFICIO")
	private String tpBeneficio;
	
	@Column(name = "TP_PLAN_RP")
	private Byte tpPlanRP;
	
	@Column(name = "DSC")
	private String dsc;
	
	@Column(name = "IND_DEC_JUD")
	private Byte indDecJud;
	
	@Column(name = "TP_PEN_MORTE")
	private Byte tpPenMorte;
	
	@Column(name = "CPF_INST")
	private String cpfInst;
	
	@Column(name = "DT_INST")
	@Temporal(TemporalType.DATE)
	private Date dtInst;
	
	@Column(name = "CNPJ_ORGAO_ANT")
	private String cnpjOrgaoAnt;
	
	@Column(name = "NR_BENEFICIO_ANT_SUC")
	private String nrBeneficioAntSuc;
	
	@Column(name = "DT_TRANSF")
	@Temporal(TemporalType.DATE)
	private Date dtTransf;
	
	@Column(name = "OBSERVACAO_SUC")
	private String observacaoSuc;
	
	@Column(name = "CPF_ANT")
	private String cpfAnt;
	
	@Column(name = "NR_BENEFICIO_ANT_MUD")
	private String nrBeneficioAntMud;
	
	@Column(name = "DT_ALT_CPF")
	@Temporal(TemporalType.DATE)
	private Date dtAltCPF;
	
	@Column(name = "OBSERVACAO_MUD")
	private String observacaoMud;
	
	@Column(name = "DT_TERM_BENEFICIO")
	@Temporal(TemporalType.DATE)
	private Date dtTermBeneficio;
	
	@Column(name = "MTV_TERMINO")
	private String mtvTermino;
	
	
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

	public String getCpfBenef() {
		return cpfBenef;
	}

	public void setCpfBenef(String cpfBenef) {
		this.cpfBenef = cpfBenef;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public String getCnpjOrigem() {
		return cnpjOrigem;
	}

	public void setCnpjOrigem(String cnpjOrigem) {
		this.cnpjOrigem = cnpjOrigem;
	}

	public Character getCadIni() {
		return cadIni;
	}

	public void setCadIni(Character cadIni) {
		this.cadIni = cadIni;
	}

	public Byte getIndSitBenef() {
		return indSitBenef;
	}

	public void setIndSitBenef(Byte indSitBenef) {
		this.indSitBenef = indSitBenef;
	}

	public String getNrBeneficio() {
		return nrBeneficio;
	}

	public void setNrBeneficio(String nrBeneficio) {
		this.nrBeneficio = nrBeneficio;
	}

	public Date getDtIniBeneficio() {
		return dtIniBeneficio;
	}

	public void setDtIniBeneficio(Date dtIniBeneficio) {
		this.dtIniBeneficio = dtIniBeneficio;
	}

	public Date getDtPublic() {
		return dtPublic;
	}

	public void setDtPublic(Date dtPublic) {
		this.dtPublic = dtPublic;
	}

	public String getTpBeneficio() {
		return tpBeneficio;
	}

	public void setTpBeneficio(String tpBeneficio) {
		this.tpBeneficio = tpBeneficio;
	}

	public Byte getTpPlanRP() {
		return tpPlanRP;
	}

	public void setTpPlanRP(Byte tpPlanRP) {
		this.tpPlanRP = tpPlanRP;
	}

	public String getDsc() {
		return dsc;
	}

	public void setDsc(String dsc) {
		this.dsc = dsc;
	}

	public Byte getIndDecJud() {
		return indDecJud;
	}

	public void setIndDecJud(Byte indDecJud) {
		this.indDecJud = indDecJud;
	}

	public Byte getTpPenMorte() {
		return tpPenMorte;
	}

	public void setTpPenMorte(Byte tpPenMorte) {
		this.tpPenMorte = tpPenMorte;
	}

	public String getCpfInst() {
		return cpfInst;
	}

	public void setCpfInst(String cpfInst) {
		this.cpfInst = cpfInst;
	}

	public Date getDtInst() {
		return dtInst;
	}

	public void setDtInst(Date dtInst) {
		this.dtInst = dtInst;
	}

	public String getCnpjOrgaoAnt() {
		return cnpjOrgaoAnt;
	}

	public void setCnpjOrgaoAnt(String cnpjOrgaoAnt) {
		this.cnpjOrgaoAnt = cnpjOrgaoAnt;
	}

	public String getNrBeneficioAntSuc() {
		return nrBeneficioAntSuc;
	}

	public void setNrBeneficioAntSuc(String nrBeneficioAntSuc) {
		this.nrBeneficioAntSuc = nrBeneficioAntSuc;
	}

	public Date getDtTransf() {
		return dtTransf;
	}

	public void setDtTransf(Date dtTransf) {
		this.dtTransf = dtTransf;
	}

	public String getObservacaoSuc() {
		return observacaoSuc;
	}

	public void setObservacaoSuc(String observacaoSuc) {
		this.observacaoSuc = observacaoSuc;
	}

	public String getCpfAnt() {
		return cpfAnt;
	}

	public void setCpfAnt(String cpfAnt) {
		this.cpfAnt = cpfAnt;
	}

	public String getNrBeneficioAntMud() {
		return nrBeneficioAntMud;
	}

	public void setNrBeneficioAntMud(String nrBeneficioAntMud) {
		this.nrBeneficioAntMud = nrBeneficioAntMud;
	}

	public Date getDtAltCPF() {
		return dtAltCPF;
	}

	public void setDtAltCPF(Date dtAltCPF) {
		this.dtAltCPF = dtAltCPF;
	}

	public String getObservacaoMud() {
		return observacaoMud;
	}

	public void setObservacaoMud(String observacaoMud) {
		this.observacaoMud = observacaoMud;
	}

	public Date getDtTermBeneficio() {
		return dtTermBeneficio;
	}

	public void setDtTermBeneficio(Date dtTermBeneficio) {
		this.dtTermBeneficio = dtTermBeneficio;
	}

	public String getMtvTermino() {
		return mtvTermino;
	}

	public void setMtvTermino(String mtvTermino) {
		this.mtvTermino = mtvTermino;
	}

	
}
