package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import br.gov.ce.tce.srh.domain.DependenteEsocial;
import br.gov.ce.tce.srh.domain.Desligamento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.enums.NaturezaRubricaFolhaPagamento;
import br.gov.ce.tce.srh.enums.TipoInscricao;
import br.gov.ce.tce.srh.enums.TipoMotivoDesligamento;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.DependenteEsocialTCEService;
import br.gov.ce.tce.srh.service.DesligamentoEsocialService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("desligamentoFormBean")
@Scope("view")
public class DesligamentoFormBean implements Serializable {

  static Logger logger = Logger.getLogger(DesligamentoFormBean.class);

  @Autowired
  private DesligamentoEsocialService desligamentoEsocialService;
  @Autowired
  private DependenteEsocialTCEService dependenteEsocialTCEService;

  @Autowired
  private FuncionalService funcionalService;
  @Autowired
  private RepresentacaoFuncionalService representacaoFuncionalService;


  // entidades das telas
  private List<Funcional> servidorEnvioList;
  private Funcional servidorFuncional;
  private Desligamento entidade = new Desligamento();
  private Desligamento admissaoAnterior = new Desligamento();  
  private List<TipoMotivoDesligamento> tipoMotivoDesligamentoList;  
  private List<TipoInscricao> tipoInscricaoList; 
  private List<NaturezaRubricaFolhaPagamento> naturezaRubricaFolhaPagamentoList;

  
  boolean emEdicao = false;


  // paginação
  private UIDataTable dataTable = new UIDataTable();
  private List<Desligamento> pagedList = new ArrayList<Desligamento>();

  @PostConstruct
  private void init() {
    Desligamento flashParameter = (Desligamento) FacesUtil.getFlashParameter("entidade");
    setEntidade(flashParameter != null ? flashParameter : new Desligamento());
    this.servidorEnvioList = funcionalService.findServidoresEvento2299();

    if (getEntidade() != null && getEntidade().getFuncional() != null) {
      admissaoAnterior = getEntidade();
      servidorFuncional = getEntidade().getFuncional();
      consultar();
      emEdicao = true;
    }

  }

  public void consultar() {
    if (servidorFuncional != null) {
      try {

        entidade =  desligamentoEsocialService.getEventoS2299ByServidor(servidorFuncional);
        
        if( getEntidade().getNumeroInscricaoEmpregador() == null) {
          getEntidade().setNumeroInscricaoEmpregador("09499757");
        }
        
      } catch (Exception e) {
        e.printStackTrace();
        FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
        logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
      }
    } else {
      FacesUtil.addErroMessage("Selecione um servidor.");
    }
  }

  @Transactional
  public void salvarEvento() {

    try {
      if (servidorFuncional != null) {
        if (emEdicao) {
          /*
           * if(admissaoAnterior != null) { List<DependenteEsocial> dependentesListExcluir =
           * dependenteEsocialTCEService.findDependenteEsocialByIdfuncional(admissaoAnterior.getFuncional().
           * getId()); if(dependentesListExcluir != null && !dependentesListExcluir.isEmpty()) {
           * dependenteEsocialTCEService.excluirAll(dependentesListExcluir); }
           * desligamentoEsocialService.excluir(admissaoAnterior); }
           */

        }
        desligamentoEsocialService.salvar(entidade);

      }
      // setEntidade( new Admissao() );

      FacesUtil.addInfoMessage("Operação realizada com sucesso.");
      logger.info("Operação realizada com sucesso.");

    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage(e.getMessage());
      logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }
  }

  public String editar() {
    consultar();
    FacesUtil.setFlashParameter("entidade", getEntidade());
    return "incluirAlterar";
  }


  public Desligamento getEntidade() {
    return entidade;
  }

  public void setEntidade(Desligamento entidade) {
    this.entidade = entidade;
  }

  public List<Funcional> getServidorEnvioList() {
    return servidorEnvioList;
  }

  public void setServidorEnvioList(List<Funcional> servidorEnvioList) {
    this.servidorEnvioList = servidorEnvioList;
  }

  public Funcional getServidorFuncional() {
    return servidorFuncional;
  }

  public void setServidorFuncional(Funcional servidorFuncional) {
    this.servidorFuncional = servidorFuncional;
  }

  public boolean isEmEdicao() {
    return emEdicao;
  }

  public void setEmEdicao(boolean emEdicao) {
    this.emEdicao = emEdicao;
  }

  public UIDataTable getDataTable() {
    return dataTable;
  }

  public void setDataTable(UIDataTable dataTable) {
    this.dataTable = dataTable;
  }


  public List<Desligamento> getPagedList() {
    return pagedList;
  }

  public void setPagedList(List<Desligamento> pagedList) {
    this.pagedList = pagedList;
  }

  public List<TipoMotivoDesligamento> getTipoMotivoDesligamentoList() {
    return Arrays.asList(TipoMotivoDesligamento.values());    
  }

  public List<TipoInscricao> getTipoInscricaoList() {
    return Arrays.asList(TipoInscricao.values());    
  }

  public List<NaturezaRubricaFolhaPagamento> getNaturezaRubricaFolhaPagamentoList() {
    return Arrays.asList(NaturezaRubricaFolhaPagamento.values());
  }
}
