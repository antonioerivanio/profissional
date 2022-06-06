package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import br.gov.ce.tce.srh.sca.domain.Usuario;

/***
 * Classe que amazena os arquivos que compravam os gastos 
 * com saude dos colaboradores e seus dependentes.
 * 
 * @author erivanio.cruz
 * @since 03/06/2022
 */
@Entity
@Table(name = "FP_AUXILIOSAUDEDOC", schema=DatabaseMetadata.SCHEMA_SRH)
@SequenceGenerator(name = "SEQ_FP_AUXILIOSAUDEDOC", sequenceName = "SEQ_FP_AUXILIOSAUDEDOC",
schema=DatabaseMetadata.SCHEMA_SRH, allocationSize = 1, initialValue = 1)
public class AuxilioSaudeRequisicaoDocumento extends BasicEntity<Long> implements Serializable {

  private static final long serialVersionUID = 481877607288972254L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FP_AUXILIOSAUDEDOC")
  private Long id;

  
  @ManyToOne
  @JoinColumn(name = "ID_AUXILIOSAUDEREQ")
  private AuxilioSaudeRequisicaoDocumento auxilioSaudeRequisicao;
  
  @Column(name = "NOME_ARQ")
  private String nomeArquivo;
  
  @Column(name = "CAMINHO_ARQ")
  private String caminhoArquivo;
  
  @Temporal(TemporalType.DATE)
  @Column(name = "DT_INCLUSAO")
  private Date dataInclusao;

  @Column(name = "OBSERVACAO")
  private String observacao;
  
  

  public AuxilioSaudeRequisicaoDocumento() {

  }


  @Override
  public Long getId() {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public void setId(Long id) {
    // TODO Auto-generated method stub

  }


  public AuxilioSaudeRequisicaoDocumento getAuxilioSaudeRequisicao() {
    return auxilioSaudeRequisicao;
  }


  public void setAuxilioSaudeRequisicao(AuxilioSaudeRequisicaoDocumento auxilioSaudeRequisicao) {
    this.auxilioSaudeRequisicao = auxilioSaudeRequisicao;
  }


  public String getNomeArquivo() {
    return nomeArquivo;
  }


  public void setNomeArquivo(String nomeArquivo) {
    this.nomeArquivo = nomeArquivo;
  }


  public String getCaminhoArquivo() {
    return caminhoArquivo;
  }


  public void setCaminhoArquivo(String caminhoArquivo) {
    this.caminhoArquivo = caminhoArquivo;
  }


  public Date getDataInclusao() {
    return dataInclusao;
  }


  public void setDataInclusao(Date dataInclusao) {
    this.dataInclusao = dataInclusao;
  }


  public String getObservacao() {
    return observacao;
  }


  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }


}
