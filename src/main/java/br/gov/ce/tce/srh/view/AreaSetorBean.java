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

import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.AreaSetorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("areaSetorBean")
@Scope("view")
public class AreaSetorBean implements Serializable {

	static Logger logger = Logger.getLogger(AreaSetorBean.class);

	@Autowired
	private AreaSetorService areaSetorService;
	
	@Autowired
	private RelatorioUtil  relatorioUtil;

	@Autowired
	private SetorService setorService;

	// parametros da tela de consulta
	private String descricao = new String();

	// entidades das telas
	private List<AreaSetor> lista;
	private AreaSetor entidade = new AreaSetor();

	// combos
	private List<Setor> comboSetor;

	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<AreaSetor> pagedList = new ArrayList<AreaSetor>();
	private int flagRegistroInicial = 0;
	
	@PostConstruct
	private void init() {
		AreaSetor flashParameter = (AreaSetor)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new AreaSetor());
    }

	public void consultar() {

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

		} catch (SRHRuntimeException e) {
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
	}
	
	public String editar() {
		FacesUtil.setFlashParameter("entidade", getEntidade());        
        return "incluirAlterar";
	}

	public void excluir() {

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
		setEntidade(new AreaSetor());
		consultar();
	}

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
	
	public void relatorio() {

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
	}	

	public String getDescricao() {return descricao;}
	public void setDescricao(String descricao) {this.descricao = descricao;}

	public AreaSetor getEntidade() {return entidade;}
	public void setEntidade(AreaSetor entidade) {this.entidade = entidade;}

	public List<AreaSetor> getLista(){return lista;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<AreaSetor>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

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