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

import br.gov.ce.tce.srh.domain.RepresentacaoCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.RepresentacaoCargoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC007_Manter Cargo Comissionado
* 
* @since   : Aug 31, 2011, 9:13:58 AM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("cargoComissionadoBean")
@Scope("session")
public class CargoComissionadoBean implements Serializable {

	static Logger logger = Logger.getLogger(CargoComissionadoBean.class);

	@Autowired
	private RepresentacaoCargoService representacaoCargoService;

	@Autowired
	private RelatorioUtil relatorioUtil;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametro da tela de consulta
	private String nomenclatura = new String();

	// entidades das telas
	private List<RepresentacaoCargo> lista;
	private RepresentacaoCargo entidade = new RepresentacaoCargo();

	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<RepresentacaoCargo> pagedList = new ArrayList<RepresentacaoCargo>();
	private int flagRegistroInicial = 0;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			count = representacaoCargoService.count(nomenclatura);

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
			passouConsultar = true;

		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
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

			representacaoCargoService.salvar(entidade);

			setEntidade( new RepresentacaoCargo() );
			getEntidade().setAtivo(true);

			FacesUtil.addInfoMessage("Operação realizada com sucesso");
			logger.info("Operação realizada com sucesso");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.error(e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
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
		return consultar();
	}


	/**
	 * Emitir Relatorio
	 * 
	 * @return  
	 */
	public String relatorio() {

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

		return null;
	}
	
	public String limpaTela() {
		setEntidade(new RepresentacaoCargo());
		return "listar";
	}
	

	/**
	 * Gets and Sets
	 */
	public String getNomenclatura() {return nomenclatura;}
	public void setNomenclatura(String nomenclatura) {this.nomenclatura = nomenclatura;}

	public RepresentacaoCargo getEntidade() {return entidade;}
	public void setEntidade(RepresentacaoCargo entidade) {this.entidade = entidade;}

	public List<RepresentacaoCargo> getLista(){return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new RepresentacaoCargo() );
			getEntidade().setAtivo(true);
			nomenclatura = new String();
			lista = new ArrayList<RepresentacaoCargo>();
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
		pagedList = new ArrayList<RepresentacaoCargo>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

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
	//FIM PAGINAÇÃO

}