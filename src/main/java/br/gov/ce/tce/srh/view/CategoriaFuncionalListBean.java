package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CategoriaFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CadastroCategoriaFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("categoriaFuncionalListBean")
@Scope("session")
@ManagedBean
public class CategoriaFuncionalListBean implements Serializable {
	
	static Logger logger = Logger.getLogger(CategoriaFuncionalListBean.class);
	
	@Autowired
	private CadastroCategoriaFuncionalService cadastroCategoriaFuncionalService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;
	
	private CategoriaFuncional categoriaFuncional = new CategoriaFuncional();
	private List<CategoriaFuncional> categorias = new ArrayList<CategoriaFuncional>();
	
	private int count;
	private boolean passouConsultar = false;
	private int flagRegistroInicial = 0;
	private List<CategoriaFuncional> pagedList = new ArrayList<CategoriaFuncional>();
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private HtmlForm form;
	
	public String consultar(){
		try {
			count = cadastroCategoriaFuncionalService.count(categoriaFuncional.getDescricao());
			if(count == 0 ){
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}
			
			passouConsultar = true;
			flagRegistroInicial = -1;
			
		}  catch(SRHRuntimeException e) {
			
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		return "listar";
	}
	
	public String limpaTela(){
		setCategoriaFuncional(new CategoriaFuncional());
		limparLista();
		return "listar";
	}
	
	public String mostarAtiva(Long ativa){
		String status = "";
		if(ativa == 1L){
			status = "Sim";
		} else {
			status = "Não";
		}
		return status;
	}
	
	public String excluir(){
		try {
			cadastroCategoriaFuncionalService.excluir(categoriaFuncional);
			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");
			
		}  catch (SRHRuntimeException e) {
			
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (DataAccessException e) {
			
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		return consultar();
	}
	
	public String relatorio() {

		try {
			if(count == 0)
				throw new SRHRuntimeException("Realize uma consulta primeiro.");
			
			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("FILTRO", categoriaFuncional.getDescricao());

			relatorioUtil.relatorio("categoriaFuncional.jasper", parametros, "categoriaFuncional.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório das Ferias. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
	
	public List<CategoriaFuncional> listarCategorias(){
		categorias = cadastroCategoriaFuncionalService.listarCategoriasFuncionais();
		return categorias;
	}
	
	public CategoriaFuncional getCategoriaFuncional() {
		return categoriaFuncional;
	}

	public void setCategoriaFuncional(CategoriaFuncional categoriaFuncional) {
		this.categoriaFuncional = categoriaFuncional;
	}

	public List<CategoriaFuncional> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaFuncional> categorias) {
		this.categorias = categorias;
	}


	public boolean isPassouConsultar() {
		return passouConsultar;
	}


	public void setPassouConsultar(boolean passouConsultar) {
		this.passouConsultar = passouConsultar;
	}


	public List<CategoriaFuncional> getPagedList() {
		return pagedList;
	}


	public void setPagedList(List<CategoriaFuncional> pagedList) {
		this.pagedList = pagedList;
	}


	public UIDataTable getDataTable() {
		return dataTable;
	}


	public void setDataTable(UIDataTable dataTable) {
		this.dataTable = dataTable;
	}
	//PAGINAÇÃO
	private void limparLista() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<CategoriaFuncional>(); 
	}


	public PagedListDataModel getDataModel() {
		if(flagRegistroInicial != dataTable.getFirst()){
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(cadastroCategoriaFuncionalService.search(categoriaFuncional.getDescricao(), getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			}else {
				limparLista();
			}
		}
		return dataModel;
	}


	public void setDataModel(PagedListDataModel dataModel) {
		this.dataModel = dataModel;
	}


	public HtmlForm getForm() {
		if(!passouConsultar){
			setCategoriaFuncional(new CategoriaFuncional());
			limparLista();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}


	public void setForm(HtmlForm form) {
		this.form = form;
	}
}
