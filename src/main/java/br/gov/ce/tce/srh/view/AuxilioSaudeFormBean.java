package br.gov.ce.tce.srh.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.richfaces.model.UploadedFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDocumento;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.enums.EmpresaAreaSaude;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.AuxilioSaudeRequisicaoService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.ParametroService;
import br.gov.ce.tce.srh.service.PessoaJuridicaService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;


@Component("auxilioSaudeFormBean")
@Scope("view")
public class AuxilioSaudeFormBean extends ControllerViewBase<AuxilioSaudeRequisicao> implements Serializable {

  private static final long serialVersionUID = 3707425815443102633L;

  static Logger logger = Logger.getLogger(AuxilioSaudeFormBean.class);

  @Autowired
  private LoginBean loginBean;


  @Autowired
  private AfastamentoFormBean afastamentoFormBean;

  public AfastamentoFormBean getAfastamentoFormBean() {
    return afastamentoFormBean;
  }

  private UploadedFile comprovante;

  private Integer contadorAnexoBeneficiario, contatorAnexoDependente=1;
  
  private static String REMOVIDO_SUCESSO = "Arquivo removido com sucesso";

  private List<PessoaJuridica> comboEmpresasCadastradas;

  @Autowired
  FuncionalService funcionalService;

  @Autowired
  AuthenticationService authenticationService;

  @Autowired
  AuxilioSaudeRequisicaoService entidadeService;

  @Autowired
  private PessoaJuridicaService pessoaJuridicaService;


  @Autowired
  private ParametroService parametroService;


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

    AuxilioSaudeRequisicao auxilioSaudeRequisicaoLocal = new AuxilioSaudeRequisicao(getEntidade().getFuncional(),
                              loginBean.getUsuarioLogado(), pessoaJuridica, getEntidade().getValorGastoPlanoSaude(),
                              getEntidade().getFlAfirmaSerVerdadeiraInformacao());

    if (isBeneficiario) {
      getEntidade().adicionarDadosRequisicao(auxilioSaudeRequisicaoLocal);
    } else {
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
      String caminho = SRHUtils.getDadosParametroProperties("arquivo.servidorarquivosrh.comprovanteAuxilioSaude");


      String nome = "COMPROVANTE_BENEFICIARIO" + "_" + contadorAnexoBeneficiario;
      String descricacao = comprovante.getName();
      String caminhoCompletoArquivo = caminho + nome;
     

      AuxilioSaudeRequisicaoDocumento auxSaudeRequisicaoDoc = new AuxilioSaudeRequisicaoDocumento(getEntidade(), null,
                                nome, caminhoCompletoArquivo,  descricacao,  new Date(), comprovante.getData());
      
      getEntidade().adicionarComprovanteBeneficiarioList(auxSaudeRequisicaoDoc);

      contadorAnexoBeneficiario++;

    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage("Erro na gravação do comprovante.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }
  }

  public void uploadComprovanteDependente(FileUploadEvent event) {

    @SuppressWarnings("unused")
    UploadedFile comprovante = event.getUploadedFile();

    // pegando o caminho do arquivo no servidor
    String caminho = SRHUtils.getDadosParametroProperties("arquivo.servidorarquivosrh.comprovanteAuxilioSaude");

    String nome = "COMPROVANTE_DEPENDENTE"+ "_"+contatorAnexoDependente ;
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
  public void visualizar(AuxilioSaudeRequisicaoDocumento bean, String isBeneficiario) {
    System.out.println("VISUALIZAR");
  }
}
