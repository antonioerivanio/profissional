package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.Length;


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_ALTERACAOCADASTRAL", schema=DatabaseMetadata.SCHEMA_SRH)
public class AlteracaoCadastral extends BasicEntity<Long> implements Serializable{

	@Id
	@Column(name = "ID")	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
	
	@Column(name = "REFERENCIA")
	private String referencia;

	//ideEmpregador - Informações de identificação do empregador.
	@Column(name = "TP_INSC")
	private Byte tpInsc;
	
	@Column(name = "NR_INSC")
	private String nrInsc;
	
	//ideTrabalhador - Identificação do trabalhador.
	@Column(name = "CPF_TRAB")
	private String cpfTrab;

	//dadosTrabalhador - Informações pessoais do trabalhador.
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
	
	@Column(name = "PAIS_NAC")
	private String paisNac;	
	
	/** endereco - Grupo de informações do endereço do trabalhador. 
	 * brasil - Endereço no Brasil.
	 */
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
	
	@Column(name = "UF")
	private String uf;	
	
	//infoDeficiencia - Pessoa com deficiência.	
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
	
	//contato - Informações de contato
	@Column(name = "FONE_PRINC")
	private String fonePrinc;
	
	@Column(name = "EMAIL_PRINC")
	private String emailPrinc;

	@Transient
	private List<DependenteEsocial> dependentesList;
	
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

	public List<DependenteEsocial> getDependentesList() {
		return dependentesList;
	}

	public void setDependentesList(List<DependenteEsocial> dependentesList) {
		this.dependentesList = dependentesList;
	}
}
