package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_INFOREMUNPERANTERIORES", schema=DatabaseMetadata.SCHEMA_SRH)
public class InfoRemuneracaoPeriodoAnteriores extends BasicEntity<Long> implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_INFOREMUNPERANTERIORES")
	@SequenceGenerator(name="SEQ_INFOREMUNPERANTERIORES", sequenceName="SEQ_INFOREMUNPERANTERIORES", schema=DatabaseMetadata.SCHEMA_SRH, allocationSize=1, initialValue=1)
	@Column(name = "ID")	
	private Long id;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDDMDEV")
	private DemonstrativosDeValores demonstrativosDeValores;
	
	@Column(name = "DT_AC_CONV")
	@Temporal(TemporalType.DATE)
	private Date dtAcConv;
	
	@Column(name = "TP_AC_CONV")
	private Character tpAcConv;
	
	@Column(name = "DSC")
	private String dsc;
	
	@Column(name = "REMUN_SUC")
	private Character remunSuc;
	
	@Column(name = "PER_REF")
	private String perRef;

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
	
	@OneToMany(mappedBy = "infoRemuneracaoPeriodoAnteriores", fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
	private List<ItensRemuneracaoTrabalhador> itensRemun = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public DemonstrativosDeValores getDemonstrativosDeValores() {
		return demonstrativosDeValores;
	}

	public void setDemonstrativosDeValores(DemonstrativosDeValores demonstrativosDeValores) {
		this.demonstrativosDeValores = demonstrativosDeValores;
	}
	
	public Date getDtAcConv() {
		return dtAcConv;
	}

	public void setDtAcConv(Date dtAcConv) {
		this.dtAcConv = dtAcConv;
	}

	public Character getTpAcConv() {
		return tpAcConv;
	}

	public void setTpAcConv(Character tpAcConv) {
		this.tpAcConv = tpAcConv;
	}

	public String getDsc() {
		return dsc;
	}

	public void setDsc(String dsc) {
		this.dsc = dsc;
	}

	public Character getRemunSuc() {
		return remunSuc;
	}

	public void setRemunSuc(Character remunSuc) {
		this.remunSuc = remunSuc;
	}

	public String getPerRef() {
		return perRef;
	}

	public void setPerRef(String perRef) {
		this.perRef = perRef;
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
    public InfoRemuneracaoPeriodoAnteriores clone() throws CloneNotSupportedException {
        return (InfoRemuneracaoPeriodoAnteriores) super.clone();
    }
}
