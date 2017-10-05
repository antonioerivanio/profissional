package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.gov.ce.tce.srh.enums.EnumTipoBeneficio;

@Entity
@SuppressWarnings("serial")
@Table(name="TB_APOSENTADORIA", schema="SRH")
public class Aposentadoria extends BasicEntity<Long> implements Serializable {

    @Id
    @Column(name = "ID")
    private Long id;

    @OneToOne
    @JoinColumn(name = "IDFUNCIONAL")
    private Funcional funcional;

    @Column(name = "IDTIPOBENEFICIO")
    @Enumerated(EnumType.ORDINAL)
    private EnumTipoBeneficio tipoBeneficio;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DATAATO")
    private Date dataAto;
    
    @Column(name = "NUMERORESOLUCAO")
    private Long numeroResolucao;
    
    @Column(name = "ANORESOLUCAO")
    private Long anoResolucao;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DATAULTIMACONTAGEM")
    private Date dataUltimaContagem;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "DATAINICIOBENEFICIO")
    private Date dataInicioBeneficio;
        
    @ManyToOne
    @JoinColumn(name="IDTIPOPUBLICACAO")
    private TipoPublicacao tipoPublicacao;

    @Temporal(TemporalType.DATE)
    @Column(name = "DATAPUBLICACAOATO")
    private Date dataPublicacao;

    @Column(name = "OBS", length=2000)
    private String observacao;
    		

	@Override
	public Long getId() {return this.id;}
	
	@Override
	public void setId(Long id) {this.id = id;}

	public Funcional getFuncional() {
		return funcional;
	}

	public void setFuncional(Funcional funcional) {
		this.funcional = funcional;
	}

	public EnumTipoBeneficio getTipoBeneficio() {
		return tipoBeneficio;
	}

	public void setTipoBeneficio(EnumTipoBeneficio tipoBeneficio) {
		this.tipoBeneficio = tipoBeneficio;
	}

	public Date getDataAto() {
		return dataAto;
	}

	public void setDataAto(Date dataAto) {
		this.dataAto = dataAto;
	}

	public Long getNumeroResolucao() {
		return numeroResolucao;
	}

	public void setNumeroResolucao(Long numeroResolucao) {
		this.numeroResolucao = numeroResolucao;
	}

	public Long getAnoResolucao() {
		return anoResolucao;
	}

	public void setAnoResolucao(Long anoResolucao) {
		this.anoResolucao = anoResolucao;
	}

	public Date getDataUltimaContagem() {
		return dataUltimaContagem;
	}

	public void setDataUltimaContagem(Date dataUltimaContagem) {
		this.dataUltimaContagem = dataUltimaContagem;
	}

	public Date getDataInicioBeneficio() {
		return dataInicioBeneficio;
	}

	public void setDataInicioBeneficio(Date dataInicioBeneficio) {
		this.dataInicioBeneficio = dataInicioBeneficio;
	}

	public TipoPublicacao getTipoPublicacao() {
		return tipoPublicacao;
	}

	public void setTipoPublicacao(TipoPublicacao tipoPublicacao) {
		this.tipoPublicacao = tipoPublicacao;
	}

	public Date getDataPublicacao() {
		return dataPublicacao;
	}

	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}
		
	
}