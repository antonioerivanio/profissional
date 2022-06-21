package br.gov.ce.tce.srh.view;

import java.io.FileNotFoundException;
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


    if (isBeneficiario) {
      adicionarDadosBeneficiario(bean, pessoaJuridica);
    } else {
      adicionarDadosDependente(bean, pessoaJuridica);
    }

    fazerUploadArquivos(isBeneficiario);
  }

  private void adicionarDadosBeneficiario(AuxilioSaudeRequisicao bean, PessoaJuridica pessoaJuridica) {
    // validar os anexos
    if (getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList() == null
                              || getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList().isEmpty()) {
      FacesUtil.addErroMessage("O anexo do beneficiário é obrigatório");

      return;
    }


    AuxilioSaudeRequisicao auxilioSaudeRequisicaoLocal =
                              new AuxilioSaudeRequisicao(getEntidade().getFuncional(), loginBean.getUsuarioLogado(),
                                                        pessoaJuridica, null, getEntidade().getValorGastoPlanoSaude(),
                                                        getEntidade().getFlAfirmaSerVerdadeiraInformacao());

    getEntidade().adicionarDadosRequisicao(auxilioSaudeRequisicaoLocal);
  }

  private void fazerUploadArquivos(Boolean isBeneficiario) {

    try {
      if (isBeneficiario) {
        for (AuxilioSaudeRequisicaoDocumento doc : getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
          FileUtils.criarDiretorio(doc.getCaminhoArquivo());
          FileUtils.upload(doc.getCaminhoCompleto(), doc.getArquivoVO().getComprovante());
        }
      } else {
        for (AuxilioSaudeRequisicaoDocumento doc : getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList()) {
          FileUtils.criarDiretorio(doc.getCaminhoArquivo());
          FileUtils.upload(doc.getCaminhoCompleto(), doc.getArquivoVO().getComprovante());
        }
      }
    } catch (FileUploadException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }


  public void adicionarDadosDependente(AuxilioSaudeRequisicao bean, PessoaJuridica pessoaJuridica) {
    if (getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList() == null
                              || getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList().isEmpty()) {
      FacesUtil.addErroMessage("O anexo do dependente é obrigatório");

      return;
    }


    Dependente dependenteSelecionado = entidadeService.getDependentePorId(getEntidade().getDependenteSelecionado(),
                              getEntidade().getDependentesComboList());

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

      auxSaudeRequisicaoDoc = new AuxilioSaudeRequisicaoDocumento(getEntidade(), ArquivoVO.NOME_ARQUIVO_BENEFICIARIO,
                                comprovante.getName(), new Date(), comprovante.getData());

      auxSaudeRequisicaoDoc.adicionarCaminho(new Date(),
                                getEntidade().getFuncional().getMatricula());
      
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

      auxSaudeRequisicaoDoc = new AuxilioSaudeRequisicaoDocumento(getEntidade(), ArquivoVO.NOME_ARQUIVO_DEPENDENTE,
                                comprovante.getName(), new Date(), comprovante.getData());

      auxSaudeRequisicaoDoc.adicionarCaminho(new Date(),
                                getEntidade().getFuncional().getMatricula());
      
      auxSaudeRequisicaoDoc.adicionarDependente(getEntidade());

      getEntidade().adicionarComprovanteDependenteList(auxSaudeRequisicaoDoc);

    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage("Erro na gravação do comprovante.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
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
      for (AuxilioSaudeRequisicaoDocumento auxDoc : getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList()) {
        if (auxDoc.getAuxilioSaudeRequisicao().equals(bean)) {
          String comprovante = auxSaudeRequisicaoDoc.getCaminhoCompleto();
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
   * Visualizar o anexo
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
