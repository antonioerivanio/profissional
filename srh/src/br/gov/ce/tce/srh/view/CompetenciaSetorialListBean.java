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

import br.gov.ce.tce.srh.domain.CompetenciaSetorial;
import br.gov.ce.tce.srh.domain.sapjava.Setor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CompetenciaService;
import br.gov.ce.tce.srh.service.CompetenciaSetorialService;
import br.gov.ce.tce.srh.service.sapjava.SetorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("competenciaSetorialListBean")
@Scope("session")
public class CompetenciaSetorialListBean implements Serializable {

	static Logger logger = Logger.getLogger(AreaSetorCompetenciaFormBean.class);

	@Autowired
	private CompetenciaSetorialService competenciaSetorialService;

	@Autowired
	private SetorService setorService;

	@Autowired
	private CompetenciaService competenciaService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;

	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// entidades das telas
	private CompetenciaSetorial entidade;

	private Setor setor;
	private Long tipo;

	// combos
	private List<Setor> comboSetor;

	// pagina��o
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<CompetenciaSetorial> pagedList = new ArrayList<CompetenciaSetorial>();
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
			if( this.tipo == null )
				throw new SRHRuntimeException("Selecione uma Tipo.");

			// realizando consulta
			if( this.tipo == null || this.tipo == 0 ) {
				count  = competenciaSetorialService.count( this.setor );
			} else {
				count  = competenciaSetorialService.count( this.setor, this.tipo );
			}
	
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
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Opera��o cancelada.");
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

			competenciaSetorialService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro exclu�do com sucesso.");
			logger.info("Registro exclu�do com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclus�o n�o poder� ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		entidade = new CompetenciaSetorial();
		return consultar();
	}
	
	public String relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();
			if(count == 0){
				throw new SRHRuntimeException("Realize uma consulta primeiro.");
			} else {
				parametros.put("FILTRO", setor.getId());
				parametros.put("TIPO", tipo);
			}

			relatorioUtil.relatorio("competenciaSetorial.jasper", parametros, "competenciaSetorial.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na gera��o do Relat�rio das Ferias. Opera��o cancelada.");
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
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Opera��o cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboSetor;
	}

	
	// GETs e SETs	
	public CompetenciaSetorial getEntidade() {
		return entidade;
	}

	public void setEntidade(CompetenciaSetorial entidade) {
		this.entidade = entidade;
	}

	public Setor getSetor() {
		return setor;
	}

	public void setSetor(Setor setor) {
		this.setor = setor;
	}

	public Long getTipo() {
		return tipo;
	}

	public void setTipo(Long tipo) {
		this.tipo = tipo;
	}

	public void setComboSetor(List<Setor> comboSetor) {
		this.comboSetor = comboSetor;
	}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setor = null;
			tipo = null;
			comboSetor = null;
			limparListas();
			flagRegistroInicial = 0;
		}
				
		passouConsultar = false;
		return form;
	}
	
	public String limpaTela() {
		setEntidade(new CompetenciaSetorial());
		this.tipo = null;
		this.setor = null;
		return "listar";
	}
	
	

	// PAGINA��O
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<CompetenciaSetorial>();
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
			if(tipo != null && tipo != 0){
				setPagedList(competenciaSetorialService.search(
						this.setor, this.tipo, getDataTable().getFirst(),
						getDataTable().getRows()));
			} else {
				setPagedList(competenciaSetorialService.search(
						this.setor, getDataTable().getFirst(),
						getDataTable().getRows()));
			}	

			if (count != 0) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}

		}
		return dataModel;
	}

	public List<CompetenciaSetorial> getPagedList() {
		return pagedList;
	}

	public void setPagedList(List<CompetenciaSetorial> pagedList) {
		this.pagedList = pagedList;
	}
	// FIM PAGINA��O
	
	public boolean isPassouConsultar() {
		return passouConsultar;
	}

	public void setPassouConsultar(boolean passouConsultar) {
		this.passouConsultar = passouConsultar;
	}

}
