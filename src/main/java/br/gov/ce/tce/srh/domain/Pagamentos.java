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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_PAGAMENTOS", schema=DatabaseMetadata.SCHEMA_SRH)
public class Pagamentos extends BasicEntity<Long> implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_PAGAMENTOS")
	@SequenceGenerator(name="SEQ_PAGAMENTOS", sequenceName="SEQ_PAGAMENTOS", schema=DatabaseMetadata.SCHEMA_SRH, allocationSize=1, initialValue=1)
	@Column(name = "ID")	
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
	
	@Column(name = "REFERENCIA")
	private String referencia;
	
	@Column(name = "IND_APURACAO")
	private Byte indApuracao;
	
	@Column(name = "PER_APUR")
	private String perApur;
	
	@Column(name = "PER_APUR_COMPETENCIA")
	private String perApurCompetencia;
	
	@Column(name = "NM_TRAB_DESC")
	private String nmTrabDesc;
	
	@Column(name = "CPF_BENEF")
	private String cpfBenef;
	
	@OneToMany(mappedBy = "pagamentos", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<InformacaoPagamentos> informacaoPagamentos = new ArrayList<>();
	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Funcional getFuncional() {
		return funcional;
	}

	public void setFuncional(Funcional funcional) {
		this.funcional = funcional;
	}

	public String getReferencia() {
		return referencia;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public Byte getIndApuracao() {
		return indApuracao;
	}

	public void setIndApuracao(Byte indApuracao) {
		this.indApuracao = indApuracao;
	}

	public String getPerApur() {
		return perApur;
	}

	public void setPerApur(String perApur) {
		this.perApur = perApur;
	}
	
	public String getPerApurCompetencia() {
		return perApurCompetencia;
	}

	public void setPerApurCompetencia(String perApurCompetencia) {
		this.perApurCompetencia = perApurCompetencia;
	}

	public String getNmTrabDesc() {
		return nmTrabDesc;
	}

	public void setNmTrabDesc(String nmTrabDesc) {
		this.nmTrabDesc = nmTrabDesc;
	}	

	public String getCpfBenef() {
		return cpfBenef;
	}

	public void setCpfBenef(String cpfBenef) {
		this.cpfBenef = cpfBenef;
	}

	public List<InformacaoPagamentos> getInformacaoPagamentos() {
		return informacaoPagamentos;
	}

	public void setInformacaoPagamentos(List<InformacaoPagamentos> informacaoPagamentos) {
		this.informacaoPagamentos = informacaoPagamentos;
	}

	@Override
    public Pagamentos clone() throws CloneNotSupportedException {
        return (Pagamentos) super.clone();
    }
	
}
