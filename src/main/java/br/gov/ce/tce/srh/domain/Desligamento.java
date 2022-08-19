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
@Table(name = "ESOCIAL_DESLIGAMENTO", schema = DatabaseMetadata.SCHEMA_SRH)
public class Desligamento extends BasicEntity<Long> implements Serializable {

  @Id
  @Column(name = "ID")
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "IDFUNCIONAL")
  private Funcional funcional;
  
  @Column(name = "REFERENCIA")
  private String referencia;

  @Column(name = "CPF_TRAB")
  private String cpfTrab;

  @Column(name = "MATRICULA")
  private String matricula;
  
  @Column(name = "MTV_DESLIG")
  private String mtvDesligamento;
  
  @Temporal(TemporalType.DATE)
  @Column(name = "DT_DESLIG")
  private Date dtDesligamento;
  
  @Column(name = "NR_PROCTRABALHO")
  private String numeroProcessoTrabalhista;

  @Column(name = "OBSERVACAO")
  private String observacao;
  
  @Column(name = "TP_INSC_EMPREGADOR")
  private String tipoInscricaoEmpregador;
  
  @Column(name = "NR_INSC_EMPREGADOR")
  private String numeroInscricaoEmpregador;  
 
  @Column(name = "MUDANCA_CPF")
  private String mudancaCpf;  
    
  @Column(name = "NOVO_CPF")
  private String novoCpf;
  
  
  @Column(name = "COD_LOTACAO")
  private Integer codLotacao;

  @Column(name = "TP_INSC_LOCATACAO")
  private String tipoInscricaoLotacao;
  
  @Column(name = "NR_INSC_LOCATACAO")
  private String numeroInscricaoLotacao;
  
  @Column(name = "GRAU_EXP")
  private String grauExposicao;

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

  public String getCpfTrab() {
    return cpfTrab;
  }

  public void setCpfTrab(String cpfTrab) {
    this.cpfTrab = cpfTrab;
  }

  public String getMatricula() {
    return matricula;
  }

  public void setMatricula(String matricula) {
    this.matricula = matricula;
  }

  public String getMtvDesligamento() {
    return mtvDesligamento;
  }

  public void setMtvDesligamento(String mtvDesligamento) {
    this.mtvDesligamento = mtvDesligamento;
  }

  public Date getDtDesligamento() {
    return dtDesligamento;
  }

  public void setDtDesligamento(Date dtDesligamento) {
    this.dtDesligamento = dtDesligamento;
  }

  public String getNumeroProcessoTrabalhista() {
    return numeroProcessoTrabalhista;
  }

  public void setNumeroProcessoTrabalhista(String numeroProcessoTrabalhista) {
    this.numeroProcessoTrabalhista = numeroProcessoTrabalhista;
  }

  public String getObservacao() {
    return observacao;
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }

  public String getTipoInscricaoEmpregador() {
    return tipoInscricaoEmpregador;
  }

  public void setTipoInscricaoEmpregador(String tipoInscricaoEmpregador) {
    this.tipoInscricaoEmpregador = tipoInscricaoEmpregador;
  }

  public String getNumeroInscricaoEmpregador() {
    return numeroInscricaoEmpregador;
  }

  public void setNumeroInscricaoEmpregador(String numeroInscricaoEmpregador) {
    this.numeroInscricaoEmpregador = numeroInscricaoEmpregador;
  }

  public String getMudancaCpf() {
    return mudancaCpf;
  }

  public void setMudancaCpf(String mudancaCpf) {
    this.mudancaCpf = mudancaCpf;
  }

  public String getNovoCpf() {
    return novoCpf;
  }

  public void setNovoCpf(String novoCpf) {
    this.novoCpf = novoCpf;
  }

  public Integer getCodLotacao() {
    return codLotacao;
  }

  public void setCodLotacao(Integer codLotacao) {
    this.codLotacao = codLotacao;
  }

  public String getTipoInscricaoLotacao() {
    return tipoInscricaoLotacao;
  }

  public void setTipoInscricaoLotacao(String tipoInscricaoLotacao) {
    this.tipoInscricaoLotacao = tipoInscricaoLotacao;
  }

  public String getNumeroInscricaoLotacao() {
    return numeroInscricaoLotacao;
  }

  public void setNumeroInscricaoLotacao(String numeroInscricaoLotacao) {
    this.numeroInscricaoLotacao = numeroInscricaoLotacao;
  }

  public String getGrauExposicao() {
    return grauExposicao;
  }

  public void setGrauExposicao(String grauExposicao) {
    this.grauExposicao = grauExposicao;
  }

  public Funcional getFuncional() {
    return funcional;
  }

  public void setFuncional(Funcional funcional) {
    this.funcional = funcional;
  }
  
  
}
