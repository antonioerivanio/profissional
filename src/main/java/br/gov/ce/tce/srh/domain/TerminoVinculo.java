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
import br.gov.ce.tce.srh.enums.DadoTCE;


@Entity
@SuppressWarnings("serial")
@Table(name = "ESOCIAL_TERMINOVINCULO", schema = DatabaseMetadata.SCHEMA_SRH)
public class TerminoVinculo extends BasicEntity<Long> implements Serializable {

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
  
  @Column(name = "COD_CATEGORIA")
  private Integer codCategoria;
  
  @Temporal(TemporalType.DATE)
  @Column(name = "DT_TERM")
  private Date dtDesligamento;
  
  @Column(name = "NR_PROCTRABALHO")
  private String numeroProcessoTrabalhista;
  
  @Column(name = "TP_INSC_EMPREGADOR")
  private Integer tipoInscricaoEmpregador;
  
  @Column(name = "NR_INSC_EMPREGADOR")
  private String numeroInscricaoEmpregador;

  @Column(name = "TP_INSC_LOTACAO")
  private Integer tipoInscricaoLotacao;
  
  @Column(name = "NR_INSC_LOTACAO")
  private String numeroInscricaoLotacao;
  
  @Column(name = "COD_LOTACAO")
  private Integer codLotacao;
  
  @Temporal(TemporalType.DATE)
  @Column(name = "DT_FIMQUAR")
  private Date dtFimQuarentena;

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

  public Integer getTipoInscricaoEmpregador() {
    return tipoInscricaoEmpregador;
  }

  public void setTipoInscricaoEmpregador(Integer tipoInscricaoEmpregador) {
    this.tipoInscricaoEmpregador = tipoInscricaoEmpregador;
  }

  public String getNumeroInscricaoEmpregador() {
    if(numeroInscricaoEmpregador == null) {
      numeroInscricaoEmpregador = DadoTCE.NR_INSC;
    }
    return numeroInscricaoEmpregador;
  }

  public void setNumeroInscricaoEmpregador(String numeroInscricaoEmpregador) {
    this.numeroInscricaoEmpregador = numeroInscricaoEmpregador;
  }

  public Integer getCodLotacao() {
    return codLotacao;
  }

  public void setCodLotacao(Integer codLotacao) {
    this.codLotacao = codLotacao;
  }

  public Integer getTipoInscricaoLotacao() {
    return tipoInscricaoLotacao;
  }

  public void setTipoInscricaoLotacao(Integer tipoInscricaoLotacao) {
    this.tipoInscricaoLotacao = tipoInscricaoLotacao;
  }

  public String getNumeroInscricaoLotacao() {
    if(numeroInscricaoLotacao ==  null) {
      numeroInscricaoLotacao = DadoTCE.CNPJ;//cnpj tce
    }
    return numeroInscricaoLotacao;
  }

  public void setNumeroInscricaoLotacao(String numeroInscricaoLotacao) {
    this.numeroInscricaoLotacao = numeroInscricaoLotacao;
  }  

  public Funcional getFuncional() {
    return funcional;
  }

  public void setFuncional(Funcional funcional) {
    this.funcional = funcional;
  }

  public Integer getCodCategoria() {
    return codCategoria;
  }

  public void setCodCategoria(Integer codCategoria) {
    this.codCategoria = codCategoria;
  }

  public Date getDtFimQuarentena() {
    return dtFimQuarentena;
  }

  public void setDtFimQuarentena(Date dtFimQuarentena) {
    this.dtFimQuarentena = dtFimQuarentena;
  }
  
}
