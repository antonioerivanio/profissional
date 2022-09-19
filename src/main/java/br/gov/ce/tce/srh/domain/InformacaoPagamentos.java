package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_INFOPAGAMENTOS", schema=DatabaseMetadata.SCHEMA_SRH)
public class InformacaoPagamentos  extends BasicEntity<Long> implements Serializable, Cloneable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_INFOPAGAMENTOS")
	@SequenceGenerator(name="SEQ_INFOPAGAMENTOS", sequenceName="SEQ_INFOPAGAMENTOS", schema=DatabaseMetadata.SCHEMA_SRH, allocationSize=1, initialValue=1)
	@Column(name = "ID")	
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "IDPAGAMENTOS")
	private Pagamentos pagamentos;
	
	@Column(name = "IDE_DM_DEV")
	private String ideDmDev;
	
	@Column(name = "DT_PGTO")
	@Temporal(TemporalType.DATE)
	private Date dtPgto;
	
	@Column(name = "TP_PGTO")
	private Byte tpPgto;
	
	@Column(name = "PER_REF")
	private String perRef;
	
	@Column(name="VR_LIQ")
	private BigDecimal vrLiq;
	
	public String getIdeDmDev() {
		return ideDmDev;
	}

	public void setIdeDmDev(String ideDmDev) {
		this.ideDmDev = ideDmDev;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Pagamentos getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(Pagamentos pagamentos) {
		this.pagamentos = pagamentos;
	}

	public Date getDtPgto() {
		return dtPgto;
	}

	public void setDtPgto(Date dtPgto) {
		this.dtPgto = dtPgto;
	}

	public Byte getTpPgto() {
		return tpPgto;
	}

	public void setTpPgto(Byte tpPgto) {
		this.tpPgto = tpPgto;
	}

	public String getPerRef() {
		return perRef;
	}

	public void setPerRef(String perRef) {
		this.perRef = perRef;
	}

	public BigDecimal getVrLiq() {
		return vrLiq;
	}

	public void setVrLiq(BigDecimal vrLiq) {
		this.vrLiq = vrLiq;
	}

	@Override
    public InformacaoPagamentos clone() throws CloneNotSupportedException {
        return (InformacaoPagamentos) super.clone();
    }

}
