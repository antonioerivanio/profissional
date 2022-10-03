package br.gov.ce.tce.srh.domain;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

@Entity
public class AdmissaoVO {

	@Id
	private String referencia;

	private String cpfTrab;

	private String nmTrab;

	private Character sexo;

	private Byte racaCor;

	private Byte estCiv;

	private String grauInstr;

	private Date dtNascto;

	private String nmSoc;

	private String paisNascto;

	private String paisNac;

	private String tpLograd;

	private String dscLograd;

	private String nrLograd;

	private String complemento;

	private String bairro;

	private String cep;

	private String codMunic;

	private String uf;

	private String fonePrinc;

	private String emailPrinc;

	private Character defFisica;

	private Character defVisual;

	private Character defAuditiva;

	private Character defMental;

	private Character defIntelectual;

	private Character reabReadap;

	private Character infoCota;

	private String observacao;

	private String matricula;

	private Byte tpRegTrab;

	private Byte tpRegPrev;

	private Character cadIni;

	private Byte tpProv;

	private Date dtExercicio;

	private Byte tpPlanRP;

	private Character indTetoRGPS;

	private Character indAbonoPerm;

	private Date dtIniAbono;

	private String nmCargo;

	private String CBOCargo;

	private Date dtIngrCargo;

	private String nmFuncao;

	private String CBOFuncao;

	private Character acumCargo;

	private Integer codCateg;

	private Byte tpInsc;

	private String nrInsc;

	private String descComp;

	private Float vrSalFx;

	private Byte undSalFixo;

	private String dscSalVar;
	
	@Transient
	private List<DependenteEsocial> dependentesList;

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
	

	public List<DependenteEsocial> getDependentesList() {
		return dependentesList;
	}

	public void setDependentesList(List<DependenteEsocial> dependentesList) {
		this.dependentesList = dependentesList;
	}

	@Override
	public int hashCode() {
		HashCodeBuilder builder = new HashCodeBuilder();
		builder.append(CBOCargo);
		builder.append(CBOFuncao);
		builder.append(acumCargo);
		builder.append(bairro);
		builder.append(cadIni);
		builder.append(codCateg);
		builder.append(codMunic);
		builder.append(complemento);
		builder.append(cpfTrab);
		builder.append(defAuditiva);
		builder.append(defFisica);
		builder.append(defIntelectual);
		builder.append(defMental);
		builder.append(descComp);
		builder.append(descComp);
		builder.append(dscLograd);
		builder.append(dscSalVar);
		builder.append(dtExercicio);
		builder.append(dtIngrCargo);
		builder.append(dtIniAbono);
		builder.append(dtNascto);
		builder.append(emailPrinc);
		builder.append(estCiv);
		builder.append(fonePrinc);
		builder.append(grauInstr);
		builder.append(indAbonoPerm);
		builder.append(indTetoRGPS);
		builder.append(infoCota);
		builder.append(matricula);
		builder.append(nmCargo);
		builder.append(nmFuncao);
		builder.append(nmSoc);
		builder.append(nmTrab);
		builder.append(nrInsc);
		builder.append(nrLograd);
		builder.append(observacao);
		builder.append(paisNac);
		builder.append(paisNascto);
		builder.append(racaCor);
		builder.append(reabReadap);
		builder.append(referencia);
		builder.append(sexo);
		builder.append(tpInsc);
		builder.append(tpLograd);
		builder.append(tpPlanRP);
		builder.append(tpProv);
		builder.append(tpRegPrev);
		builder.append(tpRegTrab);
		builder.append(uf);
		builder.append(undSalFixo);
		builder.append(vrSalFx);
		builder.append(dependentesList);
		
		return builder.toHashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AdmissaoVO other = (AdmissaoVO) obj;

		EqualsBuilder builder = new EqualsBuilder();
		builder.append(CBOCargo, other.CBOCargo);
		builder.append(CBOFuncao, other.CBOFuncao);
		builder.append(acumCargo, other.acumCargo);
		builder.append(bairro, other.bairro);
		builder.append(cadIni, other.cadIni);
		builder.append(codCateg, other.codCateg);
		builder.append(codMunic, other.codMunic);
		builder.append(complemento, other.complemento);
		builder.append(cpfTrab, other.cpfTrab);
		builder.append(defAuditiva, other.defAuditiva);
		builder.append(defFisica, other.defFisica);
		builder.append(defIntelectual, other.defIntelectual);
		builder.append(defMental, other.defMental);
		builder.append(descComp, other.descComp);
		builder.append(descComp, other.descComp);
		builder.append(dscLograd, other.dscLograd);
		builder.append(dscSalVar, other.dscSalVar);
		builder.append(dtExercicio, other.dtExercicio);
		builder.append(dtIngrCargo, other.dtIngrCargo);
		builder.append(dtIniAbono, other.dtIniAbono);
		builder.append(dtNascto, other.dtNascto);
		builder.append(emailPrinc, other.emailPrinc);
		builder.append(estCiv, other.estCiv);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(fonePrinc, other.fonePrinc);
		builder.append(grauInstr, other.grauInstr);
		builder.append(indAbonoPerm, other.indAbonoPerm);
		builder.append(indTetoRGPS, other.indTetoRGPS);
		builder.append(infoCota, other.infoCota);
		builder.append(matricula, other.matricula);
		builder.append(nmCargo, other.nmCargo);
		builder.append(nmFuncao, other.nmFuncao);
		builder.append(nmSoc, other.nmSoc);
		builder.append(nmTrab, other.nmTrab);
		builder.append(nrInsc, other.nrInsc);
		builder.append(nrLograd, other.nrLograd);
		builder.append(observacao, other.observacao);
		builder.append(paisNac, other.paisNac);
		builder.append(paisNascto, other.paisNascto);
		builder.append(racaCor, other.racaCor);
		builder.append(reabReadap, other.reabReadap);
		builder.append(referencia, other.referencia);
		builder.append(sexo, other.sexo);
		builder.append(tpInsc, other.tpInsc);
		builder.append(tpLograd, other.tpLograd);
		builder.append(tpPlanRP, other.tpPlanRP);
		builder.append(tpProv, other.tpProv);
		builder.append(tpRegPrev, other.tpRegPrev);
		builder.append(tpRegTrab, other.tpRegTrab);
		builder.append(uf, other.uf);
		builder.append(undSalFixo, other.undSalFixo);
		builder.append(vrSalFx, other.vrSalFx);
		builder.append(dependentesList, other.dependentesList);
		return builder.isEquals();
	}

}
