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

import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.RepresentacaoCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.RepresentacaoCargoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

@SuppressWarnings("serial")
@Component("cargoComissionadoBean")
@Scope("view")
public class CargoComissionadoBean implements Serializable {

	static Logger logger = Logger.getLogger(CargoComissionadoBean.class);

	@Autowired
	private RepresentacaoCargoService representacaoCargoService;

	@Autowired
	private RelatorioUtil relatorioUtil;	
	
	// parametro da tela de consulta
	private String nomenclatura = new String();

	// entidades das telas
	private List<RepresentacaoCargo> lista;
	private RepresentacaoCargo entidade = new RepresentacaoCargo();

	// paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<RepresentacaoCargo> pagedList = new ArrayList<RepresentacaoCargo>();
	private int flagRegistroInicial = 0;	
	
	@PostConstruct
	private void init() {
		RepresentacaoCargo flashParameter = (RepresentacaoCargo)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new RepresentacaoCargo());
		if(entidade.getEsocialVigencia() == null) {
			entidade.setEsocialVigencia(new ESocialEventoVigencia());
		}	
    }
	
	public void consultar() {

		try {

			count = representacaoCargoService.count(nomenclatura);

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;

		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}
	
	public void salvar() {

		try {

			representacaoCargoService.salvar(entidade);

			limparForm();

			FacesUtil.addInfoMessage("Operação realizada com sucesso");
			logger.info("Operação realizada com sucesso");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public String editar() {
		FacesUtil.setFlashParameter("entidade", getEntidade());        
        return "incluirAlterar";
	}

	
	public void excluir() {

		try {

			representacaoCargoService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new RepresentacaoCargo() );
		getEntidade().setAtivo(true);
		
		this.consultar();
	}
	
	public void relatorio() {

		try {

			Map<String, Object> parametros = new HashMap<String, Object>();

			if (this.nomenclatura != null && !this.nomenclatura.equalsIgnoreCase("")) {
				String filtro = " WHERE upper( nomenclatura ) LIKE upper( '%" + this.nomenclatura + "%' ) ";
				parametros.put("FILTRO", filtro);
			}

			relatorioUtil.relatorio("representacaocargo.jasper", parametros, "representacaocargo.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}
	
	private void limparForm() {
		setEntidade( new RepresentacaoCargo() );
		getEntidade().setAtivo(true);
	}
	
	
	public String getNomenclatura() {return nomenclatura;}
	public void setNomenclatura(String nomenclatura) {this.nomenclatura = nomenclatura;}

	public RepresentacaoCargo getEntidade() {return entidade;}
	public void setEntidade(RepresentacaoCargo entidade) {this.entidade = entidade;}

	public List<RepresentacaoCargo> getLista(){return lista;}
	
	
	// PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<RepresentacaoCargo>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList( representacaoCargoService.search(nomenclatura, getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<RepresentacaoCargo> getPagedList() {return pagedList;}
	public void setPagedList(List<RepresentacaoCargo> pagedList) {this.pagedList = pagedList;}
	// FIM PAGINAÇÃO

}