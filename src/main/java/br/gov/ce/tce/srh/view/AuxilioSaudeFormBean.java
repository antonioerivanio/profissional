package br.gov.ce.tce.srh.view;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.commons.io.IOUtils;
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
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.enums.EmpresaAreaSaude;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.AuxilioSaudeRequisicaoService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.ParametroService;
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

  private boolean isExibidoBotaoAdicionar;

  @Autowired
  private RelatorioUtil relatorioUtil;

  @Autowired
  private AfastamentoFormBean afastamentoFormBean;

  public AfastamentoFormBean getAfastamentoFormBean() {
    return afastamentoFormBean;
  }


  private Integer contadorAnexoBeneficiario = 1;
  private Integer contatorAnexoDependente = 1;

  private static String REMOVIDO_SUCESSO = "Arquivo removido com sucesso";

  private List<PessoaJuridica> comboEmpresasCadastradas;

  private AuxilioSaudeRequisicaoDocumento auxSaudeRequisicaoDoc;

  @Autowired
  FuncionalService funcionalService;

  @Autowired
  AuthenticationService authenticationService;

  @Autowired
  AuxilioSaudeRequisicaoService entidadeService;

  @Autowired
  private PessoaJuridicaService pessoaJuridicaService;


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

        logger.info("Operação realizada com sucesso.");
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
  public void adicionarDadosBeneficiario(AuxilioSaudeRequisicao bean, Boolean isBeneficiario) {

    PessoaJuridica pessoaJuridica = entidadeService.getPessoaJuridicaPorId(getEntidade().getPessoaJuridica(),
                              comboEmpresasCadastradas);

    String statusFuncional = getEntidade().getFuncional() != null && getEntidade().getFuncional().getStatus() != null
                              && getEntidade().getFuncional().getStatus() == 1 ? AuxilioSaudeRequisicao.ATIVO
                                                        : AuxilioSaudeRequisicao.INATIVO;


    AuxilioSaudeRequisicao auxilioSaudeRequisicaoLocal = new AuxilioSaudeRequisicao(getEntidade().getFuncional(),
                              loginBean.getUsuarioLogado(), pessoaJuridica, getEntidade().getValorGastoPlanoSaude(),
                              getEntidade().getFlAfirmaSerVerdadeiraInformacao(), statusFuncional);


    if (isBeneficiario) {
      auxilioSaudeRequisicaoLocal.setauxilioSaudeRequisicaoDocumentoBeneficiarioList(
                                getEntidade().getAuxilioSaudeRequisicaoDocumentoBeneficiarioList());
      getEntidade().adicionarDadosRequisicao(auxilioSaudeRequisicaoLocal);
    } else {
      auxilioSaudeRequisicaoLocal.setAuxilioSaudeRequisicaoDocumentoDependenteList(
                                getEntidade().getAuxilioSaudeRequisicaoDocumentoDependenteList());
      adicionarDadosDependente(auxilioSaudeRequisicaoLocal);
    }

    bean = new AuxilioSaudeRequisicao();

  }

  public void adicionarDadosDependente(AuxilioSaudeRequisicao bean) {

    Dependente dependente = entidadeService.getDependentePorId(getEntidade().getDependenteSelecionado(),
                              getEntidade().getDependentesComboList());
    getEntidade().setDependenteSelecionado(dependente);

    AuxilioSaudeRequisicaoDependente beanDependente = new AuxilioSaudeRequisicaoDependente(getEntidade(), dependente,
                              bean.getPessoaJuridica(), bean.getValorGastoPlanoSaude());

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

      String nome = "COMPROVANTE_BENEFICIARIO" + "_" + contadorAnexoBeneficiario;
      String descricacao = comprovante.getName();
      String caminhoCompleto = caminhoArquivo + File.separator + nome;

      auxSaudeRequisicaoDoc = new AuxilioSaudeRequisicaoDocumento(getEntidade(), null, nome, caminhoArquivo,
                                descricacao, new Date(), comprovante.getData());

      getEntidade().adicionarComprovanteBeneficiarioList(auxSaudeRequisicaoDoc);

      contadorAnexoBeneficiario++;

      auxSaudeRequisicaoDoc.setCaminhoTemporario(caminhoCompleto + ".pdf");

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
  }

  public void uploadComprovanteDependente(FileUploadEvent event) {

    @SuppressWarnings("unused")
    UploadedFile comprovante = event.getUploadedFile();

    // pegando o caminho do arquivo no servidor
    String caminho = SRHUtils.getDadosParametroProperties("arquivo.servidorarquivosrh.comprovanteAuxilioSaude");

    String nome = "COMPROVANTE_DEPENDENTE" + "_" + contatorAnexoDependente;
    String descricacao = comprovante.getName();
    String caminhoCompletoArquivo = caminho + nome;


    AuxilioSaudeRequisicaoDocumento auxSaudeRequisicaoDoc = new AuxilioSaudeRequisicaoDocumento(getEntidade(), null,
                              nome, caminhoCompletoArquivo, comprovante.getName(), new Date(), comprovante.getData());

    getEntidade().adicionarComprovanteDependenteList(auxSaudeRequisicaoDoc);

    contatorAnexoDependente++;
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
  public void deletar(AuxilioSaudeRequisicao bean, String isBeneficiario) {
    if (Boolean.parseBoolean(isBeneficiario)) {
      getEntidade().getAuxilioSaudeRequisicaoList().remove(bean);
      FacesUtil.addInfoMessage(REMOVIDO_SUCESSO);
    } else {
      getEntidade().getAuxilioSaudeRequisicaoDependenteList().remove(bean);
      FacesUtil.addInfoMessage(REMOVIDO_SUCESSO);
    }

  }

  // deletar os registro
  public void visualizar(AuxilioSaudeRequisicao bean, String isBeneficiario) {
    try {
      String comprovante = auxSaudeRequisicaoDoc.getCaminhoArquivo() + auxSaudeRequisicaoDoc.getNomeArquivo() + ".pdf";
      
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

  public void exibirBotaoAdicionarChange() {
    isExibidoBotaoAdicionar = Boolean.TRUE;
  }


  public boolean getIsExibidoBotaoAdicionar() {
    return isExibidoBotaoAdicionar;
  }


  public void setExibidoBotaoAdicionar(boolean isExibidoBotaoAdicionar) {
    this.isExibidoBotaoAdicionar = isExibidoBotaoAdicionar;
  }
  
  


}
