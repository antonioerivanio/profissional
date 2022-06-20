package br.gov.ce.tce.srh.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.exception.FileUploadException;
import org.richfaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
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
import br.gov.ce.tce.srh.util.SRHUtils;


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


  private Integer contadorAnexoBeneficiario = 1;
  private Integer contatorAnexoDependente = 1;

  private static String REMOVIDO_SUCESSO = "Arquivo removido com sucesso";

  private ExibeCampoFormAuxilioSaude exibeCampoFormAuxilioSaude;

  private List<PessoaJuridica> comboEmpresasCadastradas;

  private AuxilioSaudeRequisicaoDocumento auxSaudeRequisicaoDoc;


  @PostConstruct
  private void init() {
    try {

      getEntidade().setUsuario(loginBean.getUsuarioLogado());

      entidadeService.setDadosIniciaisDaEntidadePorCpf(getEntidade(), getEntidade().getUsuario().getCpf());

    } catch (Exception e) {
      FacesUtil.addErroMessage("Erro ao carregar os dados. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }
  }


  @Override
  public void consultar() {
    try {

      entidadeService.setDadosIniciaisDaEntidade(getEntidade());

      exibirCamposFormChange(Boolean.TRUE);

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

      entidadeService.executarAntesSalvar(getEntidade(), getEntidade().getObservacao(),
                                getEntidade().getFlAfirmaSerVerdadeiraInformacao());

      if (entidadeService.isOK(getEntidade())) {

        entidadeService.salvarAll(getEntidade().getAuxilioSaudeRequisicaoList());

        FacesUtil.addInfoMessage("Registro Salvo com sucesso!");

        logger.info("Operação realizada com sucesso");
      }

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
   */
  public void adicionar(AuxilioSaudeRequisicao bean, Boolean isBeneficiario) {

    PessoaJuridica pessoaJuridica = entidadeService.getPessoaJuridicaPorId(getEntidade().getPessoaJuridica(),
                              comboEmpresasCadastradas);

    String statusFuncional = getEntidade().getFuncional() != null && getEntidade().getFuncional().getStatus() != null
                              && getEntidade().getFuncional().getStatus() == 1 ? AuxilioSaudeRequisicao.ATIVO
                                                        : AuxilioSaudeRequisicao.INATIVO;

    if (isBeneficiario) {
      adicionarDadosBeneficiario(bean, pessoaJuridica, statusFuncional);
    } else {
      adicionarDadosDependente(bean, pessoaJuridica, statusFuncional);
    }

    bean = new AuxilioSaudeRequisicao();

  }

  private void adicionarDadosBeneficiario(AuxilioSaudeRequisicao bean, PessoaJuridica pessoaJuridica,
                            String statusFuncional) {
    Dependente dependenteNull = null;

    AuxilioSaudeRequisicao auxilioSaudeRequisicaoLocal = new AuxilioSaudeRequisicao(getEntidade().getFuncional(),
                              loginBean.getUsuarioLogado(), pessoaJuridica, dependenteNull,
                              getEntidade().getValorGastoPlanoSaude(),
                              getEntidade().getFlAfirmaSerVerdadeiraInformacao(), statusFuncional);

    // validar os anexos
    if (getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList() == null
                              || getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList().isEmpty()) {
      FacesUtil.addErroMessage("O anexo do beneficiário é obrigatório");

      return;
    }

    auxilioSaudeRequisicaoLocal.setauxilioSaudeRequisicaoDocumentoBeneficiarioList(
                              getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList());

    getEntidade().adicionarDadosRequisicao(auxilioSaudeRequisicaoLocal);
  }


  public void adicionarDadosDependente(AuxilioSaudeRequisicao bean, PessoaJuridica pessoaJuridica,
                            String statusFuncional) {
    if (getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList() == null
                              || getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList().isEmpty()) {
      FacesUtil.addErroMessage("O anexo do dependente é obrigatório");

      return;
    }


    /*
     * auxilioSaudeRequisicaoLocal.setAuxilioSaudeRequisicaoDocumentoDependenteList(
     * getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList());
     */


    Dependente dependenteSelecionado = entidadeService.getDependentePorId(getEntidade().getDependenteSelecionado(),
                              getEntidade().getDependentesComboList());

    /*
     * AuxilioSaudeRequisicao auxilioSaudeRequisicaoLocal = new
     * AuxilioSaudeRequisicao(getEntidade().getFuncional(), loginBean.getUsuarioLogado(),
     * pessoaJuridica, dependenteSelecionado, getEntidade().getValorGastoPlanoSaude(),
     * getEntidade().getFlAfirmaSerVerdadeiraInformacao(), statusFuncional);
     */


    AuxilioSaudeRequisicaoDependente beanDependente = new AuxilioSaudeRequisicaoDependente(getEntidade(),
                              dependenteSelecionado, pessoaJuridica, bean.getValorGastoPlanoSaude());

    /*** adicionar os dependentes na lista */
    getEntidade().adicionarDadosDependente(beanDependente);
  }


  public List<PessoaJuridica> getComboEmpresasCadastradas() {

    try {

      if (this.comboEmpresasCadastradas == null)
        this.comboEmpresasCadastradas = pessoaJuridicaService.findAllByTipo(EmpresaAreaSaude.SIM);

    } catch (Exception e) {
      FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de publicação. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }

    return this.comboEmpresasCadastradas;
  }

  /***
   * Salvar o arquivo que comprove o gasto com auxilio saude
   * 
   * @param event
   * @throws IllegalAccessException
   * @throws InstantiationException
   */


  public void uploadComprovanteBeneficario(FileUploadEvent event) {

    try {

      @SuppressWarnings("unused")
      UploadedFile comprovante = event.getUploadedFile();

      // pegando o caminho do arquivo no servidor
      String caminhoArquivo = SRHUtils
                                .getDadosParametroProperties("arquivo.servidorarquivosrh.comprovanteAuxilioSaude");

      String nomeTemp = "COMPROVANTE_BENEFICIARIO" + "_" + contadorAnexoBeneficiario + ".pdf";
      String descricacao = comprovante.getName();
      String caminhoCompleto = caminhoArquivo + File.separator + nomeTemp;
      AuxilioSaudeRequisicaoDependente dependenteNull = null;

      auxSaudeRequisicaoDoc = new AuxilioSaudeRequisicaoDocumento(getEntidade(), dependenteNull, nomeTemp,
                                caminhoCompleto, descricacao, new Date(), comprovante.getData());

      getEntidade().adicionarComprovanteBeneficiarioList(auxSaudeRequisicaoDoc);

      FileUtils.upload(caminhoCompleto, comprovante.getData());

    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage("Erro na gravação do comprovante.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (FileUploadException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    contadorAnexoBeneficiario++;
  }

  public void uploadComprovanteDependente(FileUploadEvent event) {
    try {
      @SuppressWarnings("unused")
      UploadedFile comprovante = event.getUploadedFile();

      // pegando o caminho do arquivo no servidor
      String caminho = SRHUtils.getDadosParametroProperties("arquivo.servidorarquivosrh.comprovanteAuxilioSaude");

      String nome = "COMPROVANTE_DEPENDENTE" + "_" + contatorAnexoDependente + ".pdf";
      String descricacao = comprovante.getName();
      String caminhoCompletoArquivo = caminho + nome;

      AuxilioSaudeRequisicaoDependente dependente = new AuxilioSaudeRequisicaoDependente(getEntidade(),
                                getEntidade().getDependenteSelecionado(), getEntidade().getPessoaJuridica(),
                                getEntidade().getValorGastoPlanoSaude());

      AuxilioSaudeRequisicaoDocumento auxSaudeRequisicaoDoc = new AuxilioSaudeRequisicaoDocumento(null, dependente,
                                nome, caminhoCompletoArquivo, descricacao, new Date(), comprovante.getData());

      getEntidade().adicionarComprovanteDependenteList(auxSaudeRequisicaoDoc);

      contatorAnexoDependente++;

      FileUtils.upload(caminhoCompletoArquivo, comprovante.getData());

    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage("Erro na gravação do comprovante.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (FileUploadException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


  public void deletarAnexo(AuxilioSaudeRequisicaoDocumento bean, String isBeneficiario) {
    if (Boolean.parseBoolean(isBeneficiario)) {
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
   * Visualizar o anexo
   * 
   * @param bean
   * @param isBeneficiario
   */
  public void visualizarAnexoBeneficiario(AuxilioSaudeRequisicao bean) {

    try {
      String comprovante = auxSaudeRequisicaoDoc.getCaminhoArquivo();

      FileUtils.visualizar(comprovante, relatorioUtil);
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
   * Visualizar o anexo
   * 
   * @param bean
   * @param isBeneficiario
   */
  public void visualizarAnexoDependente(AuxilioSaudeRequisicaoDependente bean) {
    try {
      for (AuxilioSaudeRequisicaoDocumento auxDoc : getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList()) {
        if (auxDoc.getAuxilioSaudeRequisicaoDependente().equals(bean)) {
          String comprovante = auxDoc.getCaminhoArquivo();
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
    else {
      exibeCampoFormAuxilioSaude.setExibirCamposInputFormDependente(!isBeneficiario);
    }
  }


  public ExibeCampoFormAuxilioSaude getExibeCampoFormAuxilioSaude() {
    return exibeCampoFormAuxilioSaude;
  }


  public AfastamentoFormBean getAfastamentoFormBean() {
    return afastamentoFormBean;
  }


}
