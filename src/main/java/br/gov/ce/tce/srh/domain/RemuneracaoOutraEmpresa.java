package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_REMUNOUTRAEMPRESA", schema=DatabaseMetadata.SCHEMA_SRH)
public class RemuneracaoOutraEmpresa extends BasicEntity<Long> implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_REMUNOUTRAEMPRESA")
	@SequenceGenerator(name="SEQ_REMUNOUTRAEMPRESA", sequenceName="SEQ_REMUNOUTRAEMPRESA", schema=DatabaseMetadata.SCHEMA_SRH, allocationSize=1, initialValue=1)
	@Column(name = "ID")	
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "IDREMUNERACAOTRABALHADOR")
	private RemuneracaoTrabalhador remuneracaoTrabalhador;
	
	@Column(name = "TP_INSC_OUTR_EMPR")
	private Byte tpInscOutrEmpr;
	
	@Column(name = "NR_INSC_OUTR_EMPR")
	private String nrInscOutrEmpr;
	
	@Column(name = "COD_CATEG_OUTR_EMPR")
	private Integer codCategOutrEmpr;
	
	@Column(name="VLR_REMUN_OE")
	private BigDecimal vlrRemunOE;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public RemuneracaoTrabalhador getRemuneracaoTrabalhador() {
		return remuneracaoTrabalhador;
	}

	public void setRemuneracaoTrabalhador(RemuneracaoTrabalhador remuneracaoTrabalhador) {
		this.remuneracaoTrabalhador = remuneracaoTrabalhador;
	}

	@Override
    public RemuneracaoOutraEmpresa clone() throws CloneNotSupportedException {
        return (RemuneracaoOutraEmpresa) super.clone();
    }
	
}
