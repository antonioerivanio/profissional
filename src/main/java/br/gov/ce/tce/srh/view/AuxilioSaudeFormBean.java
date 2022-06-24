package br.gov.ce.tce.srh.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.apache.lucene.document.Field.Index;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.exception.FileUploadException;
import org.richfaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.AfastamentoESocial;
import br.gov.ce.tce.srh.domain.ArquivoVO;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDocumento;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.ExibeCampoFormAuxilioSaude;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.enums.EmpresaAreaSaude;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
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

  @Autowired
  private LoginBean loginBean;

  @Autowired
  private RelatorioUtil relatorioUtil;

  @Autowired
  private AfastamentoFormBean afastamentoFormBean;

  @Autowired
  FuncionalService funcionalService;

  @Autowired
  AuthenticationService authenticationService;

  @Autowired
  AuxilioSaudeRequisicaoService entidadeService;

  @Autowired
  private PessoaJuridicaService pessoaJuridicaService;

  private static String REMOVIDO_SUCESSO = "Arquivo removido com sucesso";

  private ExibeCampoFormAuxilioSaude exibeCampoFormAuxilioSaude;

  private List<PessoaJuridica> comboEmpresasCadastradas;

  private AuxilioSaudeRequisicaoDocumento auxSaudeRequisicaoDoc;

  private AuxilioSaudeRequisicao entidadeParamentro;


  @PostConstruct
  private void init() {
    try {
      if (!isEdicao) {
        inicializar();
      }

      inicializarDadosParaEdicao();
    } catch (Exception e) {
      FacesUtil.addErroMessage("Erro ao carregar os dados. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }
  }

  private void inicializarDadosParaEdicao() {
    entidadeParamentro = new AuxilioSaudeRequisicao();
    AuxilioSaudeRequisicao bean = new AuxilioSaudeRequisicao();

    if (FacesUtil.getFlashParameter("entidade") != null && FacesUtil.getFlashParameter("entidade") instanceof AuxilioSaudeRequisicao) {
      bean = entidadeService.getAuxilioSaudePorId((AuxilioSaudeRequisicao) FacesUtil.getFlashParameter("entidade"));
      List<AuxilioSaudeRequisicaoDocumento> documentoList = entidadeService.getListaArquivosPorIdAuxilio(bean);
      bean.setAuxilioSaudeRequisicaoList(new ArrayList<AuxilioSaudeRequisicao>());
      bean.getAuxilioSaudeRequisicaoList().add(bean);
      bean.setAuxilioSaudeRequisicaoDocumentoBeneficiarioList(documentoList);

      setEntidade(bean);

      isEdicao = Boolean.TRUE;
    }
  }

  private void inicializar() throws InstantiationException, IllegalAccessException {
    createNewInstance();
    getEntidade().setUsuario(loginBean.getUsuarioLogado());
    entidadeService.setDadosIniciaisDaEntidadePorCpf(getEntidade(), getEntidade().getUsuario().getCpf());
  }

  @Override
  public void consultar() {
    try {
      entidadeService.setDadosIniciaisDaEntidade(getEntidade());

    } catch (Exception e) {

      FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }
  }

  @Override
  public String editar() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void excluir() {
    // TODO Auto-generated method stub

  }


  @Override
  public void salvar() {

    try {

      if (isEdicao) {

        entidadeService.editar(getEntidade());
      } else {

        entidadeService.executarAntesSalvar(getEntidade(), getEntidade().getObservacao(), getEntidade().getFlAfirmaSerVerdadeiraInformacao());

        if (entidadeService.isOK(getEntidade())) {
          entidadeService.salvar(getEntidade().getAuxilioSaudeRequisicaoList());
        }
      }

      FacesUtil.addInfoMessage("Registro Salvo com sucesso!");

      logger.info("Operação realizada com sucesso");

    } catch (Exception e) {
      FacesUtil.addErroMessage("Ops! Não foi possível salvar a requisição, Por gentileza entre em contato o setor responsável");
      logger.error(e);
    }
  }

  @Override
  public void salvar(boolean finalizar) {
    // TODO Auto-generated method stub

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
  public void adicionar(AuxilioSaudeRequisicao bean, Boolean isBeneficiario) throws InstantiationException, IllegalAccessException {

    checkPessoraJuridicaNull();

    if (isBeneficiario) {
      adicionarDadosBeneficiario(bean, getPessoaJuridica());
    } else {
      adicionarDadosDependente(bean, getPessoaJuridica());
    }

    fazerUploadArquivos(isBeneficiario);

    // createNewInstance();
  }

  private void checkPessoraJuridicaNull() {
    if (getEntidade().isPessoaJuridicaNull()) {
      FacesUtil.addErroMessage("Nome da Empresa do Plano Saúde é obrigatório");
      return;
    }
  }

  public PessoaJuridica getPessoaJuridica() {
    PessoaJuridica pessoaJuridica = entidadeService.getPessoaJuridicaPorId(getEntidade().getPessoaJuridica(), comboEmpresasCadastradas);

    return pessoaJuridica;
  }

  private void adicionarDadosBeneficiario(AuxilioSaudeRequisicao bean, PessoaJuridica pessoaJuridica) {
    // validar os anexos
    if (getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList() == null || getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList().isEmpty()) {
      FacesUtil.addErroMessage("O anexo do beneficiário é obrigatório");
      return;
    }

    AuxilioSaudeRequisicao auxilioSaudeRequisicaoLocal = null;
    
    if (isEdicao) {
      bean.setPessoaJuridica(pessoaJuridica);

      /*
       * if (getEntidade().getAuxilioSaudeRequisicaoList().contains(bean)) { int index =
       * getEntidade().getAuxilioSaudeRequisicaoList().indexOf(bean);
       * getEntidade().getAuxilioSaudeRequisicaoList().set(index, bean); }else {
       */
        auxilioSaudeRequisicaoLocal = new AuxilioSaudeRequisicao(getEntidade().getFuncional(), loginBean.getUsuarioLogado(), pessoaJuridica, null,
                                  bean.getValorGastoPlanoSaude(), getEntidade().getFlAfirmaSerVerdadeiraInformacao());
        
        getEntidade().adicionarDadosRequisicao(auxilioSaudeRequisicaoLocal);
      //}
    } else {
       auxilioSaudeRequisicaoLocal = new AuxilioSaudeRequisicao(getEntidade().getFuncional(), loginBean.getUsuarioLogado(), pessoaJuridica, null,
                                getEntidade().getValorGastoPlanoSaude(), getEntidade().getFlAfirmaSerVerdadeiraInformacao());

      getEntidade().adicionarDadosRequisicao(auxilioSaudeRequisicaoLocal);
    }
    
    
    entidadeParamentro = new AuxilioSaudeRequisicao();
  }

  /***
   * Metodo procura o item da lista de documentos, cria o diretorio se não existir e Faz o upload do
   * arquivo
   * 
   * @param isBeneficiario
   */
  private void fazerUploadArquivos(Boolean isBeneficiario) {

    try {
      if (isBeneficiario) {
        criarDiretorioEfazerUploadArquivoBeneficiarioList();
      } else {
        criarDiretorioEfazerUploadArquivoDependenteList();
      }
    } catch (FileUploadException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  private void criarDiretorioEfazerUploadArquivoBeneficiarioList() throws Exception {
    if (getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList() == null) {
      FacesUtil.addAvisoMessage("Anexo não foi encontrado!");
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


  public void adicionarDadosDependente(AuxilioSaudeRequisicao bean, PessoaJuridica pessoaJuridica) {
    checkListaNullOrVazia();

    /*** adicionar os dependentes na lista */
    getEntidade().adicionarDadosDependente(getAuxilioSaudeRequisicaoDependente(bean));
  }

  public AuxilioSaudeRequisicaoDependente getAuxilioSaudeRequisicaoDependente(AuxilioSaudeRequisicao bean) {
    AuxilioSaudeRequisicaoDependente beanDependente = new AuxilioSaudeRequisicaoDependente(getEntidade(), getDependenteSelecionado(), getPessoaJuridica(), bean.getValorGastoPlanoSaude());

    return beanDependente;
  }

  private void checkListaNullOrVazia() {
    if (getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList() == null || getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList().isEmpty()) {
      FacesUtil.addErroMessage("O anexo do dependente é obrigatório");

      return;
    }

  }


  /***
   * Metodo procura o dependente que usuario selecionou na lista
   * 
   * @return depentende encontrado
   */
  private Dependente getDependenteSelecionado() {
    Dependente dependenteSelecionado = entidadeService.getDependentePorId(getEntidade().getDependenteSelecionado(), getEntidade().getDependentesComboList());
    return dependenteSelecionado;
  }


  public List<PessoaJuridica> getComboEmpresasCadastradas() {

    try {

      if (isListaEmpresaJuridicaNull())
        this.comboEmpresasCadastradas = pessoaJuridicaService.findAllByTipo(EmpresaAreaSaude.SIM);

    } catch (Exception e) {
      FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de publicação. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }

    return this.comboEmpresasCadastradas;
  }

  /***
   * Verificar se lista de empresa é nula return true se a lista for nula e false caso contrario
   */
  private Boolean isListaEmpresaJuridicaNull() {
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

      @SuppressWarnings("unused")
      UploadedFile comprovante = event.getUploadedFile();

      novaInstanciaAuxDocumento(comprovante, ArquivoVO.NOME_ARQUIVO_BENEFICIARIO);

      auxSaudeRequisicaoDoc.adicionarNovoCaminhoArquivo(new Date(), getEntidade().getFuncional().getMatricula());

      getEntidade().adicionarComprovanteBeneficiarioList(auxSaudeRequisicaoDoc);

    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage("Erro na gravação do comprovante.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }

  }

  public void uploadComprovanteDependente(FileUploadEvent event) {
    try {
      @SuppressWarnings("unused")
      UploadedFile comprovante = event.getUploadedFile();

      novaInstanciaAuxDocumento(comprovante, ArquivoVO.NOME_ARQUIVO_DEPENDENTE);

      auxSaudeRequisicaoDoc.adicionarNovoCaminhoArquivo(new Date(), getEntidade().getFuncional().getMatricula());

      auxSaudeRequisicaoDoc.adicionarDependente(getEntidade());

      getEntidade().adicionarComprovanteDependenteList(auxSaudeRequisicaoDoc);

    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage("Erro na gravação do comprovante.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }

  }

  public void novaInstanciaAuxDocumento(UploadedFile comprovante, String nomeArquivo) {
    auxSaudeRequisicaoDoc = new AuxilioSaudeRequisicaoDocumento(getEntidade(), nomeArquivo, comprovante.getName(), new Date(), comprovante.getData());
  }

  public void deletarAnexo(AuxilioSaudeRequisicaoDocumento bean, Boolean isBeneficiario) {
    if (isBeneficiario) {
      getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList().remove(bean);

      FacesUtil.addInfoMessage(REMOVIDO_SUCESSO);
    } else {
      getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList().remove(bean);
      FacesUtil.addInfoMessage(REMOVIDO_SUCESSO);
    }

  }


  // deletar os registro
  public void deletar(AuxilioSaudeRequisicao bean) {
    getEntidade().getAuxilioSaudeRequisicaoList().remove(bean);

    FacesUtil.addInfoMessage(REMOVIDO_SUCESSO);
  }

  // deletar os registro
  public void deletarDependente(AuxilioSaudeRequisicaoDependente bean) {
    getEntidade().getAuxilioSaudeRequisicaoDependenteList().remove(bean);

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
      String comprovante = null;

      for (AuxilioSaudeRequisicaoDocumento auxDoc : getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
        if (auxDoc.getId() != null) {
          comprovante = auxDoc.getCaminhoCompleto();
          FileUtils.visualizar(comprovante, relatorioUtil);
        }
        if (auxDoc.getAuxilioSaudeRequisicao().equals(bean)) {
          comprovante = auxSaudeRequisicaoDoc.getCaminhoCompleto();
          FileUtils.visualizar(comprovante, relatorioUtil);
          break;
        }
      }
    } catch (FileNotFoundException e) {
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage(e.getMessage());
      logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
    } catch (Exception e) {
      FacesUtil.addErroMessage("Ocorreu algum erro na geração do arquivo. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
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
          String comprovante = auxDoc.getCaminhoCompleto();
          FileUtils.visualizar(comprovante, relatorioUtil);
          break;
        }
      }


    } catch (FileNotFoundException e) {
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage(e.getMessage());
      logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
    } catch (Exception e) {
      FacesUtil.addErroMessage("Ocorreu algum erro na geração do arquivo. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }
  }

  public void exibirCamposFormChange(Boolean isBeneficiario) {
    exibeCampoFormAuxilioSaude = new ExibeCampoFormAuxilioSaude();

    if (isBeneficiario)
      exibeCampoFormAuxilioSaude.setExibirCamposInputFormBeneficiario(isBeneficiario);
    /*
     * else { exibeCampoFormAuxilioSaude.setExibirCamposInputFormDependente(!isBeneficiario); }
     */
  }

  public ExibeCampoFormAuxilioSaude getExibeCampoFormAuxilioSaude() {
    return exibeCampoFormAuxilioSaude;
  }

  public AfastamentoFormBean getAfastamentoFormBean() {
    return afastamentoFormBean;
  }

  public void aprovar(Boolean deferido) {
    if (deferido) {
      System.out.println("Deferido " + deferido);
    }
  }


  public AuxilioSaudeRequisicao getEntidadeParamentro() {
    return entidadeParamentro;
  }

  public void setEntidadeParamentro(AuxilioSaudeRequisicao entidadeParamentro) {
    this.entidadeParamentro = entidadeParamentro;
  }

}
