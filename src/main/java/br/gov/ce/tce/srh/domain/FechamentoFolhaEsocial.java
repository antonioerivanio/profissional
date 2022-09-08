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
import br.gov.ce.tce.srh.enums.DadoTCE;


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_FECHAMENTOFOLHA", schema=DatabaseMetadata.SCHEMA_SRH)
public class FechamentoFolhaEsocial extends BasicEntity<Long> implements Serializable{
 
	@Id
	@Column(name = "ID")
	private Long id;
	
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
    private Integer evtInfoComplementarPrevidenciaria;
 
    @Column(name = "TRANS_DCTWEB")
    private Integer evtTransmissaoImediata;
    
    @Column(name = "NAO_VALID")
    private Integer naoValidacao;
    
    @Column(name = "PER_APUR")
    private String peridoApuracao;
    
    @Transient
    private String anoReferencia;
    @Transient
    private String mesReferencia;
    
    
    
    public FechamentoFolhaEsocial() {
      super();
    }

    public FechamentoFolhaEsocial(Integer tipoInscricaoEmpregador, String numeroInscricaoEmpregador, Integer evtRemuneracao,
                              Integer evtComercializacaoProduto, Integer evtContratoAvulsoNaoPortuario, Integer evtInfoComplementarPrevidenciaria, Integer evtTransmissaoImediata, Integer naoValidacao, String peridoApuracao) {
      super();
      this.tipoInscricaoEmpregador = tipoInscricaoEmpregador;
      this.numeroInscricaoEmpregador = numeroInscricaoEmpregador;
      this.evtRemuneracao = evtRemuneracao;
      this.evtComercializacaoProduto = evtComercializacaoProduto;
      this.evtContratoAvulsoNaoPortuario = evtContratoAvulsoNaoPortuario;
      this.evtInfoComplementarPrevidenciaria = evtInfoComplementarPrevidenciaria;
      this.evtTransmissaoImediata = evtTransmissaoImediata;
      this.naoValidacao = naoValidacao;      
      this.peridoApuracao = peridoApuracao;
    }

    public Long getId() {
      return id;
    }

    public void setId(Long id) {
      this.id = id;
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
      /*
       * if(numeroInscricaoEmpregador == null) { numeroInscricaoEmpregador = DadoTCE.NR_INSC; }
       */
      
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

    public Integer getEvtInfoComplementarPrevidenciaria() {
      return evtInfoComplementarPrevidenciaria;
    }

    public void setEvtInfoComplementarPrevidenciaria(Integer evtInfoComplementarPrevidenciaria) {
      this.evtInfoComplementarPrevidenciaria = evtInfoComplementarPrevidenciaria;
    }

    public Integer getEvtTransmissaoImediata() {
      return evtTransmissaoImediata;
    }

    public void setEvtTransmissaoImediata(Integer evtTransmissaoImediata) {
      this.evtTransmissaoImediata = evtTransmissaoImediata;
    }

    public Integer getNaoValidacao() {
      return naoValidacao;
    }

    public void setNaoValidacao(Integer naoValidacao) {
      this.naoValidacao = naoValidacao;
    }

    public String getPeridoApuracao() {
      return peridoApuracao;
    }

    public void setPeridoApuracao(String peridoApuracao) {
      this.peridoApuracao = peridoApuracao;
    }

    public String getAnoReferencia() {
      return anoReferencia;
    }

    public void setAnoReferencia(String anoReferencia) {
      this.anoReferencia = anoReferencia;
    }

    public String getMesReferencia() {
      return mesReferencia;
    }

    public void setMesReferencia(String mesReferencia) {
      this.mesReferencia = mesReferencia;
    }
    
    
}
	
