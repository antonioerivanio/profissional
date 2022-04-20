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
@Table(name="ESOCIAL_DEPENDENTE", schema=DatabaseMetadata.SCHEMA_SRH)

public class DependenteEsocial extends BasicEntity<Long> implements Serializable{

	@Id
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
	
	@Column(name="TP_DEP")
	private String tpDep;
	
	@Column(name="NM_DEP")
	private String nmDep;
	
	@Column(name="DT_NASC")
	@Temporal(TemporalType.DATE)
	private Date dtNascto;
	
	@Column(name="CPF_DEP")
	private String cpfDep;
	
	@Column(name = "SEXO_DEP")
	private Character sexoDep;
	
	@Column(name="DEP_IRRF")
	private Character depIRRF;
	
	@Column(name="DEP_SF")
	private Character depSF;
	
	@Column(name="INC_TRAB")
	private Character incTrab;
	
	@Column(name="INC_FIS_MEN")
	private Character incFisMen;
	
	
	public DependenteEsocial() {
		
	}	
	

	@Override
	public Long getId() {return this.id;}

	@Override
	public void setId(Long id) {this.id = id;}

	public Funcional getFuncional() {
		return funcional;
	}

	public void setFuncional(Funcional funcional) {
		this.funcional = funcional;
	}

	public String getTpDep() {
		return tpDep;
	}

	public void setTpDep(String tpDep) {
		this.tpDep = tpDep;
	}

	public String getNmDep() {
		return nmDep;
	}

	public void setNmDep(String nmDep) {
		this.nmDep = nmDep;
	}

	public Date getDtNascto() {
		return dtNascto;
	}

	public void setDtNascto(Date dtNascto) {
		this.dtNascto = dtNascto;
	}

	public String getCpfDep() {
		return cpfDep;
	}

	public void setCpfDep(String cpfDep) {
		this.cpfDep = cpfDep;
	}

	public Character getSexoDep() {
		return sexoDep;
	}

	public void setSexoDep(Character sexoDep) {
		this.sexoDep = sexoDep;
	}

	public Character getDepIRRF() {
		return depIRRF;
	}

	public void setDepIRRF(Character depIRRF) {
		this.depIRRF = depIRRF;
	}

	public Character getDepSF() {
		return depSF;
	}

	public void setDepSF(Character depSF) {
		this.depSF = depSF;
	}

	public Character getIncTrab() {
		return incTrab;
	}

	public void setIncTrab(Character incTrab) {
		this.incTrab = incTrab;
	}


	public Character getIncFisMen() {
		return incFisMen;
	}

	public void setIncFisMen(Character incFisMen) {
		this.incFisMen = incFisMen;
	}
		

}
