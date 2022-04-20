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
@Table(name = "ESOCIAL_BENEFICIARIO", schema=DatabaseMetadata.SCHEMA_SRH)
public class Beneficiario extends BasicEntity<Long> implements Serializable{
 
	@Id
	@Column(name = "ID")	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
	
	@Column(name = "REFERENCIA")
	private String referencia;
	
	@Column(name = "CPF_BENEF")
	private String cpfTrab;
	
	@Column(name = "NM_BENEFIC")
	private String nmTrab;
	
	@Column(name = "SEXO")
	private Character sexo;
	
	@Column(name = "RACA_COR")
	private Byte racaCor;
	
	@Column(name = "EST_CIV")
	private Byte estCiv;
	
	@Column(name = "DT_NASCTO")
	@Temporal(TemporalType.DATE)
	private Date dtNascto;
	
	@Column(name = "DT_INICIO")
	@Temporal(TemporalType.DATE)
	private Date dtInicio;
	
	@Column(name = "INC_FIS_MEN")
	private Character incFisMen;
	
	@Column(name = "DT_INC_FIS_MEN")
	@Temporal(TemporalType.DATE)
	private Date dtIncFisMen;
	
	@Column(name = "TP_LOGRAD")
	private String tpLograd;
	
	@Column(name = "DSC_LOGRAD")
	private String dscLograd;
	
	@Column(name = "NR_LOGRAD")
	private String nrLograd;
	
	@Column(name = "COMPLEMENTO")
	private String complemento;
	
	@Column(name = "BAIRRO_EX")
	private String bairroEx;
	
	@Column(name = "COD_POSTAL")
	private String codPostal;
	
	@Column(name = "NM_CID")
	private String nmCid;
	
	@Column(name = "UF_END")
	private String uf;	

	@Column(name = "DSC_LOGRAD_EX")
	private String dscLogradEx;
	
	@Column(name = "NRLOGRAD_EX")
	private String nrLogradEx;
	
	@Column(name = "COMPLEMENTO_EX")
	private String complementoEx;
	
	@Column(name = "BAIRRO")
	private String bairro;
	
	@Column(name = "CEP")
	private String cep;
	
	@Column(name = "COD_MUNIC_END")
	private String codMunic;
	
	@Column(name = "PAIS_RESID")
	private String paisResid;	
	
	
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

	public Date getDtNascto() {
		return dtNascto;
	}

	public void setDtNascto(Date dtNascto) {
		this.dtNascto = dtNascto;
	}

	public Date getDtInicio() {
		return dtInicio;
	}

	public void setDtInicio(Date dtInicio) {
		this.dtInicio = dtInicio;
	}

	public Character getIncFisMen() {
		return incFisMen;
	}

	public void setIncFisMen(Character incFisMen) {
		this.incFisMen = incFisMen;
	}

	public Date getDtIncFisMen() {
		return dtIncFisMen;
	}

	public void setDtIncFisMen(Date dtIncFisMen) {
		this.dtIncFisMen = dtIncFisMen;
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

	public String getBairroEx() {
		return bairroEx;
	}

	public void setBairroEx(String bairroEx) {
		this.bairroEx = bairroEx;
	}

	public String getCodPostal() {
		return codPostal;
	}

	public void setCodPostal(String codPostal) {
		this.codPostal = codPostal;
	}

	public String getNmCid() {
		return nmCid;
	}

	public void setNmCid(String nmCid) {
		this.nmCid = nmCid;
	}

	public String getUf() {
		return uf;
	}

	public void setUf(String uf) {
		this.uf = uf;
	}

	public String getDscLogradEx() {
		return dscLogradEx;
	}

	public void setDscLogradEx(String dscLogradEx) {
		this.dscLogradEx = dscLogradEx;
	}

	public String getNrLogradEx() {
		return nrLogradEx;
	}

	public void setNrLogradEx(String nrLogradEx) {
		this.nrLogradEx = nrLogradEx;
	}

	public String getComplementoEx() {
		return complementoEx;
	}

	public void setComplementoEx(String complementoEx) {
		this.complementoEx = complementoEx;
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

	public String getPaisResid() {
		return paisResid;
	}

	public void setPaisResid(String paisResid) {
		this.paisResid = paisResid;
	}
	
	
	
}
