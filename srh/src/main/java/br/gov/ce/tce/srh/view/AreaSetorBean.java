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

import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.AreaSetorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC002_Manter Área de Competência do Setor
* 
* @since   : Aug 30, 2011, 3:23:38 PM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("areaSetorBean")
@Scope("session")
public class AreaSetorBean implements Serializable {

	static Logger logger = Logger.getLogger(AreaSetorBean.class);

	@Autowired
	private AreaSetorService areaSetorService;
	
	@Autowired
	private RelatorioUtil  relatorioUtil;

	@Autowired
	private SetorService setorService;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private String descricao = new String();

	// entidades das telas
	private List<AreaSetor> lista;
	private AreaSetor entidade = new AreaSetor();

	// combos
	private List<Setor> comboSetor;

	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<AreaSetor> pagedList = new ArrayList<AreaSetor>();
	private int flagRegistroInicial = 0;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			// validando campos da entidade
			if (entidade.getSetor() == null)
				throw new SRHRuntimeException("Selecione um setor.");

			count = areaSetorService.count( entidade.getSetor().getId(), getDescricao() );
			
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
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
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

		try{

			areaSetorService.salvar(entidade);
			setEntidade( new AreaSetor() );

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());		
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
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

			areaSetorService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

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


	/**
	 * Emitir Relatorio
	 * 
	 * @return
	 */
	public String relatorio() {

		try {

			// validando campos da entidade
			if (entidade.getSetor() == null)
				throw new SRHRuntimeException("Selecione o campo setor.");

			Map<String, Object> parametros = new HashMap<String, Object>();

			StringBuffer filtro = new StringBuffer();
			filtro.append(" WHERE areasetor.IDSETOR = " + this.entidade.getSetor().getId());
			
			if (this.descricao != null && !this.descricao.equalsIgnoreCase(""))
				filtro.append( " AND upper( areasetor.descricao ) like '%" + this.descricao.toUpperCase() + "%' " );

			parametros.put("FILTRO", filtro.toString());

			relatorioUtil.relatorio("areasetor.jasper", parametros, "areasetor.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public String limpaTela() {
	   setEntidade(new AreaSetor());
       lista = new ArrayList<AreaSetor>();
       if(passouConsultar){
    	   return "incluirAlterar";
       }
       return "incluirAlterar";
		
	}

	/**
	 * Gets and Sets
	 */
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public AreaSetor getEntidade() {return entidade;}
	public void setEntidade(AreaSetor entidade) {this.entidade = entidade;}

	public List<AreaSetor> getLista(){return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new AreaSetor() );
			comboSetor = null;
			descricao = new String();
			lista = new ArrayList<AreaSetor>();
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<AreaSetor>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(areaSetorService.search(entidade.getSetor().getId(), getDescricao(), getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<AreaSetor> getPagedList() {return pagedList;}
	public void setPagedList(List<AreaSetor> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}