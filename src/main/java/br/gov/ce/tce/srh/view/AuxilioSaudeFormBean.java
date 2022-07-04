package br.gov.ce.tce.srh.view;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.ArquivoVO;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDocumento;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.ExibeCampoFormAuxilioSaude;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.enums.TipodeEmpresa;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.exception.UsuarioException;
import br.gov.ce.tce.srh.service.AuxilioSaudeRequisicaoService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.PessoaJuridicaService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.FileUtils;
import br.gov.ce.tce.srh.util.RelatorioUtil;


@Component("auxilioSaudeFormBean")
@Scope("view")
public class AuxilioSaudeFormBean extends ControllerViewBase<AuxilioSaudeRequisicao> implements Serializable {

  private static final long serialVersionUID = 3707425815443102633L;

  static Logger logger = Logger.getLogger(AuxilioSaudeFormBean.class);


  private boolean exibirCamposDependente = Boolean.FALSE;
  int contadorBeneficiario = 1;
  int contadorDependente = 1;

  @Autowired
  private RelatorioUtil relatorioUtil;

  @Autowired
  private AfastamentoFormBean afastamentoFormBean;

  @Autowired
  FuncionalService funcionalService;

  @Autowired
  AuxilioSaudeRequisicaoService entidadeService;

  @Autowired
  private PessoaJuridicaService pessoaJuridicaService;

  private static final String REMOVIDO_SUCESSO = "Arquivo removido com sucesso";

  private ExibeCampoFormAuxilioSaude exibeCampoFormAuxilioSaude;

  private List<PessoaJuridica> comboEmpresasCadastradas;

  private AuxilioSaudeRequisicaoDocumento auxSaudeRequisicaoDoc;

  private AuxilioSaudeRequisicao itemBeneficiario;
  private AuxilioSaudeRequisicao itemDependente;


  @PostConstruct
  private void init() {
    try {

      if (FacesUtil.getFlashParameter("entidade") instanceof AuxilioSaudeRequisicao) {
        inicializarDadosParaEdicao();
        isEdicao = true;
        consultar();

      } else {
        inicializar();
      }

    } catch (UsuarioException e) {
      logger.fatal(e.getMessage());
    } catch (Exception e) {
      // FacesUtil.addErroMessage("Erro ao carregar os dados. Operação cancelada.");
      logger.fatal(e.getMessage());
    }
  }

  private void inicializarDadosParaEdicao() {
    itemBeneficiario = new AuxilioSaudeRequisicao();
    itemDependente = new AuxilioSaudeRequisicao();

    AuxilioSaudeRequisicao entidadeEditar = entidadeService.getAuxilioSaudePorId((AuxilioSaudeRequisicao) FacesUtil.getFlashParameter("entidade"));

    List<AuxilioSaudeRequisicaoDocumento> documentoList = entidadeService.getListaArquivosPorIdAuxilio(entidadeEditar);
    entidadeEditar.setAuxilioSaudeRequisicaoDependenteList(entidadeService.getAuxilioSaudeDependenteList(entidadeEditar.getId()));
    entidadeEditar.setAuxilioSaudeRequisicaoBeneficiarioItemList(new ArrayList<AuxilioSaudeRequisicao>());

    entidadeEditar.getAuxilioSaudeRequisicaoBeneficiarioItemList().add(entidadeEditar);

    entidadeEditar.setAuxilioSaudeRequisicaoDocumentoBeneficiarioList(documentoList);

    setValorSolicitado(entidadeEditar);

    entidadeService.setValorMaximoSolicitadoPorIdade(entidadeEditar);

    setEntidade(entidadeEditar);
  }

  private void inicializar() throws Exception {
    createInstanceEntidade();
    itemBeneficiario = new AuxilioSaudeRequisicao();
    itemDependente = new AuxilioSaudeRequisicao();

    getEntidade().setUsuario(loginBean.getUsuarioLogado());
    entidadeService.setDadosIniciaisDaEntidadePorCpf(getEntidade(), getEntidade().getUsuario().getCpf());
    entidadeService.setValorMaximoSolicitadoPorIdade(getEntidade());
  }

  @Override
  public void consultar() {
    try {
      entidadeService.setDadosIniciaisDaEntidade(getEntidade());
      entidadeService.setValorMaximoSolicitadoPorIdade(getEntidade());

    } catch (NullPointerException e) {
      FacesUtil.addErroMessage(e.getMessage());
    } catch (Exception e) {

      FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
      logger.fatal(e.getMessage());
    }
  }


  @Override
  public void salvar() {
    try {
      if (isEdicao) {
        entidadeService.atualizar(getEntidade());
      } else {
        entidadeService.executarAntesSalvar(getEntidade(), getEntidade().getObservacao(), getEntidade().getFlAfirmaSerVerdadeiraInformacao());

        if (Boolean.TRUE.equals(entidadeService.isOK(getEntidade()))) {
          entidadeService.salvar(getEntidade().getAuxilioSaudeRequisicaoBeneficiarioItemList());        
        }
      }

      FacesUtil.addInfoMessage("Registro Salvo com sucesso!");

      logger.info("Operação realizada com sucesso");
    } catch (Exception e) {
      FacesUtil.addErroMessage("Ops! Não foi possível salvar a requisição, Por gentileza entre em contato o setor responsável");
      logger.error(e);
    }

    contadorBeneficiario = 1;
    contadorDependente = 1;
  }

  @Override
  public void salvar(boolean finalizar) {

  }

  public void deferir(boolean deferido) {
    logger.info("Iniciando o deferimento dos dados!");
    try {
      String msg = "";

      if (deferido) {
        getEntidade().setDataFimRequisicao(new Date());
        getEntidade().setStatusAprovacao(AuxilioSaudeRequisicao.DEFERIDO);
        msg = "Requisição foi deferida com sucesso";
      } else {
        getEntidade().setDataFimRequisicao(new Date());
        getEntidade().setStatusAprovacao(AuxilioSaudeRequisicao.INDEFERIDO);
        msg = "Requisição foi indeferida com sucesso";
      }

      if (getEntidade().getId() == null) {
        salvar();
      } else {
        entidadeService.atualizar(getEntidade());
      }

      FacesUtil.addInfoMessage(msg);
    } catch (Exception e) {
      FacesUtil.addErroMessage("Erro ao salvar o deferimentos dados dados");
      logger.error(e.getMessage());
    }
  }


  /***
   * metodo adiciona os dados do titular e validade se este é dependente, caso seja dependente o
   * metodo para cadastro de dependentes é chamado
   * 
   * @param beanEntidade
   * @param isTitular
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public void adicionar(AuxilioSaudeRequisicao bean, boolean isBeneficiario) {
    try {
      if (isBeneficiario) {
        adicionarDadosBeneficiario(bean);
      } else {
        adicionarDadosDependente(bean);
      }

      setValorSolicitado(getEntidade());
      fazerUploadArquivos(isBeneficiario);
    } catch (Exception e) {
      FacesUtil.addErroMessage(e.getMessage());
    }
  }

  public PessoaJuridica getPessoaJuridicaPorId(AuxilioSaudeRequisicao bean) {
    return entidadeService.getPessoaJuridicaPorId(bean.getPessoaJuridica(), comboEmpresasCadastradas);
  }

  private void adicionarDadosBeneficiario(AuxilioSaudeRequisicao bean) {
    checkPessoaJuridicaIsNull(bean);
    checkAnexoBeneficiarioIsNull();

    PessoaJuridica pessoaJuridica = getPessoaJuridicaPorId(bean);

    AuxilioSaudeRequisicao auxilioSaudeRequisicaoLocal = new AuxilioSaudeRequisicao(getEntidade().getFuncional(), loginBean.getUsuarioLogado(), pessoaJuridica, null, bean.getValorGastoPlanoSaude());
    getEntidade().adicionarDadosRequisicaoList(auxilioSaudeRequisicaoLocal);
  }

  private void checkAnexoBeneficiarioIsNull() {
    if (getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList() == null) {
      throw new NullPointerException("Ops!, Por favor adicone um anexo para continuar");
    }
  }

  /***
   * Metodo procura o item da lista de documentos, cria o diretorio se não existir e Faz o upload do
   * arquivo
   * 
   * @param isBeneficiario
   * @throws Exception
   */
  private void fazerUploadArquivos(boolean isBeneficiario) throws Exception {
    if (isBeneficiario) {
      criarDiretorioEfazerUploadArquivoBeneficiarioList();
    } else {
      criarDiretorioEfazerUploadArquivoDependenteList();
    }
  }

  private void criarDiretorioEfazerUploadArquivoBeneficiarioList() throws Exception {
    if (getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList() == null) {
      throw new NullPointerException("Ops!, Por favor adiconar um anexo não foi encontrado!");
    }

    for (AuxilioSaudeRequisicaoDocumento beanDoc : getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
      criarDiretorio(beanDoc);
      salvarArquivoDiretorio(beanDoc);
    }
  }

  private void criarDiretorioEfazerUploadArquivoDependenteList() throws Exception {
    for (AuxilioSaudeRequisicaoDocumento beanDoc : getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList()) {
      criarDiretorio(beanDoc);
      salvarArquivoDiretorio(beanDoc);
    }
  }

  private void criarDiretorio(AuxilioSaudeRequisicaoDocumento doc) throws Exception {
    FileUtils.criarDiretorio(doc.getCaminhoArquivo());
  }

  private void salvarArquivoDiretorio(AuxilioSaudeRequisicaoDocumento doc) throws Exception {
    FileUtils.upload(doc.getCaminhoCompleto(), doc.getArquivoVO().getComprovante());
  }


  public void adicionarDadosDependente(AuxilioSaudeRequisicao bean) {
    checkDepedenteSeleciona();
    checkPessoaJuridicaIsNull(bean);
    checkListaAnexoDependenteIsNull();

    /*** adicionar os dependentes na lista */
    getEntidade().adicionarDadosDependenteList(getAuxilioSaudeRequisicaoDependente(bean));
  }

  public AuxilioSaudeRequisicaoDependente getAuxilioSaudeRequisicaoDependente(AuxilioSaudeRequisicao bean) {
    PessoaJuridica pessoaJuridica = getPessoaJuridicaPorId(bean);
    return new AuxilioSaudeRequisicaoDependente(getEntidade(), getDependenteSelecionado(), pessoaJuridica, bean.getValorGastoPlanoSaude());
  }

  private void checkPessoaJuridicaIsNull(AuxilioSaudeRequisicao bean) {

    if (bean.isPessoaJuridicaNull()) {
      // if (comboEmpresasCadastradas == null || comboEmpresasCadastradas.isEmpty()) {
      throw new NullPointerException("Ops!. Por favor escolha a empresa do plano de saúde para continuar");
    }
  }

  private void checkListaAnexoDependenteIsNull() {
    if (getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList() == null || getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList().isEmpty()) {
      throw new NullPointerException("Ops!. Por favor adicione um anexo do dependente para continuar");
    }
  }

  private void checkDepedenteSeleciona() {
    if (getEntidade().getDependenteSelecionado() == null) {
      throw new NullPointerException("Ops!. Por favor escolha um dependente para continuar");
    }
  }

  /***
   * Metodo procura o dependente que usuario selecionou na lista
   * 
   * @return depentende encontrado
   */
  private Dependente getDependenteSelecionado() {
    try {
      return entidadeService.getDependentePorId(getEntidade().getDependenteSelecionado(), getEntidade().getDependentesComboList());
    } catch (NullPointerException e) {
      FacesUtil.addErroMessage(e.getMessage());
    }

    return null;
  }


  public List<PessoaJuridica> getComboEmpresasCadastradas() {

    try {

      if (isListaEmpresaJuridicaNull())
        this.comboEmpresasCadastradas = pessoaJuridicaService.findAllByTipo(TipodeEmpresa.PLANOS_SAUDE);

    } catch (Exception e) {
      FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de publicação. Operação cancelada.");
      logger.fatal(OCORREU_ERRO + e.getMessage());
    }

    return this.comboEmpresasCadastradas;
  }

  /***
   * Verificar se lista de empresa é nula return true se a lista for nula e false caso contrario
   */
  private boolean isListaEmpresaJuridicaNull() {
    if (this.comboEmpresasCadastradas == null)
      return Boolean.TRUE;

    return Boolean.FALSE;
  }

  /***
   * Salvar o arquivo para comprovar os gastos com o auxilio saude
   * 
   * @param event
   * @throws IllegalAccessException
   * @throws InstantiationException
   */
  public void uploadComprovanteBeneficario(FileUploadEvent event) {
    try {

      UploadedFile comprovante = event.getUploadedFile();

      ArquivoVO arquivoVO = getInstanciaArquivoVO(AuxilioSaudeRequisicaoDocumento.NOME_ARQUIVO_BENEFICIARIO, comprovante.getName(), comprovante.getData(), contadorBeneficiario);
      auxSaudeRequisicaoDoc = getInstanciaAuxDocumento(arquivoVO);
      auxSaudeRequisicaoDoc.adicionarNovoCaminhoArquivo(new Date(), getEntidade().getFuncional().getMatricula());

      getEntidade().adicionarComprovanteBeneficiarioList(auxSaudeRequisicaoDoc);
      contadorBeneficiario++;
    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage("Erro na gravação do comprovante.");
      logger.fatal(OCORREU_ERRO + e.getMessage());
    }

  }

  public void uploadComprovanteDependente(FileUploadEvent event) {
    try {
      UploadedFile comprovante = event.getUploadedFile();

      ArquivoVO arquivoVO = getInstanciaArquivoVO(AuxilioSaudeRequisicaoDocumento.NOME_ARQUIVO_DEPENDENTE, comprovante.getName(), comprovante.getData(), contadorDependente);

      auxSaudeRequisicaoDoc = getInstanciaAuxDocumento(arquivoVO);
      auxSaudeRequisicaoDoc.adicionarNovoCaminhoArquivo(new Date(), getEntidade().getFuncional().getMatricula());
      auxSaudeRequisicaoDoc.adicionarDependente(getEntidade());

      getEntidade().adicionarComprovanteDependenteList(auxSaudeRequisicaoDoc);

      contadorBeneficiario++;
    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage("Erro na gravação do comprovante.");
      logger.fatal(OCORREU_ERRO + e.getMessage());
    }

  }

  private ArquivoVO getInstanciaArquivoVO(String nome, String descricao, byte[] comprovante, int contador) {

    return new ArquivoVO(nome, descricao, ArquivoVO.CAMINHO_PARA_SALVAR_ARQUIVO, comprovante, contador);
  }

  public AuxilioSaudeRequisicaoDocumento getInstanciaAuxDocumento(ArquivoVO arquivoVO) {
    return new AuxilioSaudeRequisicaoDocumento(getEntidade(), new Date(), arquivoVO);
  }

  public void deletarAnexo(AuxilioSaudeRequisicaoDocumento bean, boolean isBeneficiario) {
    if (isBeneficiario) {
      getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList().remove(bean);

      FacesUtil.addInfoMessage(REMOVIDO_SUCESSO);
    } else {
      getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList().remove(bean);
      FacesUtil.addInfoMessage(REMOVIDO_SUCESSO);
    }

  }

  /**
   * deletar os registro da lista e arquivo
   * 
   * @param bean
   */
  public void deletar(AuxilioSaudeRequisicao bean) {
    checkAnexoBeneficiarioIsNull();
    for (AuxilioSaudeRequisicaoDocumento auxDoc : getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
      if (auxDoc.getAuxilioSaudeRequisicao().equals(bean)) {
        if (auxDoc.getId() == null) {
          FileUtils.removerArquivo(auxDoc.getCaminhoCompleto());
        } else {
          auxDoc.setDeletado(Boolean.TRUE);
        }
      }
    }

    getEntidade().getAuxilioSaudeRequisicaoBeneficiarioItemList().remove(bean);

    setValorSolicitado(getEntidade());

    FacesUtil.addInfoMessage(REMOVIDO_SUCESSO);
  }

  /**
   * deletar os registro da lista e arquivo
   * 
   * @param bean
   */
  public void deletarDependente(AuxilioSaudeRequisicaoDependente bean) {
    checkListaAnexoDependenteIsNull();

    for (AuxilioSaudeRequisicaoDocumento auxDoc : getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList()) {
      if (auxDoc.getAuxilioSaudeRequisicaoDependente().equals(bean)) {
        if (auxDoc.getId() == null) {
          FileUtils.removerArquivo(auxDoc.getCaminhoCompleto());
        } else {
          auxDoc.setDeletado(Boolean.TRUE);
        }
      }
    }

    getEntidade().getAuxilioSaudeRequisicaoDependenteList().remove(bean);
    
    setValorSolicitado(getEntidade());
    
    FacesUtil.addInfoMessage(REMOVIDO_SUCESSO);
  }

  /***
   * Procurar anexo na lista e visualizar
   * 
   * @param bean
   * @param isBeneficiario
   */
  public void visualizarAnexoBeneficiario(AuxilioSaudeRequisicao bean) {
    try {
      for (AuxilioSaudeRequisicaoDocumento auxDoc : getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
        if (auxDoc.getId() != null) {
          FileUtils.visualizar(auxDoc.getCaminhoCompleto(), relatorioUtil);
        }

        if (auxDoc.getAuxilioSaudeRequisicao().getId() != null) {
          FileUtils.visualizar(auxSaudeRequisicaoDoc.getCaminhoCompleto(), relatorioUtil);
          break;
        } else {
          FileUtils.visualizar(auxSaudeRequisicaoDoc.getCaminhoCompleto(), relatorioUtil);
          break;
        }
      }
    } catch (FileNotFoundException e) {
      logger.fatal(OCORREU_ERRO + e.getMessage());
    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage(e.getMessage());
      logger.warn(OCORREU_ERRO + e.getMessage());
    } catch (Exception e) {
      FacesUtil.addErroMessage("Ocorreu algum erro na geração do arquivo. Operação cancelada.");
      logger.fatal(OCORREU_ERRO + e.getMessage());
    }
  }


  /***
   * Procurar anexo na lista e visualizar
   * 
   * @param bean
   * @param isBeneficiario
   */
  public void visualizarAnexoDependente(AuxilioSaudeRequisicaoDependente bean) {
    try {
      for (AuxilioSaudeRequisicaoDocumento auxDoc : getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList()) {
        if (auxDoc.getAuxilioSaudeRequisicaoDependente().equals(bean)) {
          FileUtils.visualizar(auxDoc.getCaminhoCompleto(), relatorioUtil);
          break;
        }
      }


    } catch (FileNotFoundException e) {
      logger.fatal(OCORREU_ERRO + e.getMessage());
    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage(e.getMessage());
      logger.warn(OCORREU_ERRO + e.getMessage());
    } catch (Exception e) {
      FacesUtil.addErroMessage("Ocorreu algum erro na geração do arquivo. Operação cancelada.");
      logger.fatal(OCORREU_ERRO + e.getMessage());
    }
  }

  public void exibirCamposFormChange(boolean isBeneficiario) {
    exibeCampoFormAuxilioSaude = new ExibeCampoFormAuxilioSaude();

    if (isBeneficiario)
      exibeCampoFormAuxilioSaude.setExibirCamposInputFormBeneficiario(isBeneficiario);

  }

  public ExibeCampoFormAuxilioSaude getExibeCampoFormAuxilioSaude() {
    return exibeCampoFormAuxilioSaude;
  }

  public AfastamentoFormBean getAfastamentoFormBean() {
    return afastamentoFormBean;
  }

  public AuxilioSaudeRequisicao getItemBeneficiario() {
    return itemBeneficiario;
  }

  public AuxilioSaudeRequisicao getItemDependente() {
    return itemDependente;
  }

  /***
   * somar o valor do beneficiario com o dependente
   * 
   * @param bean
   */  
  protected void setValorSolicitado(AuxilioSaudeRequisicao bean) {
    Double valor = 0.0;

    if (bean.checkBeneficiarioItemListNotNull()) {
      for (AuxilioSaudeRequisicao auxBenef : bean.getAuxilioSaudeRequisicaoBeneficiarioItemList()) {
        if (bean.getValorTotalSolicitado() == null) {
          valor = auxBenef.getValorGastoPlanoSaude();
        } else {
          valor += auxBenef.getValorGastoPlanoSaude();
        }
      }
      
      bean.setValorTotalSolicitado(valor);
    }

    if (bean.checkDependenteItemListNotNull()) {
      for (AuxilioSaudeRequisicaoDependente auxDep : bean.getAuxilioSaudeRequisicaoDependenteList()) {
        if (bean.getValorTotalSolicitado() == null) {
          valor = auxDep.getValorGastoPlanoSaude();
        } else {
          valor += auxDep.getValorGastoPlanoSaude();
        }
      }
      
      bean.setValorTotalSolicitado(valor);
    }    
  }

  public boolean getExibirCamposDependente() {
    if (getEntidade().getDependentesComboList() != null) {
      exibirCamposDependente = Boolean.TRUE;
    }
    return exibirCamposDependente;
  }

  public void setExibirCamposDependente(boolean exibirCamposDependente) {
    this.exibirCamposDependente = exibirCamposDependente;
  }


}
