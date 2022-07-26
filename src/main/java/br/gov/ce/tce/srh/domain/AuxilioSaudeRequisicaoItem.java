package br.gov.ce.tce.srh.domain;

import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/***
 * Classe que amazena os dados das solicitações dos colaboradores e seus dependentes para
 * ressarcimento dos valores de gastos particulares com planos de saúde, odontologicos, etc..
 * 
 * @author erivanio.cruz
 * @since 03/06/2022
 */

@Entity
@Table(name = "FP_AUXILIOSAUDEREQITEM", schema = DatabaseMetadata.SCHEMA_SRH)
@SequenceGenerator(name = "SEQ_FP_AUXILIOSAUDEREQITEM", sequenceName = "SEQ_FP_AUXILIOSAUDEREQITEM", allocationSize = 1)
public class AuxilioSaudeRequisicaoItem extends BasicEntity<Long> implements BeanEntidade {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FP_AUXILIOSAUDEREQITEM")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "IDPESSOAJURIDICA")
  private PessoaJuridica pessoaJuridica;
  
  @ManyToOne
  @JoinColumn(name = "IDAUXILIOSAUDEREQ")
  private AuxilioSaudeRequisicao auxilioSaudeRequisicao;
  

  @Column(name = "VALOR_PLANOSAUDE")
  private Double valorGastoPlanoSaude;
  
  @Temporal(TemporalType.DATE)
  @Column(name = "DT_INCLUSAO")
  private Date dataInclusao;
  
  @Column(name = "FLG_DELETADO")
  private boolean flgDeletado;

  @Temporal(TemporalType.DATE)
  @Column(name = "DT_DELETE")
  private Date dataDelete;

  @Transient
  private List<AuxilioSaudeRequisicaoDocumento> auxilioSaudeRequisicaoDocumentoBeneficiarioList;

  public AuxilioSaudeRequisicaoItem() {

  }
  

  public AuxilioSaudeRequisicaoItem(AuxilioSaudeRequisicao auxilioSaudeRequisicao, PessoaJuridica pessoaJuridica, Double valorGastoPlanoSaude, Date dataInclusao) {
    super();
    this.auxilioSaudeRequisicao = auxilioSaudeRequisicao;
    this.pessoaJuridica = pessoaJuridica;
    this.valorGastoPlanoSaude = valorGastoPlanoSaude;
    this.dataInclusao = dataInclusao;    
  }


  public Long getId() {
    return id;
  }


  public void setId(Long id) {
    this.id = id;
  }


  public PessoaJuridica getPessoaJuridica() {
    return pessoaJuridica;
  }

  public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
    this.pessoaJuridica = pessoaJuridica;
  }

  public Double getValorGastoPlanoSaude() {
    return valorGastoPlanoSaude;
  }

  public void setValorGastoPlanoSaude(Double valorGastoPlanoSaude) {
    this.valorGastoPlanoSaude = valorGastoPlanoSaude;
  }

  public Date getDataInclusao() {
    return dataInclusao;
  }

  public void setDataInclusao(Date dataInclusao) {
    this.dataInclusao = dataInclusao;
  }

  public boolean isFlgDeletado() {
    return flgDeletado;
  }


  public void setFlgDeletado(boolean flgDeletado) {
    this.flgDeletado = flgDeletado;
  }


  public Date getDataDelete() {
    return dataDelete;
  }

  public void setDataDelete(Date dataDelete) {
    this.dataDelete = dataDelete;
  }


  public AuxilioSaudeRequisicao getAuxilioSaudeRequisicao() {
    return auxilioSaudeRequisicao;
  }


  public void setAuxilioSaudeRequisicao(AuxilioSaudeRequisicao auxilioSaudeRequisicao) {
    this.auxilioSaudeRequisicao = auxilioSaudeRequisicao;
  }


  public List<AuxilioSaudeRequisicaoDocumento> getAuxilioSaudeRequisicaoDocumentoBeneficiarioList() {
    return auxilioSaudeRequisicaoDocumentoBeneficiarioList;
  }

  public void setAuxilioSaudeRequisicaoDocumentoBeneficiarioList(List<AuxilioSaudeRequisicaoDocumento> auxilioSaudeRequisicaoDocumentoBeneficiarioList) {
    this.auxilioSaudeRequisicaoDocumentoBeneficiarioList = auxilioSaudeRequisicaoDocumentoBeneficiarioList;
  }
  
}
