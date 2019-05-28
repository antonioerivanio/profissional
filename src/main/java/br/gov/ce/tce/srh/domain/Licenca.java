package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * Referente a tabela: TB_LICENCA
 * 
 * @since   : Nov 20, 2011, 11:44:45 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@SuppressWarnings("serial")
@Table(name="TB_LICENCA", schema="SRH")
public class Licenca extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDPESSOAL")
    private Pessoal pessoal;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDLICENCAESP")
    private LicencaEspecial licencaEspecial;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDTIPOLICENCA")
    private TipoLicenca tipoLicenca;

    @Column(name = "INICIO", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date inicio;
 
    @Column(name = "FIM", nullable=false)
    @Temporal(TemporalType.DATE)
    private Date fim;

    @Column(name = "EXCLUITEMPOSERV", nullable=false)
    private boolean excluitemposerv;

    @Column(name = "EXCLUIFINANCEIRO", nullable=false)
    private boolean excluifinanceiro;

    @Column(name = "NRPROCESSO", length=10)
    private String nrprocesso;

    @Column(name = "OBS", length=2000)
    private String obs;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "IDTIPOPUBLICACAO")
    private TipoPublicacao tipoPublicacao;

    @Column(name = "DOE")
    @Temporal(TemporalType.DATE)
    private Date doe;
    
    @Column(name = "CONTARDIASEMDOBRO")
    private Long contarDiasEmDobro;

	@Transient
	private Integer dias;

	public Pessoal getPessoal() {return pessoal;}
	public void setPessoal(Pessoal pessoal) {this.pessoal = pessoal;}

	public LicencaEspecial getLicencaEspecial() {return licencaEspecial;}
	public void setLicencaEspecial(LicencaEspecial licencaEspecial) {this.licencaEspecial = licencaEspecial;}

	public TipoLicenca getTipoLicenca() {return tipoLicenca;}
	public void setTipoLicenca(TipoLicenca tipoLicenca) {this.tipoLicenca = tipoLicenca;}

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	public boolean isExcluitemposerv() {return excluitemposerv;}
	public void setExcluitemposerv(boolean excluitemposerv) {this.excluitemposerv = excluitemposerv;}

	public boolean isExcluifinanceiro() {return excluifinanceiro;}
	public void setExcluifinanceiro(boolean excluifinanceiro) {this.excluifinanceiro = excluifinanceiro;}

	public String getNrprocesso() {
		if(nrprocesso != null && !nrprocesso.trim().isEmpty()){
			return nrprocesso.substring(4,9) + nrprocesso.substring(0,4) + nrprocesso.substring(9,10);
		}
		else{
			return null;
		}
	}
	public void setNrprocesso(String nrprocesso) {
		this.nrprocesso = nrprocesso;
	}
	
	public String getNrprocessoPuro(){
		return nrprocesso;
	}

	public String getObs() {return obs;}
	public void setObs(String obs) {this.obs = obs;}

	public TipoPublicacao getTipoPublicacao() {return tipoPublicacao;}
	public void setTipoPublicacao(TipoPublicacao tipoPublicacao) {this.tipoPublicacao = tipoPublicacao;}

	public Date getDoe() {return doe;}
	public void setDoe(Date doe) {this.doe = doe;}

	public Long getContarDiasEmDobro() {return contarDiasEmDobro;}
	public void setContarDiasEmDobro(Long contarDiasEmDobro) {this.contarDiasEmDobro = contarDiasEmDobro;}
	
	public Integer getDias() {
		Date dfim = (getFim()==null?new Date():getFim());		
		Date dini = getInicio();
		dias = SRHUtils.dataDiff(dini, dfim);
		return dias;
	}

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}