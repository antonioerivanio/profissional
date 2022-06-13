package br.gov.ce.tce.srh.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.richfaces.event.FileUploadEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicao;
import br.gov.ce.tce.srh.domain.AuxilioSaudeRequisicaoDependente;
import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.enums.EmpresaAreaSaude;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Entidade;
import br.gov.ce.tce.srh.sca.domain.Usuario;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.AfastamentoESocialService;
import br.gov.ce.tce.srh.service.AuxilioSaudeRequisicaoService;
import br.gov.ce.tce.srh.service.DependenteService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.PessoaJuridicaService;
import br.gov.ce.tce.srh.util.FacesUtil;


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
  
  private List<PessoaJuridica> comboEmpresasCadastradas;

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

      entidadeService.executarAntesSalvar(getEntidade(), getEntidade().getObservacao(), getEntidade().getFlAfirmaSerVerdadeiraInformacao());
      
      if (entidadeService.isOK(getEntidade())) {
       
        entidadeService.salvarAll(getEntidade().getAuxilioSaudeRequisicaoList());

        FacesUtil.addInfoMessage("Registro Salvo com sucesso!");

        logger.info("Operação realizada com sucesso.");

        //createNewInstance();
      }
    } catch (InstantiationException | IllegalAccessException e) {
      logger.error(e);
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

    try {

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

    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

  }

  public void adicionarDadosDependente(AuxilioSaudeRequisicao bean) {

    try {

      Dependente dependente = entidadeService.getDependentePorId(getEntidade().getDependenteSelecionado(),
                                getEntidade().getDependentesComboList());
      getEntidade().setDependenteSelecionado(dependente);

      AuxilioSaudeRequisicaoDependente beanDependente = new AuxilioSaudeRequisicaoDependente(getEntidade(), dependente,
                                bean.getPessoaJuridica(), bean.getValorGastoPlanoSaude());

      /*** adicionar os dependentes na lista */
      getEntidade().adicionarDadosDependente(beanDependente);

    } catch (InstantiationException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

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
   * @param event
   */
  public void uploadComprovante(FileUploadEvent event) {

    try {

        //pegando o caminho do arquivo no servidor
        //Parametro parametro = parametroService.getByNome("pathComprovanteFolgaSRH");

        //if (parametro == null) {
          //  throw new SRHRuntimeException(
            //        "Parâmetro do caminho do comprovante não encontrado na tabela SRH.TB_PARAMETRO");               
       // }

        //setComprovante(event.getUploadedFile());
        
        // gravando em disco
       // File file = new File(parametro.getValor() + comprovante.getName());
        FileOutputStream fop;
        
        // setando o nome do comprovante
       // getEntidade().setCaminhoComprovante(file.getName());

        //fop = new FileOutputStream(file);
        fop = new FileOutputStream(new File(""));
        //fop.write(comprovante.getData());
        fop.flush();
        fop.close();

    } catch (SRHRuntimeException e) {
        FacesUtil.addErroMessage("Erro na gravação do comprovante de folga.");
        logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    } catch (FileNotFoundException e) {
        FacesUtil.addErroMessage("Erro na gravação do comprovante de folga.");
        logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    } catch (IOException e) {
        FacesUtil.addErroMessage("Erro na gravação do comprovante de folga.");
        logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }

}
  
}
