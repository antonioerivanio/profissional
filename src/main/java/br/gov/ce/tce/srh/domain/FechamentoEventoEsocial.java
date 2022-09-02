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


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_FECHAMENTO", schema=DatabaseMetadata.SCHEMA_SRH)
public class FechamentoEventoEsocial extends BasicEntity<Long> implements Serializable{
 
	@Id
	@Column(name = "ID")
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "IDFUNCIONAL")
	private Funcional funcional;
	
	@Column(name = "REFERENCIA")
	private String referencia;
	
	@Column(name = "TP_INSC")
	private Integer tipoInscricaoEmpregador;
	  
	@Column(name = "NR_INSC")
	private String numeroInscricaoEmpregador;
	  
	@Column(name = "EVT_REMUN")
    private Integer evtRemuneracao;  
    
	@Column(name = "EVT_COMPROD")
    private Integer evtComercializacaoProduto;

    @Column(name = "EVT_CONTRAAVNP")
    private Integer evtContratoAvulsoNaoPortuario;

    @Column(name = "EVT_INFOCOMPLPER")
    private Integer evtInfoComplementarPrevidenciária;
 
    @Column(name = "TRANS_DCTWEB")
    private Integer evtTransmissaoImediata;
    
    @Column(name = "NAO_VALID")
    private Integer evtNaoValidacao;

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

    public Integer getTipoInscricaoEmpregador() {
      return tipoInscricaoEmpregador;
    }

    public void setTipoInscricaoEmpregador(Integer tipoInscricaoEmpregador) {
      this.tipoInscricaoEmpregador = tipoInscricaoEmpregador;
    }

    public String getNumeroInscricaoEmpregador() {
      return numeroInscricaoEmpregador;
    }

    public void setNumeroInscricaoEmpregador(String numeroInscricaoEmpregador) {
      this.numeroInscricaoEmpregador = numeroInscricaoEmpregador;
    }

    public Integer getEvtRemuneracao() {
      return evtRemuneracao;
    }

    public void setEvtRemuneracao(Integer evtRemuneracao) {
      this.evtRemuneracao = evtRemuneracao;
    }

    public Integer getEvtComercializacaoProduto() {
      return evtComercializacaoProduto;
    }

    public void setEvtComercializacaoProduto(Integer evtComercializacaoProduto) {
      this.evtComercializacaoProduto = evtComercializacaoProduto;
    }

    public Integer getEvtContratoAvulsoNaoPortuario() {
      return evtContratoAvulsoNaoPortuario;
    }

    public void setEvtContratoAvulsoNaoPortuario(Integer evtContratoAvulsoNaoPortuario) {
      this.evtContratoAvulsoNaoPortuario = evtContratoAvulsoNaoPortuario;
    }

    public Integer getEvtInfoComplementarPrevidenciária() {
      return evtInfoComplementarPrevidenciária;
    }

    public void setEvtInfoComplementarPrevidenciária(Integer evtInfoComplementarPrevidenciária) {
      this.evtInfoComplementarPrevidenciária = evtInfoComplementarPrevidenciária;
    }

    public Integer getEvtTransmissaoImediata() {
      return evtTransmissaoImediata;
    }

    public void setEvtTransmissaoImediata(Integer evtTransmissaoImediata) {
      this.evtTransmissaoImediata = evtTransmissaoImediata;
    }

    public Integer getEvtNaoValidacao() {
      return evtNaoValidacao;
    }

    public void setEvtNaoValidacao(Integer evtNaoValidacao) {
      this.evtNaoValidacao = evtNaoValidacao;
    }
}		
	
