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

/**
 * Esta class foi criada com base nas informações da tabela de estágiario(#link EstagiarioESocial)
 * @author erivanio.cruz
 * @since   08/04/2022
 */
@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_ESTAGIARIO", schema=DatabaseMetadata.SCHEMA_SRH)
public class EstatutarioESocial extends BasicEntity<Long> implements Serializable{
	
	
	@Id
	@Column(name="ID")
	private Long id;
//
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
//	
	@Column(name = "REFERENCIA")
	private String referencia;
	
	@Column(name = "LTRAB_GERAL_TP_INSC")
	private String tpInsc;
	
	@Column(name = "LTRAB_GERAL_NR_INSC")
	private String nrInsc;
	
	@Column(name = "RETIFICAR_RECIBO")
	private String retificarRecibo;
//	
	@Column(name = "OCORRENCIA_ID")
	private Long ocorrenciaId;
//	
	@Column(name = "OCORRENCIA_COD_ESTADO")
	private Long ocorrenciaCodEstado;
//	
	@Column(name = "CPF_TRAB")
	private String cpfTrab;
//	
	@Column(name = "NM_TRAB")
	private String nmTrab;
//	
	@Column(name = "SEXO")
	private Character sexo;
//	
	@Column(name = "RACA_COR")
	private Byte racaCor;
	
	@Column(name = "EST_CIV")
	private Byte estCiv;
	
	@Column(name = "GRAU_INSTR")
	private String grauInstr;

	@Column(name = "DT_NASC")
	@Temporal(TemporalType.DATE)
	private Date dtNascto;
	
//	@Column(name = "COD_MUNIC_NASC")
//	private Long codMunicNasc;
//	
//	@Column(name = "UF_NASC")
//	private Long ufNasc;
//	
	@Column(name = "PAIS_NASC")
	private String paisNascto;
	
	@Column(name = "PAIS_NAC")
	private String paisNac;
//	
//	@Column(name = "NM_MAE")
//	private String nmMae;
//	
//	@Column(name = "NM_PAI")
//	private String nmPai;
	
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
	
	@Column(name = "MATRICULA")
	private String matricula;

	
	@Column(name = "DT_INICIO")
	@Temporal(TemporalType.DATE)
	private Date dtInicio;
	

	@Column(name = "CAD_INI")
	private Character cadIni;
	
	@Column(name = "COD_CATEG")
	private Long codCateg;
	

	@Column(name = "VR_SAL_FX")
	private Double vrSalFx;
	
	@Column(name="UND_SAL_FIXO")
	private Byte undSalFixo;
	
	@Column(name = "NM_SOC")
	private String nmSoc;
	
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
	
	@Column(name = "NM_CARGO")
	private String nmCargo;
	
	@Column(name = "CBO_CARGO")
	private String CBOCargo;
	
	@Column(name="NM_FUNCAO")
	private String nmFuncao;
	
	@Column(name = "CBO_FUNCAO")
	private String CBOFuncao;
	
	
//	@Column(name = "NAT_ATIVIDADE")
//	private String natAtividade;
	
	@Column(name="DT_TERMINO")
	@Temporal(TemporalType.DATE)
	private Date dtTerm;
	
//	@Column(name = "CNPJ_AGNT_INTEG")
//	private String cnpjAgntInteg;
//	
//	private String cpfSupervisor;
//	
//	@Column(name = "DSC_SAL_VAR")
//	private String dscSalVar;
//	
//	@Column(name = "FL_EXCLUSAO")
//	private Long flExclusao;
//	
//	@Column(name="FONE_ALTERNAT")
//	private String foneAlternat;
//	
//	@Column(name="DEF_INfO_COTA")
//	private Character defInfoCota;
//	
//	@Column(name = "INI_VALID")
//	private String iniValid;
//	
//	@Column(name="DEF_OBSERVACAO")
//	private String defObservacao;
//	
//	@Column(name="UF_NAC")
//	private Long ufNac;
	
	@Column(name="CATEG_ORIG")
	private Long categOrig;
	
	@Column(name="CNPJ_CEDNT")
	private String cnpjCednt;
	
	@Column(name="MATRIC_CED")
	private String matricCed;
	
	@Column(name="DT_ADM_CED")
	@Temporal(TemporalType.DATE)
	private Date dtAdmCed;
	
	@Column(name="TP_REG_TRAB")
	private Long tpRegTrab;
	
	@Column(name="TP_REG_PREV")
	private Long tpRegPrev;
	
	public Date getDtNascto() {
		return dtNascto;
	}
	public void setDtNascto(Date dtNascto) {
		this.dtNascto = dtNascto;
	}
	public Date getDtTerm() {
		return dtTerm;
	}
	public void setDtTerm(Date dtTerm) {
		this.dtTerm = dtTerm;
	}
	public Date getDtInicio() {
		return dtInicio;
	}
	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
	}
	
	public String getReferencia() {
		return referencia;
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
	
	public Character getCadIni() {
		return cadIni;
	}
	public void setCadIni(Character cadIni) {
		this.cadIni = cadIni;
	}
	public void setNmSoc(String nmSoc) {
		this.nmSoc = nmSoc;
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
	
	public String getTpInsc() {
		return tpInsc;
	}
	public void setTpInsc(String tpInsc) {
		this.tpInsc = tpInsc;
	}
	public String getNrInsc() {
		return nrInsc;
	}
	public void setNrInsc(String nrInsc) {
		this.nrInsc = nrInsc;
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
	public Double getVrSalFx() {
		return vrSalFx;
	}
	
	public void setVrSalFx(Double vrSalFx) {
		this.vrSalFx = vrSalFx;
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
	public String getNmSoc() {
		return nmSoc;
	}
	public void setNmSocial(String nmSoc) {
		this.nmSoc = nmSoc;
	}
	public Long getCodCateg() {
		return codCateg;
	}
	public void setCodCateg(Long codCateg) {
		this.codCateg = codCateg;
	}
	public Byte getUndSalFixo() {
		return undSalFixo;
	}
	public void setUndSalFixo(Byte undSalFixo) {
		this.undSalFixo = undSalFixo;
	}
	public Long getCategOrig() {
		return categOrig;
	}
	public void setCategOrig(Long categOrig) {
		this.categOrig = categOrig;
	}
	public String getCnpjCednt() {
		return cnpjCednt;
	}
	public void setCnpjCednt(String cnpjCednt) {
		this.cnpjCednt = cnpjCednt;
	}
	public String getMatricCed() {
		return matricCed;
	}
	public void setMatricCed(String matricCed) {
		this.matricCed = matricCed;
	}
	public Date getDtAdmCed() {
		return dtAdmCed;
	}
	public void setDtAdmCed(Date dtAdmCed) {
		this.dtAdmCed = dtAdmCed;
	}
	public Long getTpRegTrab() {
		return tpRegTrab;
	}
	public void setTpRegTrab(Long tpRegTrab) {
		this.tpRegTrab = tpRegTrab;
	}
	public Long getTpRegPrev() {
		return tpRegPrev;
	}
	public void setTpRegPrev(Long tpRegPrev) {
		this.tpRegPrev = tpRegPrev;
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
}