package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.ArrayList;
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
import org.richfaces.model.UploadedFile;

/***
 * Classe que amazena os arquivos que compravam os gastos com saude dos Beneficiarios e seus
 * dependentes.
 * 
 * @author erivanio.cruz
 * @since 03/06/2022
 */
@Entity
@Table(name = "FP_AUXILIOSAUDEDOC", schema = DatabaseMetadata.SCHEMA_SRH)
@SequenceGenerator(name = "SEQ_FP_AUXILIOSAUDEDOC", sequenceName = "SEQ_FP_AUXILIOSAUDEDOC",
                          schema = DatabaseMetadata.SCHEMA_SRH, allocationSize = 1, initialValue = 1)
public class AuxilioSaudeRequisicaoDocumento extends BasicEntity<Long> implements Serializable {

  private static final long serialVersionUID = 481877607288972254L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FP_AUXILIOSAUDEDOC")
  private Long id;


  @ManyToOne
  @JoinColumn(name = "IDAUXILIOSAUDEREQ")
  private AuxilioSaudeRequisicao auxilioSaudeRequisicao;

  @ManyToOne
  @JoinColumn(name = "IDAUXILIOSAUDEREQDEP")
  private AuxilioSaudeRequisicaoDependente auxilioSaudeRequisicaoDependente;

  @Column(name = "NOME_ARQ")
  private String nomeArquivo;

  @Column(name = "CAMINHO_ARQ")
  private String caminhoArquivo;

  @Column(name = "DESC_ARQUIVO")
  private String descricaoArquivo;
  
  @Temporal(TemporalType.DATE)
  @Column(name = "DT_INCLUSAO")
  private Date dataInclusao;

  @Column(name = "FLG_DELETADO")
  private boolean isDeletado;

  @Temporal(TemporalType.DATE)
  @Column(name = "DT_DELETE")
  private Date dataDelete;

  @Transient
  private byte[] comprovante;

  @Transient
  private String caminhoTemporario;

  @Transient
  private List<AuxilioSaudeRequisicaoDocumento> auxilioSaudeRequisicaoDocumentoList;

  

  public AuxilioSaudeRequisicaoDocumento() {

  }


  public AuxilioSaudeRequisicaoDocumento(AuxilioSaudeRequisicao auxilioSaudeRequisicao,
                            AuxilioSaudeRequisicaoDependente auxilioSaudeRequisicaoDependente, String nomeArquivo,
                            String caminhoArquivo, String descricaoArquivo, Date dataInclusao, byte[] comprovante) {
    super();
    this.auxilioSaudeRequisicao = auxilioSaudeRequisicao;
    this.auxilioSaudeRequisicaoDependente = auxilioSaudeRequisicaoDependente;
    this.nomeArquivo = nomeArquivo;
    this.caminhoArquivo = caminhoArquivo;
    this.descricaoArquivo = descricaoArquivo;
    this.dataInclusao = dataInclusao;
    this.comprovante = comprovante;
  }


  public void adicionarComprovanteList(AuxilioSaudeRequisicaoDocumento beanDoc) {

    if (auxilioSaudeRequisicaoDocumentoList == null) {
      auxilioSaudeRequisicaoDocumentoList = new ArrayList<>();
    }

    auxilioSaudeRequisicaoDocumentoList.add(beanDoc);
  }


  @Override
  public Long getId() {
    // TODO Auto-generated method stub
    return id;
  }


  @Override
  public void setId(Long id) {
    this.id = id;
  }

  public AuxilioSaudeRequisicao getAuxilioSaudeRequisicao() {
    return auxilioSaudeRequisicao;
  }

  public void setAuxilioSaudeRequisicao(AuxilioSaudeRequisicao auxilioSaudeRequisicao) {
    this.auxilioSaudeRequisicao = auxilioSaudeRequisicao;
  }

  public AuxilioSaudeRequisicaoDependente getAuxilioSaudeRequisicaoDependente() {
    return auxilioSaudeRequisicaoDependente;
  }


  public void setAuxilioSaudeRequisicaoDependente(AuxilioSaudeRequisicaoDependente auxilioSaudeRequisicaoDependente) {
    this.auxilioSaudeRequisicaoDependente = auxilioSaudeRequisicaoDependente;
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


  public boolean isDeletado() {
    return isDeletado;
  }


  public void setDeletado(boolean isDeletado) {
    this.isDeletado = isDeletado;
  }


  public Date getDataDelete() {
    return dataDelete;
  }


  public void setDataDelete(Date dataDelete) {
    this.dataDelete = dataDelete;
  }

  public String getDescricaoArquivo() {
    return descricaoArquivo;
  }


  public void setDescricaoArquivo(String descricaoArquivo) {
    this.descricaoArquivo = descricaoArquivo;
  }


  public String getCaminhoTemporario() {
    return caminhoTemporario;
  }


  public void setCaminhoTemporario(String caminhoTemporario) {
    this.caminhoTemporario = caminhoTemporario;
  }


  public byte[] getComprovante() {
    return comprovante;
  }


  public void setComprovante(byte[] comprovante) {
    this.comprovante = comprovante;
  }


  public List<AuxilioSaudeRequisicaoDocumento> getAuxilioSaudeRequisicaoDocumentoList() {
    return auxilioSaudeRequisicaoDocumentoList;
  }


  public void setAuxilioSaudeRequisicaoDocumentoList(
                            List<AuxilioSaudeRequisicaoDocumento> auxilioSaudeRequisicaoDocumentoList) {
    this.auxilioSaudeRequisicaoDocumentoList = auxilioSaudeRequisicaoDocumentoList;
  }


}
