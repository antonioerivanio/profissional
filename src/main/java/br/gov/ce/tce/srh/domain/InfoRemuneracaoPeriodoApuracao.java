package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_INFOREMUNPERAPUR", schema=DatabaseMetadata.SCHEMA_SRH)
public class InfoRemuneracaoPeriodoApuracao  extends BasicEntity<Long> implements Serializable, Cloneable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_INFOREMUNPERAPUR")
	@SequenceGenerator(name="SEQ_INFOREMUNPERAPUR", sequenceName="SEQ_INFOREMUNPERAPUR", schema=DatabaseMetadata.SCHEMA_SRH, allocationSize=1, initialValue=1)
	@Column(name = "ID")	
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
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
	
	@OneToMany(mappedBy = "infoRemuneracaoPeriodoApuracao", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private List<ItensRemuneracaoTrabalhador> itensRemun = new ArrayList<>();


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

	public List<ItensRemuneracaoTrabalhador> getItensRemun() {
		return itensRemun;
	}

	public void setItensRemun(List<ItensRemuneracaoTrabalhador> itensRemun) {
		this.itensRemun = itensRemun;
	}

	@Override
    public InfoRemuneracaoPeriodoApuracao clone() throws CloneNotSupportedException {
        return (InfoRemuneracaoPeriodoApuracao) super.clone();
    }

}
