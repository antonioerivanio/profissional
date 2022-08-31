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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_REMUNERACAOSERVIDOR", schema=DatabaseMetadata.SCHEMA_SRH)
public class RemuneracaoServidor extends BasicEntity<Long> implements Serializable, Cloneable {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_REMUNERACAOSERVIDOR")
	@SequenceGenerator(name="SEQ_REMUNERACAOSERVIDOR", sequenceName="SEQ_REMUNERACAOSERVIDOR", schema=DatabaseMetadata.SCHEMA_SRH, allocationSize=1, initialValue=1)
	@Column(name = "ID")	
	private Long id;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "IDPRESTADOR")
	private CadastroPrestador cadastroPrestador;
	
	@Column(name = "REFERENCIA")
	private String referencia;
	
	@Column(name = "IND_APURACAO")
	private Byte indApuracao;
	
	@Column(name = "PER_APUR")
	private String perApur;
	
	@Column(name = "CPF_TRAB")
	private String cpfTrab;
	
	@Column(name = "IND_MV")
	private Byte indMV;
	
	@Column(name = "NM_TRAB_DESC")
	private String nmTrabDesc;
	
	@Column(name = "NM_TRAB")
	private String nmTrab;
	
	@Column(name = "DT_NASCTO")
	@Temporal(TemporalType.DATE)
	private Date dtNascto;
	
	
	@OneToMany(mappedBy = "remuneracaoTrabalhador", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<DemonstrativosDeValores> dmDev = new ArrayList<>();
	
	
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

	public String getCpfTrab() {
		return cpfTrab;
	}

	public void setCpfTrab(String cpfTrab) {
		this.cpfTrab = cpfTrab;
	}

	public Byte getIndMV() {
		return indMV;
	}

	public void setIndMV(Byte indMV) {
		this.indMV = indMV;
	}

	
	public String getNmTrab() {
		return nmTrab;
	}

	public void setNmTrab(String nmTrab) {
		this.nmTrab = nmTrab;
	}

	public Date getDtNascto() {
		return dtNascto;
	}

	public void setDtNascto(Date dtNascto) {
		this.dtNascto = dtNascto;
	}
	
	public String getNmTrabDesc() {
		return nmTrabDesc;
	}

	public void setNmTrabDesc(String nmTrabDesc) {
		this.nmTrabDesc = nmTrabDesc;
	}	


	public List<DemonstrativosDeValores> getDmDev() {
		return dmDev;
	}

	public void setDmDev(List<DemonstrativosDeValores> dmDev) {
		this.dmDev = dmDev;
	}
	
	public CadastroPrestador getCadastroPrestador() {
		return cadastroPrestador;
	}

	public void setCadastroPrestador(CadastroPrestador cadastroPrestador) {
		this.cadastroPrestador = cadastroPrestador;
	}

	@Override
    public RemuneracaoServidor clone() throws CloneNotSupportedException {
        return (RemuneracaoServidor) super.clone();
    }
	
}
