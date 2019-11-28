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

import br.gov.ce.tce.srh.domain.Especialidade;
import br.gov.ce.tce.srh.domain.OrientacaoCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.OrientacaoCargoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("orientacaoCargoBean")
@Scope("view")
public class OrientacaoCargoBean implements Serializable {

	static Logger logger = Logger.getLogger(OrientacaoCargoBean.class);

	@Autowired
	private OrientacaoCargoService orientacaoCargoService;

	@Autowired
	private RelatorioUtil relatorioUtil;

	// parametro da tela de consulta
	private String descricao = new String();

	// entidades das telas
	private List<OrientacaoCargo> lista = new ArrayList<OrientacaoCargo>();
	private OrientacaoCargo entidade = new OrientacaoCargo();

	// combos
	private List<Especialidade> comboEspecialidade;
	
	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<OrientacaoCargo> pagedList = new ArrayList<OrientacaoCargo>();
	private int flagRegistroInicial = 0;

	@PostConstruct
	public void init() {
		OrientacaoCargo flashParameter = (OrientacaoCargo)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new OrientacaoCargo());
		this.descricao = new String();
		this.lista = new ArrayList<OrientacaoCargo>();
		limparListas();
		flagRegistroInicial = 0;		
	}
	
	public void consultar() {

		try {

			count = orientacaoCargoService.count(getDescricao());
			
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

			orientacaoCargoService.salvar(entidade);
			setEntidade( new OrientacaoCargo() );

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

			orientacaoCargoService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new OrientacaoCargo() );
		consultar();
	}	
	
	public void relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

//			if (this.descricao != null && !this.descricao.equalsIgnoreCase("")) {
//				String filtro = " WHERE upper( descricao ) LIKE upper( '%" + this.descricao + "%' ) ";
//				parametros.put("FILTRO", filtro.toString());
//			}

			relatorioUtil.relatorio("especialidadeOrientacaoCargo.jasper", parametros, "especialidadeOrientacaoCargo.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public List<Especialidade> getComboEspecialidade() {

		try {

			if ( this.comboEspecialidade == null )
				this.comboEspecialidade = orientacaoCargoService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo especialidade. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboEspecialidade;
	}	

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public OrientacaoCargo getEntidade() {return entidade;}
	public void setEntidade(OrientacaoCargo entidade) {this.entidade = entidade;}

	public List<OrientacaoCargo> getLista(){return lista;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<OrientacaoCargo>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(orientacaoCargoService.search(descricao, getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<OrientacaoCargo> getPagedList() {return pagedList;}
	public void setPagedList(List<OrientacaoCargo> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}