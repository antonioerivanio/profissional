package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import br.gov.ce.tce.srh.sca.domain.Usuario;

/***
 * Classe que amazena os dados das solicitações dependentes do Beneficiario do TCE-CE para
 * ressarcimento dos valores de gastos particulares com planos de saúde, odontologicos, etc..
 * 
 * @author erivanio.cruz
 * @since 03/06/2022
 */

@Entity
@Table(name = "FP_AUXILIOSAUDEREQDEP", schema = DatabaseMetadata.SCHEMA_SRH)
@SequenceGenerator(name = "SEQ_FP_AUXILIOSAUDEREQDEP", sequenceName = "SEQ_FP_AUXILIOSAUDEREQDEP", allocationSize = 1)
public class AuxilioSaudeRequisicaoDependente extends BasicEntity<Long>  implements Serializable{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FP_AUXILIOSAUDEREQDEP")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "IDAUXILIOSAUDEREQ")
  private AuxilioSaudeRequisicao auxilioSaudeRequisicao;
  
  @ManyToOne
  @JoinColumn(name = "IDDEPENDENTE")
  private Dependente dependente;

  @ManyToOne
  @JoinColumn(name = "IDPESSOAJURIDICA")  
  private PessoaJuridica pessoaJuridica;

  @Column(name = "VALOR_PLANOSAUDE")
  private Double valorGastoPlanoSaude;
  
  
  @Column(name = "FLG_DELETADO")
  private boolean flgDeletado;
  
  
  public AuxilioSaudeRequisicaoDependente() {

  }  
  
  
  public AuxilioSaudeRequisicaoDependente(AuxilioSaudeRequisicao auxilioSaudeRequisicao,
      Dependente dependente, PessoaJuridica pessoaJuridica, Double valorGastoPlanoSaude) {
    super();
    this.auxilioSaudeRequisicao = auxilioSaudeRequisicao;
    this.dependente = dependente;
    this.pessoaJuridica = pessoaJuridica;
    this.valorGastoPlanoSaude = valorGastoPlanoSaude;
  }


  public AuxilioSaudeRequisicao getAuxilioSaudeRequisicao() {
    return auxilioSaudeRequisicao;
  }


  public void setAuxilioSaudeRequisicao(AuxilioSaudeRequisicao auxilioSaudeRequisicao) {
    this.auxilioSaudeRequisicao = auxilioSaudeRequisicao;
  }


  public Dependente getDependente() {
    return dependente;
  }

  public void setDependente(Dependente dependente) {
    this.dependente = dependente;
  }

  // getters e setters

  @Override
  public Long getId() {
    // TODO Auto-generated method stub
    return id;
  }


  @Override
  public void setId(Long id) {
    this.id=id;
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


  public boolean getFlgDeletado() {
    return flgDeletado;
  }

  public void setFlgDeletado(boolean flgDeletado) {
    this.flgDeletado = flgDeletado;
  }  
  
}
