package br.gov.ce.tce.srh.domain;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import br.gov.ce.tce.srh.sca.domain.Usuario;

/***
 * Classe que amazena os dados das auxilio saude base
 * 
 * @author erivanio.cruz
 * @since 03/06/2022
 */
@Entity
@Table(name = "FP_AUXILIOSAUDEBASE", schema = DatabaseMetadata.SCHEMA_SRH)
@SequenceGenerator(name = "SEQ_AUXILIOSAUDEREQBASE", sequenceName = "SEQ_AUXILIOSAUDEREQBASE", allocationSize = 1)
public class AuxilioSaudeRequisicaoBase extends BasicEntity<Long> implements BeanEntidade {

  private static final long serialVersionUID = 1L;


  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AUXILIOSAUDEREQBASE")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "IDPESSOAL")
  private Pessoal pessoal;

  @ManyToOne
  @JoinColumn(name = "IDUSUARIO")
  private Usuario usuario;

  @Column(name = "IDBENEFICIARIO")
  private Long idBeneficiario;

  @Column(name = "CUSTOPLANOBASE")
  private Double custoPlanoBase;

  @Column(name = "CUSTOADICIONAL")
  private Double custoAdicional;

  @Column(name = "OBS")
  private String observacao;

  @Temporal(TemporalType.DATE)
  @Column(name = "DATAATUALIZACAO")
  private Date dataAtualizacao;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "FLATIVO")
  private FlagAtivo flgAtivo;

  @Temporal(TemporalType.DATE)
  @Column(name = "CREATED_AT")
  private Date dataCriacao;


  public enum FlagAtivo {
    NAO, SIM
  }


  public AuxilioSaudeRequisicaoBase() {
    super();
  }


  public AuxilioSaudeRequisicaoBase(Pessoal pessoal, Usuario usuario, Double custoPlanoBase, String observacao,
                            Date dataAtualizacao, FlagAtivo flgAtivo, Date dataCriacao) {
    super();
    this.pessoal = pessoal;
    this.usuario = usuario;
    this.custoPlanoBase = custoPlanoBase;    
    this.observacao = observacao;
    this.dataAtualizacao = dataAtualizacao;
    this.flgAtivo = flgAtivo;
    this.dataCriacao = dataCriacao;
  }


  @Override
  public Long getId() {
    return this.id;
  }


  @Override
  public void setId(Long id) {
    this.id = id;
  }


  public Pessoal getPessoal() {
    return pessoal;
  }


  public void setPessoal(Pessoal pessoal) {
    this.pessoal = pessoal;
  }


  public Usuario getUsuario() {
    return usuario;
  }


  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }




  public Long getIdBeneficiario() {
    return idBeneficiario;
  }


  public void setIdBeneficiario(Long idBeneficiario) {
    this.idBeneficiario = idBeneficiario;
  }


  public Double getCustoPlanoBase() {
    return custoPlanoBase;
  }


  public void setCustoPlanoBase(Double custoPlanoBase) {
    this.custoPlanoBase = custoPlanoBase;
  }


  public Double getCustoAdicional() {
    return custoAdicional;
  }


  public void setCustoAdicional(Double custoAdicional) {
    this.custoAdicional = custoAdicional;
  }


  public String getObservacao() {
    return observacao;
  }


  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }


  public Date getDataAtualizacao() {
    return dataAtualizacao;
  }


  public void setDataAtualizacao(Date dataAtualizacao) {
    this.dataAtualizacao = dataAtualizacao;
  }


  public FlagAtivo getFlgAtivo() {
    return flgAtivo;
  }


  public void setFlgAtivo(FlagAtivo flgAtivo) {
    this.flgAtivo = flgAtivo;
  }


  public Date getDataCriacao() {
    return dataCriacao;
  }


  public void setDataCriacao(Date dataCriacao) {
    this.dataCriacao = dataCriacao;
  }


}
