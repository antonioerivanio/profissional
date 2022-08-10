package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name="FP_DADOSPAGTOPRESTADOR", schema=DatabaseMetadata.SCHEMA_SRH)
public class DadosPagtoPrestador extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="IDPAGTOPRESTADOR")
	private Long id;
	
	@ManyToOne
	@JoinColumn(name = "IDPRESTADOR")
	private CadastroPrestador cadastroPrestador;

	@Column(name="NOME")
	private String nome;
	
	@Column(name="NOVOCBO")
	private String novoCBO;
	
	@Column(name="VALOR_BRUTO")
	private BigDecimal vlrBruto;
	
	@Column(name="VALOR_INSS")
	private BigDecimal vlrInss;
	
	@Column(name="VALOR_INSS_ORIGEM")
	private BigDecimal vlrInssOrigem;
	
	@Column(name="VALOR_ISS")
	private BigDecimal vlrIss;
	
	@Column(name="VALOR_IRRF")
	private BigDecimal vlrIrrf;
	

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public CadastroPrestador getCadastroPrestador() {
		return cadastroPrestador;
	}

	public void setCadastroPrestador(CadastroPrestador cadastroPrestador) {
		this.cadastroPrestador = cadastroPrestador;
	}

	public String getNovoCBO() {
		return novoCBO;
	}

	public void setNovoCBO(String novoCBO) {
		this.novoCBO = novoCBO;
	}

	public BigDecimal getVlrBruto() {
		return vlrBruto;
	}

	public void setVlrBruto(BigDecimal vlrBruto) {
		this.vlrBruto = vlrBruto;
	}

	public BigDecimal getVlrInss() {
		return vlrInss;
	}

	public void setVlrInss(BigDecimal vlrInss) {
		this.vlrInss = vlrInss;
	}

	public BigDecimal getVlrInssOrigem() {
		return vlrInssOrigem;
	}

	public void setVlrInssOrigem(BigDecimal vlrInssOrigem) {
		this.vlrInssOrigem = vlrInssOrigem;
	}

	public BigDecimal getVlrIss() {
		return vlrIss;
	}

	public void setVlrIss(BigDecimal vlrIss) {
		this.vlrIss = vlrIss;
	}

	public BigDecimal getVlrIrrf() {
		return vlrIrrf;
	}

	public void setVlrIrrf(BigDecimal vlrIrrf) {
		this.vlrIrrf = vlrIrrf;
	}
	
}