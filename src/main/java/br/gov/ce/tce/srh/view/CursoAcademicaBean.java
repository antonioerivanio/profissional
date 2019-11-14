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

import br.gov.ce.tce.srh.domain.AreaAcademica;
import br.gov.ce.tce.srh.domain.CursoAcademica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaAcademicaService;
import br.gov.ce.tce.srh.service.CursoAcademicaService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC011_Manter Curso  de Formação Acadêmica
* 
* @since   : Set 9, 2011, 3:53:00 PM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("cursoAcademicaBean")
@Scope("session")
public class CursoAcademicaBean implements Serializable {

	static Logger logger = Logger.getLogger(CursoAcademicaBean.class);

	@Autowired
	private CursoAcademicaService cursoAcademicaService;

	@Autowired
	private AreaAcademicaService areaAcademicaService;

	@Autowired
	private RelatorioUtil relatorioUtil;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private AreaAcademica area;
	private String descricao = new String();

	// entidades das telas
	private List<CursoAcademica> lista = new ArrayList<CursoAcademica>();
	private CursoAcademica entidade = new CursoAcademica();

	// combos
	private List<AreaAcademica> comboArea;
	
	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<CursoAcademica> pagedList = new ArrayList<CursoAcademica>();
	private int flagRegistroInicial = 0;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			if (this.area == null) {
				count = cursoAcademicaService.count(this.descricao);
			} else {
				count = cursoAcademicaService.count(this.area.getId(), this.descricao);
			}

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
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {

			cursoAcademicaService.salvar(entidade);	
			setEntidade( new CursoAcademica() );

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

			cursoAcademicaService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new CursoAcademica() );
		return consultar();
	}


	/**
	 * Combo Area
	 * 
	 * @return
	 */
	public List<AreaAcademica> getComboArea() {

        try {

        	if ( this.comboArea == null )
        		this.comboArea = areaAcademicaService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo área. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboArea;
	}


	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */
	public String relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			StringBuffer filtro = new StringBuffer();
			filtro.append(" WHERE 1 = 1 ");

			if ( this.area != null )
				filtro.append(" AND IDAREAFORMACAO = " + this.area.getId() );

			if ( this.descricao != null && !this.descricao.equalsIgnoreCase("") )
				filtro.append(" AND upper( descricao ) like '%" + this.descricao.toUpperCase() + "%' ");

			parametros.put("FILTRO", filtro.toString());

			relatorioUtil.relatorio("cursoacademico.jasper", parametros, "cursoacademico.pdf");

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}
	
	public String limpaTela() {
		setEntidade(new CursoAcademica());
		setArea(new AreaAcademica());
		descricao = null;
		return "listar";
	}
	
	
	/**
	 * Gets and Sets
	 */
	public AreaAcademica getArea() {return area;}
	public void setArea(AreaAcademica area) {this.area = area;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public CursoAcademica getEntidade() {return entidade;}
	public void setEntidade(CursoAcademica entidade) {this.entidade = entidade;}

	public List<CursoAcademica> getLista(){return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new CursoAcademica() );
			this.area = null;
			this.comboArea = null;
			this.descricao = new String();
			this.lista = new ArrayList<CursoAcademica>();
			limparListas();
			flagRegistroInicial = 0;
		}
		passouConsultar = false;
		return form;
	}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<CursoAcademica>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			if (this.area == null) {
				setPagedList(cursoAcademicaService.search(this.descricao, getDataTable().getFirst(), getDataTable().getRows()));
			} else {
				setPagedList(cursoAcademicaService.search(this.area.getId(), this.descricao, getDataTable().getFirst(), getDataTable().getRows()));
			}
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<CursoAcademica> getPagedList() {return pagedList;}
	public void setPagedList(List<CursoAcademica> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}