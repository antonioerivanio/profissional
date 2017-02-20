package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
 * Referente a tabela: TB_DIREITOLICENCAESP
 * 
 * @since   : Nov 20, 2011, 19:06:01 AM
 * @author  : robstownholanda@ivia.com.br
 * 
 */
@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@Entity
@SuppressWarnings("serial")
@Table(name="TB_FERIAS", schema="SRH")
public class Ferias extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "IDFUNCIONAL")
    private Funcional funcional;

    @ManyToOne
    @JoinColumn(name = "TIPOFERIAS")
    private TipoFerias tipoFerias;
    
    @ManyToOne
    @JoinColumn(name="IDTIPOPUBLICACAO")
    private TipoPublicacao tipoPublicacao;

    @Column(name = "ANOREFERENCIA")
    private Long anoReferencia;

    @Temporal(TemporalType.DATE)
    @Column(name = "INICIO")
    private Date inicio;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATADOATO")
    private Date dataDoAto;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATAPUBLICACAO")
    private Date dataPublicacao;

    @Temporal(TemporalType.DATE)
    @Column(name = "FIM")
    private Date fim;

    @Column(name = "OBS", length=2000)
    private String observacao;

    @Column(name = "QTDEDIAS", nullable=false)
    private Long qtdeDias;

    @Column(name = "PERIODO", nullable=false)
    private Long periodo;

	@Transient
	private Integer dias;

	public Funcional getFuncional() {return funcional;}
	public void setFuncional(Funcional funcional) {this.funcional = funcional;}

	public TipoFerias getTipoFerias() {return tipoFerias;}
	public void setTipoFerias(TipoFerias tipoFerias) {this.tipoFerias = tipoFerias;}

	public Long getAnoReferencia() {return anoReferencia;}
	public void setAnoReferencia(Long anoReferencia) {this.anoReferencia = anoReferencia;}

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {this.inicio = inicio;}

	public Date getDataDoAto() {return dataDoAto;}
	public void setDataDoAto(Date dataDoAto) {this.dataDoAto = dataDoAto;}

	public Date getDataPublicacao() {return dataPublicacao;}
	public void setDataPublicacao(Date dataPublicacao) {this.dataPublicacao = dataPublicacao;}

	public Date getFim() {return fim;}
	public void setFim(Date fim) {this.fim = fim;}

	public String getObservacao() {return observacao;}
	public void setObservacao(String observacao) {this.observacao = observacao;}

	public Long getQtdeDias() {return qtdeDias;}
	public void setQtdeDias(Long qtdeDias) {this.qtdeDias = qtdeDias;}

	public Long getPeriodo() {return periodo;}
	public void setPeriodo(Long periodo) {this.periodo = periodo;}
	
	public TipoPublicacao getTipoPublicacao() { return tipoPublicacao;	}
	public void setTipoPublicacao(TipoPublicacao tipoPublicacao) {this.tipoPublicacao = tipoPublicacao;}

	public Integer getDias() {
		Date dfim = (getFim()==null?new Date():getFim());		
		Date dini = (getInicio()==null?new Date():getInicio());
		dias = SRHUtils.dataDiff(dini, dfim);
		return dias;
	}	

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}
	
}