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

import br.gov.ce.tce.srh.domain.AreaProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaProfissionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("areaProfissionalBean")
@Scope("view")
public class AreaProfissionalBean implements Serializable {

	static Logger logger = Logger.getLogger(AreaProfissionalBean.class);

	@Autowired
	private RelatorioUtil relatorioUtil;
	
	@Autowired
	private AreaProfissionalService areaProfissionalService;

	// parametro da tela de consulta
	private String descricao = new String();

	// entidades das telas
	private List<AreaProfissional> lista;
	private AreaProfissional entidade = new AreaProfissional();

	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<AreaProfissional> pagedList = new ArrayList<AreaProfissional>();
	private int flagRegistroInicial = 0;

	@PostConstruct
	private void init() {
		AreaProfissional flashParameter = (AreaProfissional)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new AreaProfissional());
    }
	
	public void consultar() {

		try {

			count = areaProfissionalService.count( this.descricao );
			
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

			areaProfissionalService.salvar(entidade);
			setEntidade( new AreaProfissional() );

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

			areaProfissionalService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new AreaProfissional() );
		consultar();
	}

	public void relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			if (this.descricao != null && !this.descricao.equalsIgnoreCase("")) {
				String filtro = "where upper( descricao ) like '%" + this.descricao.toUpperCase() + "%' ";
				parametros.put("FILTRO", filtro);
			}

			relatorioUtil.relatorio("areaprofissional.jasper", parametros, "areaprofissional.pdf");

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public AreaProfissional getEntidade() {return entidade;}
	public void setEntidade(AreaProfissional entidade) {this.entidade = entidade;}

	public List<AreaProfissional> getLista(){return lista;}	
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<AreaProfissional>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(areaProfissionalService.search(descricao, getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<AreaProfissional> getPagedList() {return pagedList;}
	public void setPagedList(List<AreaProfissional> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}