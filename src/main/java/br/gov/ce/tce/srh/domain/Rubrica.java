package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name="TB_RUBRICA", schema="SRH")
public class Rubrica extends BasicEntity<Long> implements Serializable {

	@Id
	@Column(name="ID")
	private Long id;

	@Column(name="CODIGO", nullable=false)
	private String codigo;

	@Column(name="ORDEM", nullable=false)
	private Long ordem;

	@Column(name="TIPO", length=1, nullable=false)
	private String tipo;

	@Column(name="DESCRICAO", length=30, nullable=false)
	private String descricao;

	@Column(name="EMPRESTIMO")
	private Boolean emprestimo;

	@Column(name="CONSIGNACAO")
	private Boolean consignacao;

	@Column(name="SUPSEC")
	private Boolean supsec;

	@Column(name="IRRF")
	private Boolean irrf;

	@Column(name="VERBA_EXTRA")
	private Boolean verbaExtra;

	@Column(name="VERIFICATETO")
	private Boolean verificaTeto;
	
	@Column(name="AGRUPADESCONTO")
	private Boolean agrupaDesconto;

	@Column(name="PROPTERLABOREM")
	private Boolean propterlaborem ;

	@Column(name="VERBAPREVIDENCIA")
	private Boolean verbaPrevidencia;

	@Column(name="AUXILIO")
	private Boolean auxilio;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Long getOrdem() {
		return ordem;
	}

	public void setOrdem(Long ordem) {
		this.ordem = ordem;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Boolean getEmprestimo() {
		return emprestimo;
	}

	public void setEmprestimo(Boolean emprestimo) {
		this.emprestimo = emprestimo;
	}

	public Boolean getConsignacao() {
		return consignacao;
	}

	public void setConsignacao(Boolean consignacao) {
		this.consignacao = consignacao;
	}

	public Boolean getSupsec() {
		return supsec;
	}

	public void setSupsec(Boolean supsec) {
		this.supsec = supsec;
	}

	public Boolean getIrrf() {
		return irrf;
	}

	public void setIrrf(Boolean irrf) {
		this.irrf = irrf;
	}

	public Boolean getVerbaExtra() {
		return verbaExtra;
	}

	public void setVerbaExtra(Boolean verbaExtra) {
		this.verbaExtra = verbaExtra;
	}

	public Boolean getVerificaTeto() {
		return verificaTeto;
	}

	public void setVerificaTeto(Boolean verificaTeto) {
		this.verificaTeto = verificaTeto;
	}

	public Boolean getAgrupaDesconto() {
		return agrupaDesconto;
	}

	public void setAgrupaDesconto(Boolean agrupaDesconto) {
		this.agrupaDesconto = agrupaDesconto;
	}

	public Boolean getPropterlaborem() {
		return propterlaborem;
	}

	public void setPropterlaborem(Boolean propterlaborem) {
		this.propterlaborem = propterlaborem;
	}

	public Boolean getVerbaPrevidencia() {
		return verbaPrevidencia;
	}

	public void setVerbaPrevidencia(Boolean verbaPrevidencia) {
		this.verbaPrevidencia = verbaPrevidencia;
	}

	public Boolean getAuxilio() {
		return auxilio;
	}

	public void setAuxilio(Boolean auxilio) {
		this.auxilio = auxilio;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}	
	
}