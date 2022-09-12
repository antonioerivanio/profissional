package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import br.gov.ce.tce.srh.domain.ReaberturaFolhaEsocial;
import br.gov.ce.tce.srh.service.ReaberturaFolhaEsocialService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("reaberturaFolhaList")
@Scope("view")
public class ReaberturaFolhaListBean implements Serializable {

  static Logger logger = Logger.getLogger(ReaberturaFolhaListBean.class);

  @Autowired
  private ReaberturaFolhaEsocialService reaberturaEventoEsocialService;

  @Autowired
  private RelatorioUtil relatorioUtil;

  // entidades das telas
  private List<ReaberturaFolhaEsocial> lista;
  private ReaberturaFolhaEsocial entidade = new ReaberturaFolhaEsocial();

  // paginação
  private int count;
  private UIDataTable dataTable = new UIDataTable();
  private PagedListDataModel dataModel = new PagedListDataModel();
  private List<ReaberturaFolhaEsocial> pagedList = new ArrayList<ReaberturaFolhaEsocial>();
  private int flagRegistroInicial = 0;
  private String periodoApuracao;

  @PostConstruct
  private void init() {
    ReaberturaFolhaEsocial flashParameter = (ReaberturaFolhaEsocial) FacesUtil.getFlashParameter("entidade");
    setEntidade(flashParameter != null ? flashParameter : new ReaberturaFolhaEsocial());
  }

  public void consultar() {

    try {
      if (this.getEntidade().getMesReferencia() != null && this.getEntidade().getMesReferencia() != "0" && this.getEntidade().getAnoReferencia() != null
                                && !this.getEntidade().getAnoReferencia().isEmpty()) {
        periodoApuracao = this.getEntidade().getMesReferencia() + "-" + this.getEntidade().getMesReferencia();
      }

      count = reaberturaEventoEsocialService.count(periodoApuracao);

      if (count == 0) {
        FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
        logger.info("Nenhum registro foi encontrado.");
      }

      flagRegistroInicial = -1;

    } catch (Exception e) {
      limparListas();
      FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }
  }

  public String editar() {
    FacesUtil.setFlashParameter("entidade", getEntidade());
    return "incluirAlterar";
  }

  public void excluir() {

    try {

      reaberturaEventoEsocialService.excluir(entidade);

      FacesUtil.addInfoMessage("Registro excluído com sucesso.");
      logger.info("Registro excluído com sucesso.");

    } catch (DataAccessException e) {
      FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
      logger.error("Ocorreu o seguinte erro: " + e.getMessage());
    } catch (Exception e) {
      FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }

    setEntidade(new ReaberturaFolhaEsocial());
    consultar();
  }

  public void relatorio() {

    try {

      Map<String, Object> parametros = new HashMap<String, Object>();

      if (this.getEntidade().getAnoReferencia() != null && !this.getEntidade().getAnoReferencia().equalsIgnoreCase("")) {
        String filtro = "where upper( descricao ) like '%" + this.getEntidade().getAnoReferencia().toUpperCase() + "%' ";
        parametros.put("FILTRO", filtro);
      }

      relatorioUtil.relatorio("Reabertura.jasper", parametros, "Reabertura.pdf");

    } catch (Exception e) {
      FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
      logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
    }
  }


  public ReaberturaFolhaEsocial getEntidade() {
    return entidade;
  }

  public void setEntidade(ReaberturaFolhaEsocial entidade) {
    this.entidade = entidade;
  }

  public List<ReaberturaFolhaEsocial> getLista() {
    return lista;
  }

  // PAGINAÇÃO
  private void limparListas() {
    dataTable = new UIDataTable();
    dataModel = new PagedListDataModel();
    pagedList = new ArrayList<ReaberturaFolhaEsocial>();
  }

  public UIDataTable getDataTable() {
    return dataTable;
  }

  public void setDataTable(UIDataTable dataTable) {
    this.dataTable = dataTable;
  }

  public PagedListDataModel getDataModel() {
    if (flagRegistroInicial != getDataTable().getFirst()) {
      flagRegistroInicial = getDataTable().getFirst();

      setPagedList(reaberturaEventoEsocialService.search(periodoApuracao, getDataTable().getFirst(), getDataTable().getRows()));
      if (count != 0) {
        dataModel = new PagedListDataModel(getPagedList(), count);
      } else {
        limparListas();
      }
    }
    return dataModel;
  }

  public List<ReaberturaFolhaEsocial> getPagedList() {
    return pagedList;
  }

  public void setPagedList(List<ReaberturaFolhaEsocial> pagedList) {
    this.pagedList = pagedList;
  }
  // FIM PAGINAÇÃO

}
