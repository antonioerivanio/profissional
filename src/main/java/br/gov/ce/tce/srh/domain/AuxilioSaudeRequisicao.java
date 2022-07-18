package br.gov.ce.tce.srh.domain;

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
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import br.gov.ce.tce.srh.enums.BaseCalculoValorRestituido;
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
@SequenceGenerator(name = "SEQ_FP_AUXILIOSAUDEREQ", sequenceName = "SEQ_FP_AUXILIOSAUDEREQ", allocationSize = 1)
public class AuxilioSaudeRequisicao extends BasicEntity<Long> implements BeanEntidade {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  public static final String DEFERIDO = "DEFERIDO";
  public static final String INDEFERIDO = "INDEFERIDO";
  public static final String ATIVO = "SIM";
  public static final String INATIVO = "NÃO";

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FP_AUXILIOSAUDEREQ")
  private Long id;
  
  @ManyToOne
  @JoinColumn(name = "IDFUNCIONAL")
  private Funcional funcional;

  @ManyToOne
  @JoinColumn(name = "IDUSUARIO")
  private Usuario usuario;

  @Column(name = "STATUSAPROVACAO")
  private String statusAprovacao;

  @Column(name = "STATUSFUNCIONAL")
  private String statusFuncional;

  @Temporal(TemporalType.DATE)
  @Column(name = "DT_INICIOREQ")
  private Date dataInicioRequisicao;

  @Column(name = "VALOR_TOTALSOLICITADO")
  private Double valorTotalSolicitado;

  /* data da aprovação/reprovação da requisição */
  @Temporal(TemporalType.DATE)
  @Column(name = "DT_FIMREQ")
  private Date dataFimRequisicao;

  @Temporal(TemporalType.DATE)
  @Column(name = "DT_ALTERACAO")
  private Date dataAlteracao;

  @Column(name = "FLG_DELETADO")
  private boolean isDeletado;

  @Temporal(TemporalType.DATE)
  @Column(name = "DT_DELETE")
  private Date dataDelete;


  /**
   * flag que será marcada quando o Titular ou solicitante marca o campo (CONCORDO) da declaração
   */  
  @Column(name = "FLG_AFIRMACAOSERVERDADE")
  private boolean flAfirmaSerVerdadeiraInformacao;

  @Column(name = "OBSERVACAO")
  private String observacao;

  // Variaveis ou Beans que não são persistiveis -
  @Transient
  private ArquivoVO arquivoVO;

  @Transient
  private String declaracao = Texto.getDeclaracao();

  // Beans que não são persistiveis -
  @Transient
  private String aviso = Texto.getAviso();

  @Transient
  private Dependente dependenteSelecionado;
  
  @Transient
  private AuxilioSaudeRequisicaoItem auxilioSaudeRequisicaoItem;

  @Transient
  private Double valorMaximoAserRestituido;

  @Transient
  private AuxilioSaudeBaseCalculo auxilioSaudeBaseCalculo;

  @Transient
  private List<AuxilioSaudeRequisicaoItem> auxilioSaudeRequisicaoBeneficiarioItemList;

  @Transient
  private List<AuxilioSaudeRequisicaoDependente> auxilioSaudeRequisicaoDependenteList;

  @Transient
  private List<Dependente> dependentesComboList;

 

  @Transient
  private List<AuxilioSaudeRequisicaoDocumento> auxilioSaudeRequisicaoDocumentoDependenteList;


  public AuxilioSaudeRequisicao() {

  }


  public AuxilioSaudeRequisicao(Funcional funcional, Usuario usuario, Dependente dependenteSelecionado) {
    super();

    this.funcional = funcional;
    this.usuario = usuario;
    this.dependenteSelecionado = dependenteSelecionado;
  }

  public String getDeclaracao() {
    return declaracao;
  }

  public ArquivoVO getArquivoVO() {
    return arquivoVO;
  }

  public void setArquivoVO(ArquivoVO arquivoVO) {
    this.arquivoVO = arquivoVO;
  }

  public void adicionarDadosRequisicaoList(AuxilioSaudeRequisicaoItem bean) {
    if (auxilioSaudeRequisicaoBeneficiarioItemList == null) {
      auxilioSaudeRequisicaoBeneficiarioItemList = new ArrayList<>();
    }

    auxilioSaudeRequisicaoBeneficiarioItemList.add(bean);
  }

  public void adicionarDadosDependenteList(AuxilioSaudeRequisicaoDependente beanDep) {
    if (auxilioSaudeRequisicaoDependenteList == null) {
      auxilioSaudeRequisicaoDependenteList = new ArrayList<>();
    }

    auxilioSaudeRequisicaoDependenteList.add(beanDep);
    beanDep.getAuxilioSaudeRequisicao().setAuxilioSaudeRequisicaoDependenteList(auxilioSaudeRequisicaoDependenteList);
  }

  public void adicionarAuxilioSaudeBeneficiarioItemList(List<AuxilioSaudeRequisicaoItem> beanItemList) {
    if (auxilioSaudeRequisicaoBeneficiarioItemList == null) {
      auxilioSaudeRequisicaoBeneficiarioItemList = new ArrayList<>();
    }

    auxilioSaudeRequisicaoBeneficiarioItemList.addAll(beanItemList);
  }

  public void adicionarComprovanteDependenteList(List<AuxilioSaudeRequisicaoDocumento> beanDocList) {
    if (auxilioSaudeRequisicaoDocumentoDependenteList == null) {
      auxilioSaudeRequisicaoDocumentoDependenteList = new ArrayList<>();
    }

    auxilioSaudeRequisicaoDocumentoDependenteList.addAll(beanDocList);
  }

  public boolean checkDependenteItemListNotNull() {
    return getAuxilioSaudeRequisicaoDependenteList() != null && !getAuxilioSaudeRequisicaoDependenteList().isEmpty();
  }

  public boolean checkBeneficiarioItemListNull() {
    return getAuxilioSaudeRequisicaoBeneficiarioItemList() == null || getAuxilioSaudeRequisicaoBeneficiarioItemList().isEmpty();
  }


  // getters e setters

  @Override
  public Long getId() {
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


  public String getStatusFuncional() {
    if (this.statusFuncional == null) {
      return getFuncional() != null && getFuncional().getStatus() != null && getFuncional().getStatus() == 1 ? AuxilioSaudeRequisicao.ATIVO : AuxilioSaudeRequisicao.INATIVO;
    }

    return statusFuncional;
  }

  public void setStatusFuncional(String statusFuncional) {
    this.statusFuncional = statusFuncional;
  }

  public Date getDataInicioRequisicao() {
    return dataInicioRequisicao;
  }

  public void setDataInicioRequisicao(Date dataInicioRequisicao) {
    this.dataInicioRequisicao = dataInicioRequisicao;
  }

  public Date getDataFimRequisicao() {
    return dataFimRequisicao;
  }

  public void setDataFimRequisicao(Date dataFimRequisicao) {
    this.dataFimRequisicao = dataFimRequisicao;
  }

  public Date getDataAlteracao() {
    return dataAlteracao;
  }

  public void setDataAlteracao(Date dataAlteracao) {
    this.dataAlteracao = dataAlteracao;
  }

  public String getObservacao() {
    return observacao;
  }

  public void setObservacao(String observacao) {
    this.observacao = observacao;
  }

  public String getStatusAprovacao() {
    return statusAprovacao == null ? "Aguardando deferimento" : statusAprovacao;
  }

  public void setStatusAprovacao(String statusAprovacao) {
    this.statusAprovacao = statusAprovacao;
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

  public String getAviso() {
    return aviso;
  }

  public void setAviso(String aviso) {
    this.aviso = aviso;
  }

  public Double getValorMaximoAserRestituido() {
    return valorMaximoAserRestituido;
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


  public void setValorMaximoAserRestituido(Double valorMaximoAserRestituido) {
    this.valorMaximoAserRestituido = valorMaximoAserRestituido;
  }

  public Double getValorTotalSolicitado() {
    return valorTotalSolicitado;
  }

  public void setValorTotalSolicitado(Double valorTotalSolicitado) {
    this.valorTotalSolicitado = valorTotalSolicitado;
  }

  public void setDependenteSelecionado(Dependente dependenteSelecionado) {
    this.dependenteSelecionado = dependenteSelecionado;
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

  public void setAuxilioSaudeRequisicaoDependenteList(List<AuxilioSaudeRequisicaoDependente> auxilioSaudeRequisicaoDependenteList) {
    this.auxilioSaudeRequisicaoDependenteList = auxilioSaudeRequisicaoDependenteList;
  }

  public List<AuxilioSaudeRequisicaoItem> getAuxilioSaudeRequisicaoBeneficiarioItemList() {
    return auxilioSaudeRequisicaoBeneficiarioItemList;
  }

  public void setAuxilioSaudeRequisicaoBeneficiarioItemList(List<AuxilioSaudeRequisicaoItem> auxilioSaudeRequisicaoBeneficiarioItemList) {
    this.auxilioSaudeRequisicaoBeneficiarioItemList = auxilioSaudeRequisicaoBeneficiarioItemList;
  }

  public List<AuxilioSaudeRequisicaoDocumento> getAuxilioSaudeRequisicaoDocumentoDependenteList() {
    return auxilioSaudeRequisicaoDocumentoDependenteList;
  }

  public void setAuxilioSaudeRequisicaoDocumentoDependenteList(List<AuxilioSaudeRequisicaoDocumento> auxilioSaudeRequisicaoDocumentoDependenteList) {
    this.auxilioSaudeRequisicaoDocumentoDependenteList = auxilioSaudeRequisicaoDocumentoDependenteList;
  }

  public AuxilioSaudeBaseCalculo getAuxilioSaudeBaseCalculo() {
    return auxilioSaudeBaseCalculo;
  }

  public void setAuxilioSaudeBaseCalculo(AuxilioSaudeBaseCalculo auxilioSaudeBaseCalculo) {
    this.auxilioSaudeBaseCalculo = auxilioSaudeBaseCalculo;
  }  

  public AuxilioSaudeRequisicaoItem getAuxilioSaudeRequisicaoItem() {
    return auxilioSaudeRequisicaoItem;
  }

  public void setAuxilioSaudeRequisicaoItem(AuxilioSaudeRequisicaoItem auxilioSaudeRequisicaoItem) {
    this.auxilioSaudeRequisicaoItem = auxilioSaudeRequisicaoItem;
  }  


  public void gerarTabelaAuxilioSaudeBase() {
    List<AuxilioSaudeBaseCalculo> auxilioSaudeBaseCalculoList = new ArrayList<AuxilioSaudeBaseCalculo>();
    auxilioSaudeBaseCalculoList.add(new AuxilioSaudeBaseCalculo("ATÉ 30", BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_ATE_30ANOS));
    auxilioSaudeBaseCalculoList.add(new AuxilioSaudeBaseCalculo("31-40", BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_31_ATE_40ANOS));
    auxilioSaudeBaseCalculoList.add(new AuxilioSaudeBaseCalculo("41-50", BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_41_ATE_50ANOS));
    auxilioSaudeBaseCalculoList.add(new AuxilioSaudeBaseCalculo("51-60", BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_51_ATE_60ANOS));
    auxilioSaudeBaseCalculoList.add(new AuxilioSaudeBaseCalculo("A PARTIR DE 61", BaseCalculoValorRestituido.VALOR_MAXIMO_PARA_PESSOA_IDADE_SUPERIOR_60ANOS));

    getAuxilioSaudeBaseCalculo().setAuxilioSaudeBaseCalculos(auxilioSaudeBaseCalculoList);
  }

}
