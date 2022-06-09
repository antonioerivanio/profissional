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
import javax.persistence.ManyToOne;
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
    allocationSize = 1)
public class AuxilioSaudeRequisicao extends BasicEntity<Long> implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_AUXILIOSAUDEREQ")
  private Long id;

  @ManyToOne
  @JoinColumn(name = "IDFUNCIONAL")
  private Funcional funcional;


  @ManyToOne
  @JoinColumn(name = "IDUSUARIO")
  private Usuario usuario;

  @ManyToOne
  @JoinColumn(name = "IDPESSOAJURIDICA")
  private PessoaJuridica pessoaJuridica;

  @Column(name = "VALOR_PLANOSAUDE")
  private Double valorGastoPlanoSaude;
  
  @Column(name = "STATUS")
  private String Status;

  @Temporal(TemporalType.DATE)
  @Column(name = "DT_INICIOREQ")
  private Date dataInicioRequisicao;
  
  
  public static String DEFERIDO = "DEFERIDO";
  public static String INDEFERIDO = "INDEFERIDO";

  /**
   * flag que será marcada quando o Titular ou solicitante marca o campo (CONCORDO) da declaração
   */
  @Column(name = "FLG_AFIRMACAOSERVERDADE")
  private boolean flAfirmaSerVerdadeiraInformacao;

  /* data da aprovação/reprovação da requisição */
  @Temporal(TemporalType.DATE)
  @Column(name = "DT_FIMREQ")
  private Date dataFImRequisicao;

  @Column(name = "OBSERVACAO")
  private String observacao;


  // Beans que não são persistiveis -
  @Transient
  private String declaracao;

  @Transient
  private Dependente dependenteSelecionado;

  @Transient
  private List<AuxilioSaudeRequisicao> auxilioSaudeRequisicaoList;

  @Transient
  private List<AuxilioSaudeRequisicaoDependente> auxilioSaudeRequisicaoDependenteList;

  @Transient
  private List<Dependente> dependentesComboList;

  public AuxilioSaudeRequisicao() {

  }



  public AuxilioSaudeRequisicao(Funcional funcional, Usuario usuario,
      PessoaJuridica pessoaJuridica, Double valorGastoPlanoSaude,
      Date dataInicioRequisicao, boolean flAfirmaSerVerdadeiraInformacao
      ) {
    super();
    
    this.funcional = funcional;
    this.usuario = usuario;
    this.pessoaJuridica = pessoaJuridica;
    this.valorGastoPlanoSaude = valorGastoPlanoSaude;    
    this.dataInicioRequisicao = dataInicioRequisicao;
    this.flAfirmaSerVerdadeiraInformacao = flAfirmaSerVerdadeiraInformacao;
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


  
  public void adicionarDadosRequisicao(AuxilioSaudeRequisicao bean) {

    if (auxilioSaudeRequisicaoList == null) {
      auxilioSaudeRequisicaoList = new ArrayList<>();
    }

    auxilioSaudeRequisicaoList.add(bean);
  }

  public void adicionarDadosDependente(AuxilioSaudeRequisicaoDependente beanDep) {
   // beanDep.setAuxilioSaudeRequisicao(this);

    if (auxilioSaudeRequisicaoDependenteList == null) {
      auxilioSaudeRequisicaoDependenteList = new ArrayList<>();
    }

    auxilioSaudeRequisicaoDependenteList.add(beanDep);
    beanDep.getAuxilioSaudeRequisicao().setAuxilioSaudeRequisicaoDependenteList(auxilioSaudeRequisicaoDependenteList);
  }



  // getters e setters

  @Override
  public Long getId() {
    // TODO Auto-generated method stub
    return id;
  }


  @Override
  public void setId(Long id) {
    this.id = id;
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

  public Double getValorGastoPlanoSaude() {
    return valorGastoPlanoSaude;
  }


  public void setValorGastoPlanoSaude(Double valorGastoPlanoSaude) {
    this.valorGastoPlanoSaude = valorGastoPlanoSaude;
  }

  public String getStatus() {
    return Status;
  }

  public void setStatus(String status) {
    Status = status;
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

  public Dependente getDependenteSelecionado() {
    return dependenteSelecionado;
  }

  public boolean getFlAfirmaSerVerdadeiraInformacao() {
    return flAfirmaSerVerdadeiraInformacao;
  }

  public void setFlAfirmaSerVerdadeiraInformacao(boolean flAfirmaSerVerdadeiraInformacao) {
    this.flAfirmaSerVerdadeiraInformacao = flAfirmaSerVerdadeiraInformacao;
  }


  public void setDependenteSelecionado(Dependente dependenteSelecionado) {
    this.dependenteSelecionado = dependenteSelecionado;
  }

  public List<AuxilioSaudeRequisicao> getAuxilioSaudeRequisicaoList() {
    return auxilioSaudeRequisicaoList;
  }

  public void setAuxilioSaudeRequisicaoList(
      List<AuxilioSaudeRequisicao> auxilioSaudeRequisicaoList) {
    this.auxilioSaudeRequisicaoList = auxilioSaudeRequisicaoList;
  }


  public List<Dependente> getDependentesComboList() {
    return dependentesComboList;
  }

  public void setDependentesComboList(List<Dependente> dependentesComboList) {
    this.dependentesComboList = dependentesComboList;
  }

  public List<AuxilioSaudeRequisicaoDependente> getAuxilioSaudeRequisicaoDependenteList() {
    return auxilioSaudeRequisicaoDependenteList;
  }

  public void setAuxilioSaudeRequisicaoDependenteList(
      List<AuxilioSaudeRequisicaoDependente> auxilioSaudeRequisicaoDependenteList) {
    this.auxilioSaudeRequisicaoDependenteList = auxilioSaudeRequisicaoDependenteList;
  }


}
