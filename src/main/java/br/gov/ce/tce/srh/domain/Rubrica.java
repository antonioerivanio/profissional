package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Referente a tabela: TB_RUBRICA
 * 
 * @since   : Set 1, 2011, 10:40:00 AM
 * @author  : robstownholanda@ivia.com.br
 *
 */
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
	private boolean emprestimo;

	@Column(name="CONSIGNACAO")
	private boolean consignacao;

	@Column(name="SUPSEC")
	private boolean supsec;

	@Column(name="IRRF")
	private boolean irrf;

	@Column(name="VERBA_EXTRA")
	private boolean verbaExtra;

	@Column(name="VERIFICATETO")
	private boolean verificaTeto;


	public String getCodigo() {return codigo;}
	public void setCodigo(String codigo) {this.codigo = codigo;}

	public Long getOrdem() {return ordem;}
	public void setOrdem(Long ordem) {this.ordem = ordem;}

	public String getTipo() {return tipo;}
	public void setTipo(String tipo) {this.tipo = tipo;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public boolean isEmprestimo() {return emprestimo;}
	public void setEmprestimo(boolean emprestimo) {this.emprestimo = emprestimo;}

	public boolean isConsignacao() {return consignacao;}
	public void setConsignacao(boolean consignacao) {this.consignacao = consignacao;}

	public boolean isSupsec() {return supsec;}
	public void setSupsec(boolean supsec) {this.supsec = supsec;}

	public boolean isIrrf() {return irrf;	}
	public void setIrrf(boolean irrf) {this.irrf = irrf;}

	public boolean isVerbaExtra() {return verbaExtra;}
	public void setVerbaExtra(boolean verbaExtra) {this.verbaExtra = verbaExtra;}

	public boolean isVerificaTeto() {return verificaTeto;}
	public void setVerificaTeto(boolean verificaTeto) {this.verificaTeto = verificaTeto;}
	
	@Override
	public Long getId() {return this.id;}
	@Override
	public void setId(Long id) {this.id = id;}
	
}