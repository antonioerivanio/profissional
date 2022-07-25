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
public class RemuneracaoTrabalhador extends BasicEntity<Long> implements Serializable, Cloneable {

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
	
	@Column(name = "NM_TRAB_DESC")
	private String nmTrabDesc;
	
	@Column(name = "NM_TRAB")
	private String nmTrab;
	
	@Column(name = "DT_NASCTO")
	@Temporal(TemporalType.DATE)
	private Date dtNascto;
	
	
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
	
	public String getNmTrabDesc() {
		return nmTrabDesc;
	}

	public void setNmTrabDesc(String nmTrabDesc) {
		this.nmTrabDesc = nmTrabDesc;
	}

	@Override
    public RemuneracaoTrabalhador clone() throws CloneNotSupportedException {
        return (RemuneracaoTrabalhador) super.clone();
    }
	
}
