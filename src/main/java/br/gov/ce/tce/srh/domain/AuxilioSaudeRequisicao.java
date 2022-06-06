package br.gov.ce.tce.srh.domain;

import java.io.Serializable;
import java.util.ArrayList;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import br.gov.ce.tce.srh.sca.domain.Usuario;

/***
 * Classe que amazena os dados das solicitações dos colaboradores e seus dependentes para
 * ressarcimento dos valores de gastos particulares com planos de saúde, odontologicos, etc..
 * 
 * @author erivanio.cruz
 * @since 03/06/2022
 */
@Entity
@Table(name = "FP_AUXILIOSAUDEREQ", schema = DatabaseMetadata.SCHEMA_SRH)
@SequenceGenerator(name = "SEQ_AUXILIOSAUDEREQ", sequenceName = "SEQ_AUXILIOSAUDEREQ",
    schema = DatabaseMetadata.SCHEMA_SRH, allocationSize = 1, initialValue = 1)
public class AuxilioSaudeRequisicao extends BasicEntity<Long> implements Serializable {

  private static final long serialVersionUID = 481877607288972254L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AUXILIOSAUDEREQ")
  private Long id;

  @Column(name = "IDFUNCIONAL")
  private Funcional funcional;

  @Column(name = "IDUSUARIO")
  private Usuario usuario;

  @Column(name = "ID_PESSOAJURIDICA")
  private PessoaJuridica pessoaJuridica;

  @OneToMany
  @JoinColumn(name = "ID_DEPENDENTES")
  private List<Dependente> dependenteList;

  @Column(name = "VALOR_PLANOSAUDE")
  private Double valorGastoPlanoSaude;

  @Enumerated(EnumType.ORDINAL)
  @Column(name = "FL_VALIDADO")
  private Validacoes flValidado;

  @Temporal(TemporalType.DATE)
  @Column(name = "DT_INICIOREQ")
  private Date dataInicioRequisicao;

  /* data da aprovação/reprovação da requisição */
  @Temporal(TemporalType.DATE)
  @Column(name = "DT_FIMREQ")
  private Date dataFImRequisicao;

  @Column(name = "OBSERVACAO")
  private String observacao;

  @Transient
  private String declaracao;

  @Transient
  private List<AuxilioSaudeRequisicao> auxilioSaudeRequisicaoList;
 
  public AuxilioSaudeRequisicao() {

  }


  static enum Validacoes {
    DEFERIDO, INDEFERIDO
  }


  public String getDeclaracao() {
    StringBuilder declaracao =
        new StringBuilder("Declaro que estou ciente que a inveracidade da informação ");
    declaracao.append("contida neste documento, por mim firmado, constitui prática de ");
    declaracao.append("infração disciplinar, passível de punição na forma da lei, e ");
    declaracao.append("que não recebo auxílio-saúde semelhante nem possuo programa de ");
    declaracao
        .append("assistência à saúde custeado integral ou parcialmente pelos cofres públicos ");

    return declaracao.toString();
  };


  public void adicionarRequisiscao(Double valor, PessoaJuridica pessoaJuridica) {
    
    AuxilioSaudeRequisicao auxilioSaudeRequisicaoLocal = new AuxilioSaudeRequisicao();
    auxilioSaudeRequisicaoLocal.setPessoaJuridica(pessoaJuridica);
    auxilioSaudeRequisicaoLocal.setValorGastoPlanoSaude(valorGastoPlanoSaude);
    
    if (auxilioSaudeRequisicaoList == null) {
      auxilioSaudeRequisicaoList = new ArrayList<>();
    }
    
    auxilioSaudeRequisicaoList.add(auxilioSaudeRequisicaoLocal);
    
    /*
     * if (auxilioSaudeRequisicaoList.isEmpty()) {
     * auxilioSaudeRequisicaoList.add(auxilioSaudeRequisicaoLocal); } else { if
     * (!auxilioSaudeRequisicaoList.contains(auxilioSaudeRequisicaoLocal)) {
     * auxilioSaudeRequisicaoList.add(auxilioSaudeRequisicaoLocal); } }
     */
  }


  // getters e setters

  @Override
  public Long getId() {
    // TODO Auto-generated method stub
    return null;
  }


  @Override
  public void setId(Long id) {
    // TODO Auto-generated method stub

  }


  public Funcional getFuncional() {
    return funcional;
  }


  public void setFuncional(Funcional funcional) {
    this.funcional = funcional;
  }


  public Usuario getUsuario() {
    return usuario;
  }


  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }


  public PessoaJuridica getPessoaJuridica() {
    return pessoaJuridica;
  }


  public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
    this.pessoaJuridica = pessoaJuridica;
  }


  public List<Dependente> getDependenteList() {
    return dependenteList;
  }


  public void setDependenteList(List<Dependente> dependenteList) {
    this.dependenteList = dependenteList;
  }


  public Double getValorGastoPlanoSaude() {
    return valorGastoPlanoSaude;
  }


  public void setValorGastoPlanoSaude(Double valorGastoPlanoSaude) {
    this.valorGastoPlanoSaude = valorGastoPlanoSaude;
  }


  public Validacoes getFlValidado() {
    return flValidado;
  }


  public void setFlValidado(Validacoes flValidado) {
    this.flValidado = flValidado;
  }


  public Date getDataInicioRequisicao() {
    return dataInicioRequisicao;
  }


  public void setDataInicioRequisicao(Date dataInicioRequisicao) {
    this.dataInicioRequisicao = dataInicioRequisicao;
  }


  public Date getDataFImRequisicao() {
    return dataFImRequisicao;
  }


  public void setDataFImRequisicao(Date dataFImRequisicao) {
    this.dataFImRequisicao = dataFImRequisicao;
  }


  public String getObservacao() {
    return observacao;
  }


  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }


  public List<AuxilioSaudeRequisicao> getAuxilioSaudeRequisicaoList() {
    return auxilioSaudeRequisicaoList;
  }


  public void setAuxilioSaudeRequisicaoList(
      List<AuxilioSaudeRequisicao> auxilioSaudeRequisicaoList) {
    this.auxilioSaudeRequisicaoList = auxilioSaudeRequisicaoList;
  }



}
