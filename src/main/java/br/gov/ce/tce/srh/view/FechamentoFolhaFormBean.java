package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.FechamentoFolhaEsocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.DependenteEsocialTCEService;
import br.gov.ce.tce.srh.service.FechamentoFolhaEsocialService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("fechamentoFolhaForm")
@Scope("view")
public class FechamentoFolhaFormBean implements Serializable {

  static Logger logger = Logger.getLogger(FechamentoFolhaFormBean.class);

  @Autowired
  private FechamentoFolhaEsocialService fechamentoEventoEsocialService;
  @Autowired
  private DependenteEsocialTCEService dependenteEsocialTCEService;

  @Autowired
  private FuncionalService funcionalService;


  // entidades das telas
  private List<Funcional> servidorEnvioList;
  private List<Integer> comboAno;
  private Funcional servidorFuncional;
  private FechamentoFolhaEsocial entidade = new FechamentoFolhaEsocial();

  boolean emEdicao = false;


  // paginação
  private UIDataTable dataTable = new UIDataTable();
  private List<FechamentoFolhaEsocial> pagedList = new ArrayList<FechamentoFolhaEsocial>();

  @PostConstruct
  private void init() {
    FechamentoFolhaEsocial flashParameter = (FechamentoFolhaEsocial) FacesUtil.getFlashParameter("entidade");
    setEntidade(flashParameter != null ? flashParameter : fechamentoEventoEsocialService.getIncializarEventoS1299ByServidor());

    // anoReferencia = referencia[0];
    // mesReferencia = referencia[1];

    // this.servidorEnvioList = funcionalService.findFechamentoFolhaEvento1299();

    /*
     * if (getEntidade() != null && getEntidade().getFuncional() != null) { servidorFuncional =
     * getEntidade().getFuncional(); emEdicao = true; }
     */

    consultar();
  }

  public void consultar() {
    if (servidorFuncional != null) {
      try {
        if (emEdicao) {
          entidade = fechamentoEventoEsocialService.getEventoS1299ByServidor(servidorFuncional);
        } else {

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

  public void salvar() {

    try {

      fechamentoEventoEsocialService.salvar(entidade);

      FacesUtil.addInfoMessage("Operação realizada com sucesso.");
      logger.info("Operação realizada com sucesso.");

    } catch (NullPointerException e) {
      FacesUtil.addErroMessage(e.getMessage());
      logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
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


  public FechamentoFolhaEsocial getEntidade() {
    return entidade;
  }

  public void setEntidade(FechamentoFolhaEsocial entidade) {
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


  public List<Integer> getComboAno() {

    try {

      if (this.comboAno == null)
        this.comboAno = SRHUtils.popularComboAno(2);

    } catch (Exception e) {
      FacesUtil.addInfoMessage("Erro ao carregar o combo do Ano. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }

    return this.comboAno;
  }

  public void setComboAno(List<Integer> comboAno) {
    this.comboAno = comboAno;
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


  public List<FechamentoFolhaEsocial> getPagedList() {
    return pagedList;
  }

  public void setPagedList(List<FechamentoFolhaEsocial> pagedList) {
    this.pagedList = pagedList;
  }
  // FIM PAGINAÇÃO

}
