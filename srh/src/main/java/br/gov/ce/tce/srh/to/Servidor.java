package br.gov.ce.tce.srh.to;

import java.io.Serializable;
import java.math.BigDecimal;


public class Servidor implements Serializable{

	private static final long serialVersionUID = 21425673916585101L;
	private String nomeSetor;
	private String nomeCompleto;
	private String cargo;
	private String referencia;
	private String representacao;
	private String simbolo;
	private BigDecimal ROWNUM_;
	private BigDecimal hierarquia;
	private BigDecimal nrOrdemSetorFolha;
	private BigDecimal idFolha;
	private BigDecimal ordemOcupacao;

	public String getNomeSetor() {
		return nomeSetor;
	}

	public void setNomeSetor(String nomeSetor) {
		this.nomeSetor = nomeSetor;
	}

	public String getNomeCompleto() {
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto) {
		this.nomeCompleto = nomeCompleto;
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getRepresentacao() {
		return representacao;
	}

	public void setRepresentacao(String representacao) {
		this.representacao = representacao;
	}

	public String getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(String simbolo) {
		this.simbolo = simbolo;
	}

	public BigDecimal getROWNUM_() {
		return ROWNUM_;
	}

	public void setROWNUM_(BigDecimal ROWNUM_) {
		this.ROWNUM_ = ROWNUM_;
	}

	public BigDecimal getHierarquia() {
		return hierarquia;
	}

	public void setHierarquia(BigDecimal hierarquia) {
		this.hierarquia = hierarquia;
	}

	public BigDecimal getNrOrdemSetorFolha() {
		return nrOrdemSetorFolha;
	}

	public void setNrOrdemSetorFolha(BigDecimal nrOrdemSetorFolha) {
		this.nrOrdemSetorFolha = nrOrdemSetorFolha;
	}

	public BigDecimal getIdFolha() {
		return idFolha;
	}

	public void setIdFolha(BigDecimal idFolha) {
		this.idFolha = idFolha;
	}

	public BigDecimal getOrdemOcupacao() {
		return ordemOcupacao;
	}

	public void setOrdemOcupacao(BigDecimal ordemOcupacao) {
		this.ordemOcupacao = ordemOcupacao;
	}
	
	
}
