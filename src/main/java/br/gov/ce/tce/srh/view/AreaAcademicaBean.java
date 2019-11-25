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
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaAcademicaService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("areaAcademicaBean")
@Scope("view")
public class AreaAcademicaBean implements Serializable {

	static Logger logger = Logger.getLogger(AreaAcademicaBean.class);

	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AreaAcademicaService areaFormacaoService;

	// parametro da tela de consulta
	private String descricao = new String();

	// entidades das telas
	private List<AreaAcademica> lista = new ArrayList<AreaAcademica>();
	private AreaAcademica entidade = new AreaAcademica();
	
	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<AreaAcademica> pagedList = new ArrayList<AreaAcademica>();
	private int flagRegistroInicial = 0;
	
	@PostConstruct
	private void init() {
		AreaAcademica flashParameter = (AreaAcademica)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new AreaAcademica());
    }

	public void consultar() {

		try {

			count = areaFormacaoService.count(descricao);
			
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

	public void salvar() {

		try {

			areaFormacaoService.salvar(entidade);
			setEntidade( new AreaAcademica() );

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

			areaFormacaoService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new AreaAcademica() );
		consultar();
	}

	public void relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			if (this.descricao != null && !this.descricao.equalsIgnoreCase("")) {
				String filtro = "where upper( descricao ) like '%" + this.descricao.toUpperCase() + "%' ";
				parametros.put("FILTRO", filtro);
			}

			relatorioUtil.relatorio("areaformacao.jasper", parametros, "areaformacao.pdf");

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	/**
	 * Gets and Sets
	 */
	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public AreaAcademica getEntidade() {return entidade;}
	public void setEntidade(AreaAcademica entidade) {this.entidade = entidade;}

	public List<AreaAcademica> getLista(){return lista;}

	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<AreaAcademica>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(areaFormacaoService.search(descricao, getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<AreaAcademica> getPagedList() {return pagedList;}
	public void setPagedList(List<AreaAcademica> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}