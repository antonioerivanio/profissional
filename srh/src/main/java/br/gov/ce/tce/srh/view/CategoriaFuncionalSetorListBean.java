package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.CategoriaFuncionalSetorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("categoriaFuncionalSetorListBean")
@Scope("session")
public class CategoriaFuncionalSetorListBean implements Serializable {

	static Logger logger = Logger.getLogger(CategoriaFuncionalSetorListBean.class);

	@Autowired
	private CategoriaFuncionalSetorService categoriaFuncionalSetorService;

	@Autowired
	private SetorService setorService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;

	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// entidades das telas
	private CategoriaFuncionalSetor entidade;

	private Setor setor;

	// combos
	private List<Setor> comboSetor;

	// paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<CategoriaFuncionalSetor> pagedList = new ArrayList<CategoriaFuncionalSetor>();
	private int flagRegistroInicial = 0;

	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			// validando campos da consulta
			if( this.setor == null )
				throw new SRHRuntimeException("Selecione um setor.");
			

			count  = categoriaFuncionalSetorService.count( this.setor );
				
			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
			passouConsultar = true;

		} catch (SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}

	/**
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {

		try {

			categoriaFuncionalSetorService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		entidade = new CategoriaFuncionalSetor();
		return consultar();
	}
	
	public String relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();
			if(count == 0){
				throw new SRHRuntimeException("Realize uma consulta primeiro.");
			} else {
				parametros.put("FILTRO", setor.getId());				
			}

			relatorioUtil.relatorio("categoriaFuncionalSetor.jasper", parametros, "categoriaFuncionalSetor.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório das Férias. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Combo Setor
	 * 
	 * @return
	 */
	public List<Setor> getComboSetor() {

        try {

        	if ( this.comboSetor == null )
        		this.comboSetor = setorService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboSetor;
	}


	
	// GETs e SETs	
	public CategoriaFuncionalSetor getEntidade() {
		return entidade;
	}

	public void setEntidade(CategoriaFuncionalSetor entidade) {
		this.entidade = entidade;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public void setComboSetor(List<Setor> comboSetor) {
		this.comboSetor = comboSetor;
	}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setor = null;
			comboSetor = null;
			limparListas();
			flagRegistroInicial = 0;
		}
				
		passouConsultar = false;
		return form;
	}
	
	public String limpaTela() {
		setEntidade(new CategoriaFuncionalSetor());
		this.setor = null;
		return "listar";
	}
	
	

	// PAGINAÇÃO
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<CategoriaFuncionalSetor>();
	}

	public HtmlDataTable getDataTable() {
		return dataTable;
	}

	public void setDataTable(HtmlDataTable dataTable) {
		this.dataTable = dataTable;
	}

	public PagedListDataModel getDataModel() {
		if (flagRegistroInicial != getDataTable().getFirst()) {
			flagRegistroInicial = getDataTable().getFirst();

			// realizando consulta
			setPagedList(categoriaFuncionalSetorService.search(this.setor,
					getDataTable().getFirst(), getDataTable().getRows()));

			if (count != 0) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}

		}
		return dataModel;
	}

	public List<CategoriaFuncionalSetor> getPagedList() {
		return pagedList;
	}

	public void setPagedList(List<CategoriaFuncionalSetor> pagedList) {
		this.pagedList = pagedList;
	}
	// FIM PAGINAÇÃO
	
	public boolean isPassouConsultar() {
		return passouConsultar;
	}

	public void setPassouConsultar(boolean passouConsultar) {
		this.passouConsultar = passouConsultar;
	}

}
