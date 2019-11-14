package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CompetenciaOrganizacional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CompetenciaOrganizacionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
 * Use case : Competencia Organizacional
 * 
 * @since   : Dez 12, 2012, 12:12:12 PM
 * @author  : raphael.ferreira@ivia.com.br
 *
 */
@SuppressWarnings("serial")
@Component("competenciaOrganizacionalListBean")
@Scope("session")
public class CompetenciaOrganizacionalListBean implements Serializable {
	
	static Logger logger = Logger.getLogger(CompetenciaOrganizacionalListBean.class);

	@Autowired
	private CompetenciaOrganizacionalService competenciaOrganizacionalService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;


	//controle de acesso do formulário
	private HtmlForm form;
	private boolean passouConsultar = false;

	//parametos de tela de consulta
	private String tipo = new String();

	private CompetenciaOrganizacional entidade = new CompetenciaOrganizacional();

	//entidades das telas
	private List<CompetenciaOrganizacional> lista;
	
	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<CompetenciaOrganizacional> pagedList = new ArrayList<CompetenciaOrganizacional>();
	private int flagRegistroInicial = 0;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			count = competenciaOrganizacionalService.count( tipo );

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
			passouConsultar = true;

		} catch(SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			limparListas();
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

			competenciaOrganizacionalService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (SRHRuntimeException e) {
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


	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */
	public String relatorio() {

		try {

			//valida consulta pessoa
			if( count == 0 )
				throw new SRHRuntimeException("Realize uma consulta primeiro.");

			Map<String, Object> parametros = new HashMap<String, Object>();
			parametros.put("FILTRO", tipo);

			relatorioUtil.relatorio("competenciaOrganizacional.jasper", parametros, "competenciaOrganizacional.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro na geração do Relatório das Competências Organizacionais. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public String limpaTela() {
		tipo = null;
		passouConsultar = false;
		setEntidade(new CompetenciaOrganizacional());
		lista = new ArrayList<CompetenciaOrganizacional>();
		limparListas();
		flagRegistroInicial = 0;
		
		return "listar";
	}


	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			limpaTela();		
		}
		return form;
	}

	public List<CompetenciaOrganizacional> getLista() {return lista;}
	public void setLista(List<CompetenciaOrganizacional> lista) {this.lista = lista;}

	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<CompetenciaOrganizacional>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(competenciaOrganizacionalService.search(tipo, getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<CompetenciaOrganizacional> getPagedList() {return pagedList;}
	public void setPagedList(List<CompetenciaOrganizacional> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO


	public boolean isPassouConsultar() {
		return passouConsultar;
	}


	public void setPassouConsultar(boolean passouConsultar) {
		this.passouConsultar = passouConsultar;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public CompetenciaOrganizacional getEntidade() {
		return entidade;
	}


	public void setEntidade(CompetenciaOrganizacional entidade) {
		this.entidade = entidade;
	}

}