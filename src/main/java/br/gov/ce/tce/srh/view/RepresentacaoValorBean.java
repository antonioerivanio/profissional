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

import br.gov.ce.tce.srh.domain.RepresentacaoCargo;
import br.gov.ce.tce.srh.domain.RepresentacaoValor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.RepresentacaoCargoService;
import br.gov.ce.tce.srh.service.RepresentacaoValorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("representacaoValorBean")
@Scope("view")
public class RepresentacaoValorBean implements Serializable {

	static Logger logger = Logger.getLogger(RepresentacaoValorBean.class);

	@Autowired
	private RepresentacaoValorService representacaoValorService;

	@Autowired
	private RepresentacaoCargoService representacaoCargoService;

	@Autowired
	private RelatorioUtil relatorioUtil;

	// parametro da tela de consulta
	private RepresentacaoCargo cargo;

	// entidades das telas
	private List<RepresentacaoValor> lista;
	private RepresentacaoValor entidade = new RepresentacaoValor();

	// combos
	private List<RepresentacaoCargo> comboCargo;

	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<RepresentacaoValor> pagedList = new ArrayList<RepresentacaoValor>();
	private int flagRegistroInicial = 0;

	@PostConstruct
	public void init() {		
		RepresentacaoValor flashParameter = (RepresentacaoValor)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new RepresentacaoValor());
		comboCargo = null;
		cargo = null;
		lista = new ArrayList<RepresentacaoValor>();
		limparListas();
		flagRegistroInicial = 0;
	}
	
	
	public void consultar() {

		try {

			if ( this.cargo == null )
				throw new SRHRuntimeException("Selecione um cargo.");

			count = representacaoValorService.count(cargo.getId());

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

		try {

			representacaoValorService.salvar(entidade);

			setEntidade( new RepresentacaoValor() );
			this.comboCargo = null;

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

			representacaoValorService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new RepresentacaoValor() );
		this.comboCargo = null;

		consultar();
	}

	public List<RepresentacaoCargo> getComboCargo() {

        try {

        	if ( this.comboCargo == null )
        		this.comboCargo = representacaoCargoService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo cargo. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboCargo;
	}

	public void relatorio() {

		try {

			// validando campos da cosnulta
			if ( this.cargo == null )
				throw new SRHRuntimeException("Selecione o cargo.");

			Map<String, Object> parametros = new HashMap<String, Object>();

			if (this.cargo != null && this.cargo.getId() != null) {
				String filtro = " WHERE repr.IDREPRESENTACAOCARGO = " + this.cargo.getId();
				parametros.put("FILTRO", filtro);
			}

			relatorioUtil.relatorio("cargodeRepresentacaoValor.jasper", parametros, "cargodeRepresentacaoValor.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}
	
	public RepresentacaoCargo getCargo() {return cargo;}
	public void setCargo(RepresentacaoCargo cargo) {this.cargo = cargo;}

	public RepresentacaoValor getEntidade() {return entidade;}
	public void setEntidade(RepresentacaoValor entidade) {this.entidade = entidade;}

	public List<RepresentacaoValor> getLista(){return lista;}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<RepresentacaoValor>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(representacaoValorService.search(this.cargo.getId(), getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<RepresentacaoValor> getPagedList() {return pagedList;}
	public void setPagedList(List<RepresentacaoValor> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}