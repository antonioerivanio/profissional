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

import br.gov.ce.tce.srh.domain.AreaAcademica;
import br.gov.ce.tce.srh.domain.CursoAcademica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaAcademicaService;
import br.gov.ce.tce.srh.service.CursoAcademicaService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("cursoAcademicaBean")
@Scope("view")
public class CursoAcademicaBean implements Serializable {

	static Logger logger = Logger.getLogger(CursoAcademicaBean.class);

	@Autowired
	private CursoAcademicaService cursoAcademicaService;

	@Autowired
	private AreaAcademicaService areaAcademicaService;

	@Autowired
	private RelatorioUtil relatorioUtil;

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

	@PostConstruct
	private void init() {
		CursoAcademica flashParameter = (CursoAcademica)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new CursoAcademica());			
    }

	public void consultar() {

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

		} catch(SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public void salvar() {

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
	}
	
	public String editar() {
		FacesUtil.setFlashParameter("entidade", getEntidade());        
        return "incluirAlterar";
	}

	public void excluir() {

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
		consultar();
	}

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

	public void relatorio() {

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
	}	
	
	public AreaAcademica getArea() {return area;}
	public void setArea(AreaAcademica area) {this.area = area;}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public CursoAcademica getEntidade() {return entidade;}
	public void setEntidade(CursoAcademica entidade) {this.entidade = entidade;}

	public List<CursoAcademica> getLista(){return lista;}	
	
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