package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.hibernate.exception.ConstraintViolationException;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.TerminoVinculo;
import br.gov.ce.tce.srh.enums.NaturezaRubricaFolhaPagamento;
import br.gov.ce.tce.srh.enums.TipoConstraintException;
import br.gov.ce.tce.srh.enums.TipoInscricao;
import br.gov.ce.tce.srh.enums.TipoMotivoDesligamento;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.DependenteEsocialTCEService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.service.TerminoVinculoEsocialService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("terminoVinculoFormBean")
@Scope("view")
public class TerminoVinculoFormBean implements Serializable {

  static Logger logger = Logger.getLogger(TerminoVinculoFormBean.class);

  @Autowired
  private TerminoVinculoEsocialService terminoVinculoEsocialService;
  @Autowired
  private DependenteEsocialTCEService dependenteEsocialTCEService;

  @Autowired
  private FuncionalService funcionalService;
  @Autowired
  private RepresentacaoFuncionalService representacaoFuncionalService;


  // entidades das telas
  private List<Funcional> servidorEnvioList;
  private Funcional servidorFuncional;
  private TerminoVinculo entidade = new TerminoVinculo();
  private TerminoVinculo admissaoAnterior = new TerminoVinculo();  
  private List<TipoMotivoDesligamento> tipoMotivoDesligamentoList;  
  private List<TipoInscricao> tipoInscricaoList; 
  private List<NaturezaRubricaFolhaPagamento> naturezaRubricaFolhaPagamentoList;

  
  boolean emEdicao = false;


  // paginação
  private UIDataTable dataTable = new UIDataTable();
  private List<TerminoVinculo> pagedList = new ArrayList<TerminoVinculo>();

  @PostConstruct
  private void init() {
    TerminoVinculo flashParameter = (TerminoVinculo) FacesUtil.getFlashParameter("entidade");
    setEntidade(flashParameter != null ? flashParameter : new TerminoVinculo());
    this.servidorEnvioList = funcionalService.findServidoresEvento2299();

    if (getEntidade() != null && getEntidade().getFuncional() != null) {     
      servidorFuncional = getEntidade().getFuncional();
      emEdicao = true;
      consultar();
      
    }

  }

  public void consultar() {
    if (servidorFuncional != null) {
      try {

        entidade =  terminoVinculoEsocialService.getEventoS2399ByServidor(servidorFuncional);        
      } catch (Exception e) {
        e.printStackTrace();
        FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
        logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
      }
    } else {
      FacesUtil.addErroMessage("Selecione um servidor.");
    }    
    
  }
  
  public void salvarEvento() {

    try {
      if (servidorFuncional != null) {
     
        terminoVinculoEsocialService.salvar(entidade);
        
        entidade = new TerminoVinculo();
      }    

      FacesUtil.addInfoMessage("Operação realizada com sucesso.");
      logger.info("Operação realizada com sucesso.");

    } catch (SRHRuntimeException e) {
      FacesUtil.addErroMessage(e.getMessage());
      logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
    }  catch (DataIntegrityViolationException e) {
      if (e.getCause() instanceof ConstraintViolationException && e.getCause().getCause().getMessage().contains(TipoConstraintException.CONSTRAINT_UNIQUE_TERMINOVINCULO.getNome())) {
        FacesUtil.addErroMessage(TipoConstraintException.CONSTRAINT_UNIQUE_TERMINOVINCULO.getMensageError());
        logger.fatal("Ocorreu o seguinte erro: " + e.getCause().getCause().getMessage());
      }

    }
    catch (Exception e) {
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


  public TerminoVinculo getEntidade() {
    return entidade;
  }

  public void setEntidade(TerminoVinculo entidade) {
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


  public List<TerminoVinculo> getPagedList() {
    return pagedList;
  }

  public void setPagedList(List<TerminoVinculo> pagedList) {
    this.pagedList = pagedList;
  }

  public List<TipoMotivoDesligamento> getTipoMotivoTerminoVinculoList() {
    return Arrays.asList(TipoMotivoDesligamento.values());    
  }

  public List<TipoInscricao> getTipoInscricaoList() {
    return Arrays.asList(TipoInscricao.values());    
  }

  public List<NaturezaRubricaFolhaPagamento> getNaturezaRubricaFolhaPagamentoList() {
    return Arrays.asList(NaturezaRubricaFolhaPagamento.values());
  }
}
