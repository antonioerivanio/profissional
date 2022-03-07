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

import org.hibernate.validator.constraints.Length;
//import javax.validation.constraints.NotNull;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_ADMISSAO", schema=DatabaseMetadata.SCHEMA_SRH)
public class Admissao extends BasicEntity<Long> implements Serializable{

	@Id
	@Column(name = "ID")	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
	
	@Column(name = "REFERENCIA")
	private String referencia;
	
	@Column(name = "CPF_TRAB")
	private String cpfTrab;
	
	@Column(name = "NM_TRAB")
	private String nmTrab;
	
	@Column(name = "SEXO")
	private Character sexo;
	
	@Column(name = "RACA_COR")
	private Byte racaCor;
	
	@Column(name = "EST_CIV")
	private Byte estCiv;
	
	@Column(name = "GRAU_INSTR")
	private String grauInstr;
	
	@Column(name = "NM_SOCIAL")
	private String nmSoc;
	
	@Column(name = "DT_NASC")
	@Temporal(TemporalType.DATE)
	private Date dtNascto;
	
	@Column(name = "PAIS_NASC")
	private String paisNascto;
	
	@Column(name = "PAIS_NAC")
	private String paisNac;
	
	@Column(name = "TP_LOGRAD")
	private String tpLograd;
	
	@Column(name = "DSC_LOGRAD")
	private String dscLograd;
	
	@Column(name = "NR_LOGRAD")
	private String nrLograd;
	
	@Column(name = "COMPLEMENTO")
	private String complemento;
	
	@Column(name = "BAIRRO")
	private String bairro;
	
	@Column(name = "CEP")
	private String cep;
	
	@Column(name = "COD_MUNIC_END")
	private String codMunic;
	
	@Column(name = "UF_END")
	private String uf;	
	
	@Column(name = "FONE_PRINC")
	private String fonePrinc;
	
	@Column(name = "EMAIL_PRINC")
	private String emailPrinc;
	
	@Column(name = "DEF_FISICA")
	private Character defFisica;
	
	@Column(name = "DEF_VISUAL")
	private Character defVisual;
	
	@Column(name = "DEF_AUDITIVA")
	private Character defAuditiva;
	
	@Column(name = "DEF_MENTAL")
	private Character defMental;
	
	@Column(name = "DEF_INTELECTUAL")
	private Character defIntelectual;
	
	@Column(name = "DEF_REAB_READAP")
	private Character reabReadap;
	
	@Column(name = "DEF_INFO_COTA")
	private Character infoCota;
	
	@Column(name = "DEF_OBESERVACAO")
	@Length(min = 1, max = 255, message = "Tamanho fora do permitido para Observação")
	private String observacao;
	
	@Column(name = "MATRICULA")
	private String matricula;	
	
	@Column(name = "TP_REG_TRAB")
	private Byte tpRegTrab;
	
	@Column(name = "TP_REG_PREV")
	private Byte tpRegPrev;
	
	@Column(name = "CAD_INI")
	private Character cadIni;
	
	@Column(name = "TP_PROV")
	private Byte tpProv;
	
	@Column(name = "DT_EXERCICIO")
	@Temporal(TemporalType.DATE)
	private Date dtExercicio;
	
	@Column(name = "TP_PLAN_RP")
	private Byte tpPlanRP;
	
	@Column(name = "IND_TETO_RGPS")
	private Character indTetoRGPS;
	
	@Column(name = "IND_ABONO_PERM")
	private Character indAbonoPerm;
	
	@Column(name = "DT_INI_ABONO")
	@Temporal(TemporalType.DATE)
	private Date dtIniAbono;
	
	@Column(name = "NM_CARGO")
	private String nmCargo;
	
	@Column(name = "CBO_CARGO")
	private String CBOCargo;
	
	@Column(name = "DT_INGR_CARGO")
	@Temporal(TemporalType.DATE)
	private Date dtIngrCargo;
	
	@Column(name = "NM_FUNCAO")
	private String nmFuncao;
	
	@Column(name = "CBO_FUNCAO")
	private String CBOFuncao;
	
	@Column(name = "ACUM_CARGO")
	private Character acumCargo;
	
	@Column(name = "COD_CATEG")
	private Integer codCateg;
	
	@Column(name = "LTRAB_GERAL_TP_INSC")
	private Byte tpInsc;

	@Column(name = "LTRAB_GERAL_NR_INSC")
	private String nrInsc;
	
	@Column(name = "LTRAB_GERAL_DESC_COMP")
	private String descComp;
	
	@Column(name = "VR_SAL_FX")
	private Float vrSalFx;
	
	@Column(name = "UND_SAL_FIXO")
	private Byte undSalFixo;

	@Column(name = "DSC_SAL_VAR")
	private String dscSalVar;
	

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

	public String getCpfTrab() {
		return cpfTrab;
	}

	public void setCpfTrab(String cpfTrab) {
		this.cpfTrab = cpfTrab;
	}

	public String getNmTrab() {
		return nmTrab;
	}

	public void setNmTrab(String nmTrab) {
		this.nmTrab = nmTrab;
	}

	public Character getSexo() {
		return sexo;
	}

	public void setSexo(Character sexo) {
		this.sexo = sexo;
	}

	public Byte getRacaCor() {
		return racaCor;
	}

	public void setRacaCor(Byte racaCor) {
		this.racaCor = racaCor;
	}

	public Byte getEstCiv() {
		return estCiv;
	}

	public void setEstCiv(Byte estCiv) {
		this.estCiv = estCiv;
	}

	public String getGrauInstr() {
		return grauInstr;
	}

	public void setGrauInstr(String grauInstr) {
		this.grauInstr = grauInstr;
	}

	public String getNmSoc() {
		return nmSoc;
	}

	public void setNmSoc(String nmSoc) {
		this.nmSoc = nmSoc;
	}

	public Date getDtNascto() {
		return dtNascto;
	}

	public void setDtNascto(Date dtNascto) {
		this.dtNascto = dtNascto;
	}

	public String getPaisNascto() {
		return paisNascto;
	}

	public void setPaisNascto(String paisNascto) {
		this.paisNascto = paisNascto;
	}

	public String getPaisNac() {
		return paisNac;
	}

	public void setPaisNac(String paisNac) {
		this.paisNac = paisNac;
	}

	public String getTpLograd() {
		return tpLograd;
	}

	public void setTpLograd(String tpLograd) {
		this.tpLograd = tpLograd;
	}

	public String getDscLograd() {
		return dscLograd;
	}

	public void setDscLograd(String dscLograd) {
		this.dscLograd = dscLograd;
	}

	public String getNrLograd() {
		return nrLograd;
	}

	public void setNrLograd(String nrLograd) {
		this.nrLograd = nrLograd;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getCodMunic() {
		return codMunic;
	}

	public void setCodMunic(String codMunic) {
		this.codMunic = codMunic;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getFonePrinc() {
		return fonePrinc;
	}

	public void setFonePrinc(String fonePrinc) {
		this.fonePrinc = fonePrinc;
	}

	public String getEmailPrinc() {
		return emailPrinc;
	}

	public void setEmailPrinc(String emailPrinc) {
		this.emailPrinc = emailPrinc;
	}

	public Character getDefFisica() {
		return defFisica;
	}

	public void setDefFisica(Character defFisica) {
		this.defFisica = defFisica;
	}

	public Character getDefVisual() {
		return defVisual;
	}

	public void setDefVisual(Character defVisual) {
		this.defVisual = defVisual;
	}

	public Character getDefAuditiva() {
		return defAuditiva;
	}

	public void setDefAuditiva(Character defAuditiva) {
		this.defAuditiva = defAuditiva;
	}

	public Character getDefMental() {
		return defMental;
	}

	public void setDefMental(Character defMental) {
		this.defMental = defMental;
	}

	public Character getDefIntelectual() {
		return defIntelectual;
	}

	public void setDefIntelectual(Character defIntelectual) {
		this.defIntelectual = defIntelectual;
	}

	public Character getReabReadap() {
		return reabReadap;
	}

	public void setReabReadap(Character reabReadap) {
		this.reabReadap = reabReadap;
	}

	public Character getInfoCota() {
		return infoCota;
	}

	public void setInfoCota(Character infoCota) {
		this.infoCota = infoCota;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Byte getTpRegTrab() {
		return tpRegTrab;
	}

	public void setTpRegTrab(Byte tpRegTrab) {
		this.tpRegTrab = tpRegTrab;
	}

	public Byte getTpRegPrev() {
		return tpRegPrev;
	}

	public void setTpRegPrev(Byte tpRegPrev) {
		this.tpRegPrev = tpRegPrev;
	}

	public Character getCadIni() {
		return cadIni;
	}

	public void setCadIni(Character cadIni) {
		this.cadIni = cadIni;
	}

	public Byte getTpProv() {
		return tpProv;
	}

	public void setTpProv(Byte tpProv) {
		this.tpProv = tpProv;
	}

	public Date getDtExercicio() {
		return dtExercicio;
	}

	public void setDtExercicio(Date dtExercicio) {
		this.dtExercicio = dtExercicio;
	}

	public Byte getTpPlanRP() {
		return tpPlanRP;
	}

	public void setTpPlanRP(Byte tpPlanRP) {
		this.tpPlanRP = tpPlanRP;
	}

	public Character getIndTetoRGPS() {
		return indTetoRGPS;
	}

	public void setIndTetoRGPS(Character indTetoRGPS) {
		this.indTetoRGPS = indTetoRGPS;
	}

	public Character getIndAbonoPerm() {
		return indAbonoPerm;
	}

	public void setIndAbonoPerm(Character indAbonoPerm) {
		this.indAbonoPerm = indAbonoPerm;
	}

	public Date getDtIniAbono() {
		return dtIniAbono;
	}

	public void setDtIniAbono(Date dtIniAbono) {
		this.dtIniAbono = dtIniAbono;
	}

	public String getNmCargo() {
		return nmCargo;
	}

	public void setNmCargo(String nmCargo) {
		this.nmCargo = nmCargo;
	}

	public String getCBOCargo() {
		return CBOCargo;
	}

	public void setCBOCargo(String cBOCargo) {
		CBOCargo = cBOCargo;
	}

	public Date getDtIngrCargo() {
		return dtIngrCargo;
	}

	public void setDtIngrCargo(Date dtIngrCargo) {
		this.dtIngrCargo = dtIngrCargo;
	}

	public String getNmFuncao() {
		return nmFuncao;
	}

	public void setNmFuncao(String nmFuncao) {
		this.nmFuncao = nmFuncao;
	}

	public String getCBOFuncao() {
		return CBOFuncao;
	}

	public void setCBOFuncao(String cBOFuncao) {
		CBOFuncao = cBOFuncao;
	}

	public Character getAcumCargo() {
		return acumCargo;
	}

	public void setAcumCargo(Character acumCargo) {
		this.acumCargo = acumCargo;
	}

	public Integer getCodCateg() {
		return codCateg;
	}

	public void setCodCateg(Integer codCateg) {
		this.codCateg = codCateg;
	}

	public Byte getTpInsc() {
		return tpInsc;
	}

	public void setTpInsc(Byte tpInsc) {
		this.tpInsc = tpInsc;
	}

	public String getNrInsc() {
		return nrInsc;
	}

	public void setNrInsc(String nrInsc) {
		this.nrInsc = nrInsc;
	}

	public String getDescComp() {
		return descComp;
	}

	public void setDescComp(String descComp) {
		this.descComp = descComp;
	}

	public Float getVrSalFx() {
		return vrSalFx;
	}

	public void setVrSalFx(Float vrSalFx) {
		this.vrSalFx = vrSalFx;
	}

	public Byte getUndSalFixo() {
		return undSalFixo;
	}

	public void setUndSalFixo(Byte undSalFixo) {
		this.undSalFixo = undSalFixo;
	}

	public String getDscSalVar() {
		return dscSalVar;
	}

	public void setDscSalVar(String dscSalVar) {
		this.dscSalVar = dscSalVar;
	}
	
	
}
