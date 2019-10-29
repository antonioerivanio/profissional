package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.OcupacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("cargoListBean")
@Scope("view")
public class CargoListBean implements Serializable {

	static Logger logger = Logger.getLogger(CargoListBean.class);

	@Autowired
	private OcupacaoService ocupacaoService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;


	// parametros da tela de consulta
	private Long situacao;
	private String nomenclatura = new String();

	// entidades das telas
	private List<Ocupacao> lista;
	private Ocupacao entidade = new Ocupacao();
	
	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Ocupacao> pagedList = new ArrayList<Ocupacao>();
	private int flagRegistroInicial = 0;


	public void consultar() {

		try {

			if ( this.situacao != null && this.situacao != 0l ) {
				count = ocupacaoService.count(nomenclatura, situacao);
			} else {
				count = ocupacaoService.count(nomenclatura);
			}

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

			ocupacaoService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		entidade = new Ocupacao();
		consultar();
	}

	public void relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			StringBuffer filtro = new StringBuffer();
			filtro.append(" WHERE 1=1 ");

			if ( this.nomenclatura != null && !this.nomenclatura.equalsIgnoreCase("") )
				filtro.append(" AND upper( nomenclatura ) LIKE upper( '%" + this.nomenclatura + "%' ) ");
			if ( this.situacao != null && this.situacao != 0)
				filtro.append(" AND situacao = " + this.situacao );

			parametros.put("FILTRO", filtro.toString());

			relatorioUtil.relatorio("cargo.jasper", parametros, "cargo.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public Long getSituacao() {return situacao;}
	public void setSituacao(Long situacao) {this.situacao = situacao;}
	
	public String getNomenclatura() {return nomenclatura;}
	public void setNomenclatura(String nomenclatura) {this.nomenclatura = nomenclatura;}

	public Ocupacao getEntidade() {return entidade;}
	public void setEntidade(Ocupacao entidade) {this.entidade = entidade;}

	public List<Ocupacao> getLista() {return lista;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Ocupacao>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			if(this.situacao != null && this.situacao != 0l){
				setPagedList(ocupacaoService.search(nomenclatura, situacao, getDataTable().getFirst(), getDataTable().getRows()));
			} else {
				setPagedList(ocupacaoService.search(nomenclatura, getDataTable().getFirst(), getDataTable().getRows()));
			}

			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<Ocupacao> getPagedList() {return pagedList;}
	public void setPagedList(List<Ocupacao> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO
	
}