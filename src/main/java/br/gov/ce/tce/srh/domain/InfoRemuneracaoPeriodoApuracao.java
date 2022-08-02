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
@Table(name = "ESOCIAL_INFOREMUNPERAPUR", schema=DatabaseMetadata.SCHEMA_SRH)
public class InfoRemuneracaoPeriodoApuracao  extends BasicEntity<Long> implements Serializable, Cloneable {
	
	@Id
	@Column(name = "ID")	
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDDMDEV")
	private DemonstrativosDeValores demonstrativosDeValores;
	
	@Column(name = "TP_INSC")
	private Byte tpInsc;
	
	@Column(name = "NR_INSC")
	private String nrInsc;
	
	@Column(name = "COD_LOTACAO")
	private String codLotacao;
	
	@Column(name = "MATRICULA")
	private String matricula;
	
	@Column(name = "IND_SIMPLES")
	private Byte indSimples;
	
	@Column(name = "GRAU_EXP")
	private Byte grauEX;


	public DemonstrativosDeValores getDemonstrativosDeValores() {
		return demonstrativosDeValores;
	}

	public void setDemonstrativosDeValores(DemonstrativosDeValores demonstrativosDeValores) {
		this.demonstrativosDeValores = demonstrativosDeValores;
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

	public Byte getGrauEX() {
		return grauEX;
	}

	public void setGrauEX(Byte grauEX) {
		this.grauEX = grauEX;
	}

	@Override
    public InfoRemuneracaoPeriodoApuracao clone() throws CloneNotSupportedException {
        return (InfoRemuneracaoPeriodoApuracao) super.clone();
    }

}
