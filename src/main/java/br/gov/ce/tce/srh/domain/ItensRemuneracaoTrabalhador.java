package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_INFOREMUNPERANTERIORES", schema=DatabaseMetadata.SCHEMA_SRH)
public class ItensRemuneracaoTrabalhador extends BasicEntity<Long> implements Serializable, Cloneable {

	@Id
	@Column(name = "ID")	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDINFOREMUNPERAPUR")
	private InfoRemuneracaoPeriodoApuracao infoRemuneracaoPeriodoApuracao;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDINFOREMUNPERANTERIORES")
	private InfoRemuneracaoPeriodoAnteriores infoRemuneracaoPeriodoAnteriores;
	
	@Column(name = "COD_RUBR")
	private String codRubr;
	
	@Column(name = "IDE_TAB_RUBR")
	private String ideTabRubr;
	
	@Column(name = "QTD_RUBR")
	private Integer qtdRubr;
	
	@Column(name = "FATOR_RUBR")
	private Integer fatorRubr;
	
	@Column(name="VR_RUBR")
	private BigDecimal vlrRubr;
	
	@Column(name = "IND_APUR_IR")
	private Byte indApurIr;
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public InfoRemuneracaoPeriodoApuracao getInfoRemuneracaoPeriodoApuracao() {
		return infoRemuneracaoPeriodoApuracao;
	}

	public void setInfoRemuneracaoPeriodoApuracao(InfoRemuneracaoPeriodoApuracao infoRemuneracaoPeriodoApuracao) {
		this.infoRemuneracaoPeriodoApuracao = infoRemuneracaoPeriodoApuracao;
	}

	public InfoRemuneracaoPeriodoAnteriores getInfoRemuneracaoPeriodoAnteriores() {
		return infoRemuneracaoPeriodoAnteriores;
	}

	public void setInfoRemuneracaoPeriodoAnteriores(InfoRemuneracaoPeriodoAnteriores infoRemuneracaoPeriodoAnteriores) {
		this.infoRemuneracaoPeriodoAnteriores = infoRemuneracaoPeriodoAnteriores;
	}

	public String getCodRubr() {
		return codRubr;
	}

	public void setCodRubr(String codRubr) {
		this.codRubr = codRubr;
	}

	public BigDecimal getVlrRubr() {
		return vlrRubr;
	}

	public void setVlrRubr(BigDecimal vlrRubr) {
		this.vlrRubr = vlrRubr;
	}

	public String getIdeTabRubr() {
		return ideTabRubr;
	}

	public void setIdeTabRubr(String ideTabRubr) {
		this.ideTabRubr = ideTabRubr;
	}

	public Integer getQtdRubr() {
		return qtdRubr;
	}

	public void setQtdRubr(Integer qtdRubr) {
		this.qtdRubr = qtdRubr;
	}

	public Integer getFatorRubr() {
		return fatorRubr;
	}

	public void setFatorRubr(Integer fatorRubr) {
		this.fatorRubr = fatorRubr;
	}

	public Byte getIndApurIr() {
		return indApurIr;
	}

	public void setIndApurIr(Byte indApurIr) {
		this.indApurIr = indApurIr;
	}
	
	@Override
    public ItensRemuneracaoTrabalhador clone() throws CloneNotSupportedException {
        return (ItensRemuneracaoTrabalhador) super.clone();
    }
		
}
