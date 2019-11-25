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

import br.gov.ce.tce.srh.domain.Instituicao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.InstituicaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("instituicaoBean")
@Scope("view")
public class InstituicaoBean implements Serializable {

	static Logger logger = Logger.getLogger(InstituicaoBean.class);

	@Autowired
	private InstituicaoService instituicaoService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;

	// parametro da tela de consulta
	private String descricao = new String();

	// entidades das telas
	private List<Instituicao> lista = new ArrayList<Instituicao>();
	private Instituicao entidade = new Instituicao();
	
	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Instituicao> pagedList = new ArrayList<Instituicao>();
	private int flagRegistroInicial = 0;

	@PostConstruct
	private void init() {
		Instituicao flashParameter = (Instituicao)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Instituicao());	
    }
	
	public void consultar() {

		try {

			count = instituicaoService.count(descricao);

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

			instituicaoService.salvar(entidade);
			setEntidade( new Instituicao() );

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

			instituicaoService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new Instituicao() );
		consultar();
	}

	public void relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			if (this.descricao != null && !this.descricao.equalsIgnoreCase("")) {
				String filtro = " WHERE upper( descricao ) like '%" + this.descricao.toUpperCase() + "%' ";
				parametros.put("FILTRO", filtro.toString());
			}

			relatorioUtil.relatorio("instituicao.jasper", parametros, "instituicao.pdf");

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public Instituicao getEntidade() {return entidade;}
	public void setEntidade(Instituicao entidade) {this.entidade = entidade;}

	public List<Instituicao> getLista(){return lista;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Instituicao>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(instituicaoService.search(descricao, getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<Instituicao> getPagedList() {return pagedList;}
	public void setPagedList(List<Instituicao> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}