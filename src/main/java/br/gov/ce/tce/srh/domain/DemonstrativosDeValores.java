package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_DMDEV", schema=DatabaseMetadata.SCHEMA_SRH)
public class DemonstrativosDeValores  extends BasicEntity<Long> implements Serializable{
	
	@Id
	@Column(name = "ID")	
	private Long id;
	
	@Column(name = "IDE_DM_DEV")
	private String ideDmDev;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDREMUNERACAOTRABALHADOR")
	private RemuneracaoTrabalhador remuneracaoTrabalhador;
	
	@Column(name = "COD_CATEG")
	private Integer codCateg;
	
	@Column(name = "TP_INSC")
	private Byte tpInsc;
	
	@Column(name = "NR_INSC")
	private String nrInsc;
	
	@Column(name = "COD_LOTACAO")
	private String codLotacao;
	
	@Column(name = "QTD_DIAS_AV")
	private Integer qtdDiasAv;
	
	@Column(name = "CEP")
	private String cep;
	
	@Column(name = "MATRICULA")
	private String matricula;
	
	@Column(name = "IND_SIMPLES")
	private Byte indSimples;
	
	@Column(name = "GRAU_EXP")
	private Byte grauEX;
	
	@Column(name = "COD_CBO")
	private String codCBO;
	
	@Column(name = "NAT_ATIVIDADE")
	private Byte natAtividade;
	
	@Column(name = "QTD_DIAS_TRAB")
	private Byte qtdDiasTrab;
	
	
	public String getIdeDmDev() {
		return ideDmDev;
	}

	public void setIdeDmDev(String ideDmDev) {
		this.ideDmDev = ideDmDev;
	}

	public Integer getCodCateg() {
		return codCateg;
	}

	public void setCodCateg(Integer codCateg) {
		this.codCateg = codCateg;
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

	public String getCodLotacao() {
		return codLotacao;
	}

	public void setCodLotacao(String codLotacao) {
		this.codLotacao = codLotacao;
	}

	public Integer getQtdDiasAv() {
		return qtdDiasAv;
	}

	public void setQtdDiasAv(Integer qtdDiasAv) {
		this.qtdDiasAv = qtdDiasAv;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getMatricula() {
		return matricula;
	}

	public void setMatricula(String matricula) {
		this.matricula = matricula;
	}

	public Byte getIndSimples() {
		return indSimples;
	}

	public void setIndSimples(Byte indSimples) {
		this.indSimples = indSimples;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RemuneracaoTrabalhador getRemuneracaoTrabalhador() {
		return remuneracaoTrabalhador;
	}

	public void setRemuneracaoTrabalhador(RemuneracaoTrabalhador remuneracaoTrabalhador) {
		this.remuneracaoTrabalhador = remuneracaoTrabalhador;
	}

	public Byte getGrauEX() {
		return grauEX;
	}

	public void setGrauEX(Byte grauEX) {
		this.grauEX = grauEX;
	}

	public String getCodCBO() {
		return codCBO;
	}

	public void setCodCBO(String codCBO) {
		this.codCBO = codCBO;
	}

	public Byte getNatAtividade() {
		return natAtividade;
	}

	public void setNatAtividade(Byte natAtividade) {
		this.natAtividade = natAtividade;
	}

	public Byte getQtdDiasTrab() {
		return qtdDiasTrab;
	}

	public void setQtdDiasTrab(Byte qtdDiasTrab) {
		this.qtdDiasTrab = qtdDiasTrab;
	}	
	

}
