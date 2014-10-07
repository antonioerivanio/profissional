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

import br.gov.ce.tce.srh.domain.TipoDependencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.TipoDependenciaService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC008_Manter Tipo de Depend�ncia
* 
* @since   : Aug 31, 2011, 11:23:18 AM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("tipoDependenciaBean")
@Scope("session")
public class TipoDependenciaBean implements Serializable {

	static Logger logger = Logger.getLogger(TipoDependenciaBean.class);

	@Autowired
	private TipoDependenciaService tipoDependenciaService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;

	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametro da tela de consulta
	private String descricao = new String();


	// entidades das telas
	private List<TipoDependencia> lista = new ArrayList<TipoDependencia>();
	private TipoDependencia entidade = new TipoDependencia();
	
	//pagina��o
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<TipoDependencia> pagedList = new ArrayList<TipoDependencia>();
	private int flagRegistroInicial = 0;


	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			count = tipoDependenciaService.count(descricao);
			
			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
			passouConsultar = true;

		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}


	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {

			tipoDependenciaService.salvar(entidade);
			setEntidade( new TipoDependencia() );

			FacesUtil.addInfoMessage("Opera��o realizada com sucesso.");
			logger.info("Opera��o realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());	
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {

		try {

			tipoDependenciaService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro exclu�do com sucesso.");
			logger.info("Registro exclu�do com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclus�o n�o poder� ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new TipoDependencia() );
		return consultar();
	}


	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */
	public String relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			if (this.descricao != null && !this.descricao.equalsIgnoreCase("")) {
				String filtro = " WHERE upper( descricao ) LIKE upper( '%" + this.descricao + "%' ) ";
				parametros.put("FILTRO", filtro.toString());
			}

			relatorioUtil.relatorio("tipodependencia.jasper", parametros, "tipodependencia.pdf");

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na gera��o do relat�rio. Opera��o cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Gets and Sets
	 */
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public TipoDependencia getEntidade() {return entidade;}
	public void setEntidade(TipoDependencia entidade) {this.entidade = entidade;}

	public List<TipoDependencia> getLista(){return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new TipoDependencia() );
			descricao = new String();
			lista = new ArrayList<TipoDependencia>();
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}
	
	//PAGINA��O
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<TipoDependencia>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(tipoDependenciaService.search(descricao, getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<TipoDependencia> getPagedList() {return pagedList;}
	public void setPagedList(List<TipoDependencia> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINA��O

}