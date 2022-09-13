package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_FECHAMENTOFOLHA", schema = DatabaseMetadata.SCHEMA_SRH)
public class FechamentoFolhaEsocial extends BasicEntity<Long> implements Serializable {

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
  private char evtRemuneracao;

  @Column(name = "EVT_COMPROD")
  private char evtComercializacaoProduto;

  @Column(name = "EVT_CONTRAAVNP")
  private char evtContratoAvulsoNaoPortuario;

  @Column(name = "EVT_INFOCOMPLPER")
  private char evtInfoComplementarPrevidenciaria;

  @Column(name = "TRANS_DCTWEB")
  private String evtTransmissaoImediata;

  @Column(name = "NAO_VALID")
  private String naoValidacao;

  @Column(name = "PER_APUR")
  private String periodoApuracao;

  @Column(name = "IND_APURACAO")
  private Integer indicativoApuracao;

  @Transient
  private String anoReferencia;
  @Transient
  private String mesReferencia;
  @Transient
  private String referenciaMesAnoTransient;


  public FechamentoFolhaEsocial() {
    super();
  }

  public FechamentoFolhaEsocial(Integer tipoInscricaoEmpregador, String numeroInscricaoEmpregador, char evtRemuneracao, char evtComercializacaoProduto, char evtContratoAvulsoNaoPortuario,
                            char evtInfoComplementarPrevidenciaria, String evtTransmissaoImediata, String naoValidacao, String peridoApuracao, Integer indicativoApuracao) {
    super();
    this.tipoInscricaoEmpregador = tipoInscricaoEmpregador;
    this.numeroInscricaoEmpregador = numeroInscricaoEmpregador;
    this.evtRemuneracao = evtRemuneracao;
    this.evtComercializacaoProduto = evtComercializacaoProduto;
    this.evtContratoAvulsoNaoPortuario = evtContratoAvulsoNaoPortuario;
    this.evtInfoComplementarPrevidenciaria = evtInfoComplementarPrevidenciaria;
    this.evtTransmissaoImediata = evtTransmissaoImediata;
    this.naoValidacao = naoValidacao;
    this.periodoApuracao = peridoApuracao;
    this.indicativoApuracao = indicativoApuracao;
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
    return numeroInscricaoEmpregador;
  }

  public void setNumeroInscricaoEmpregador(String numeroInscricaoEmpregador) {
    this.numeroInscricaoEmpregador = numeroInscricaoEmpregador;
  }

  public char getEvtRemuneracao() {
    return evtRemuneracao;
  }

  public void setEvtRemuneracao(char evtRemuneracao) {
    this.evtRemuneracao = evtRemuneracao;
  }

  public char getEvtComercializacaoProduto() {
    return evtComercializacaoProduto;
  }

  public void setEvtComercializacaoProduto(char evtComercializacaoProduto) {
    this.evtComercializacaoProduto = evtComercializacaoProduto;
  }

  public char getEvtContratoAvulsoNaoPortuario() {
    return evtContratoAvulsoNaoPortuario;
  }

  public void setEvtContratoAvulsoNaoPortuario(char evtContratoAvulsoNaoPortuario) {
    this.evtContratoAvulsoNaoPortuario = evtContratoAvulsoNaoPortuario;
  }

  public char getEvtInfoComplementarPrevidenciaria() {
    return evtInfoComplementarPrevidenciaria;
  }

  public void setEvtInfoComplementarPrevidenciaria(char evtInfoComplementarPrevidenciaria) {
    this.evtInfoComplementarPrevidenciaria = evtInfoComplementarPrevidenciaria;
  }

  public String getEvtTransmissaoImediata() {
    if (evtTransmissaoImediata != null && evtTransmissaoImediata.equals("null")) {
      evtTransmissaoImediata = null;
    }

    return evtTransmissaoImediata;
  }

  public void setEvtTransmissaoImediata(String evtTransmissaoImediata) {
    this.evtTransmissaoImediata = evtTransmissaoImediata;
  }

  public String getNaoValidacao() {
    if (naoValidacao != null && naoValidacao.equals("null")) {
      naoValidacao = null;
    }
    return naoValidacao;
  }

  public void setNaoValidacao(String naoValidacao) {
    this.naoValidacao = naoValidacao;
  }

  public String getPeriodoApuracao() {
    return periodoApuracao;
  }

  public void setPeriodoApuracao(String peridoApuracao) {
    this.periodoApuracao = peridoApuracao;
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

  public Integer getIndicativoApuracao() {
    return indicativoApuracao;
  }

  public void setIndicativoApuracao(Integer indicativoApuracao) {
    this.indicativoApuracao = indicativoApuracao;
  }

  public String getReferenciaMesAnoTransient() {
    return referenciaMesAnoTransient;
  }

  public void setReferenciaMesAnoTransient(String referenciaMesAno) {
    this.referenciaMesAnoTransient = referenciaMesAno;
  }


}

