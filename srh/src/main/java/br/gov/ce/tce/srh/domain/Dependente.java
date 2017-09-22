package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@SuppressWarnings("serial")
@Table(name="TB_DEPENDENTE", schema="SRH")
@NamedQueries({
	@NamedQuery(name = "Dependente.findAll", query = "SELECT d FROM Dependente d ORDER BY d.responsavel.nome")
})
public class Dependente extends BasicEntity<Long> implements Serializable{

	@Id
    @Column(name = "ID")
    private Long id;
	
	@ManyToOne
	@JoinColumn(name = "IDPESSOALRESP")
	private Pessoal responsavel;
	
	@ManyToOne
	@JoinColumn(name = "IDPESSOALDEP")
	private Pessoal dependente;
	
	@ManyToOne
	@JoinColumn(name = "IDTIPODEPENDENCIA")
	private TipoDependencia tipoDependencia;
	
	@ManyToOne
	@JoinColumn(name = "IDMOTIVOINICIO")
	private MotivoDependencia motivoInicio;
	
	@ManyToOne
	@JoinColumn(name = "IDMOTIVOFIM")
	private MotivoDependencia motivoFim;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATAINICIO")
	private Date dataInicio;
	
	@Temporal(TemporalType.DATE)
	@Column(name = "DATAFIM")
	private Date dataFim;
	
	@Column(name = "TIPODURACAO")
	private Long tipoDuracao;
	
	@Column(name = "DEPPREV")
	private boolean depPrev;
	
	@Column(name = "DEPIR")
	private boolean depIr;
	
	@Column(name = "DEPSF")
	private boolean depSf;
	
	@Column(name = "OBS")
	private String obs;
	
	@Column(name = "FLUNIVERSITARIO")
	private boolean flUniversitario;
	
	
	public Dependente() {
		
	}	
	

	@Override
	public Long getId() {return this.id;}

	@Override
	public void setId(Long id) {this.id = id;}

	public Pessoal getResponsavel() {return responsavel;}
	public void setResponsavel(Pessoal responsavel) {this.responsavel = responsavel;}

	public Pessoal getDependente() {return dependente;}
	public void setDependente(Pessoal dependente) {this.dependente = dependente;}
		
	public TipoDependencia getTipoDependencia() {return tipoDependencia;}
	public void setTipoDependencia(TipoDependencia tipoDependencia) {this.tipoDependencia = tipoDependencia;}

	public MotivoDependencia getMotivoInicio() {return motivoInicio;}
	public void setMotivoInicio(MotivoDependencia motivoInicio) {this.motivoInicio = motivoInicio;}

	public MotivoDependencia getMotivoFim() {return motivoFim;}
	public void setMotivoFim(MotivoDependencia motivoFim) {this.motivoFim = motivoFim;}

	public Date getDataInicio() {return dataInicio;}
	public void setDataInicio(Date dataInicio) {this.dataInicio = dataInicio;}

	public Date getDataFim() {return dataFim;}
	public void setDataFim(Date dataFim) {this.dataFim = dataFim;}

	public Long getTipoDuracao() {return tipoDuracao;}
	public void setTipoDuracao(Long tipoDuracao) {this.tipoDuracao = tipoDuracao;}

	public boolean isDepPrev() {return depPrev;}
	public void setDepPrev(boolean depPrev) {this.depPrev = depPrev;}

	public boolean isDepIr() {return depIr;}
	public void setDepIr(boolean depIr) {this.depIr = depIr;}

	public boolean isDepSf() {return depSf;}
	public void setDepSf(boolean depSf) {this.depSf = depSf;}

	public String getObs() {return obs;}
	public void setObs(String obs) {this.obs = obs;}

	public boolean isFlUniversitario() {return flUniversitario;}
	public void setFlUniversitario(boolean flUniversitario) {this.flUniversitario = flUniversitario;}	
	

}
