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

import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.OcupacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC005_Manter Cargo
* 
* @since   : Sep 13, 2011, 17:28:36 AM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("cargoListBean")
@Scope("session")
public class CargoListBean implements Serializable {

	static Logger logger = Logger.getLogger(CargoListBean.class);

	@Autowired
	private OcupacaoService ocupacaoService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametros da tela de consulta
	private Long situacao;
	private String nomenclatura = new String();

	// entidades das telas
	private List<Ocupacao> lista;
	private Ocupacao entidade = new Ocupacao();
	
	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Ocupacao> pagedList = new ArrayList<Ocupacao>();
	private int flagRegistroInicial = 0;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			if ( this.situacao != null && this.situacao != 0l ) {
				count = ocupacaoService.count(nomenclatura, situacao);
			} else {
				count = ocupacaoService.count(nomenclatura);
			}

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}
			
			flagRegistroInicial = -1;
			passouConsultar = true;

		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}


	/**
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {

		try {

			ocupacaoService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		entidade = new Ocupacao();
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

			StringBuffer filtro = new StringBuffer();
			filtro.append(" WHERE 1=1 ");

			if ( this.nomenclatura != null && !this.nomenclatura.equalsIgnoreCase("") )
				filtro.append(" AND upper( nomenclatura ) LIKE upper( '%" + this.nomenclatura + "%' ) ");
			if ( this.situacao != null && this.situacao != 0)
				filtro.append(" AND situacao = " + this.situacao );

			parametros.put("FILTRO", filtro.toString());

			relatorioUtil.relatorio("cargo.jasper", parametros, "cargo.pdf");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na geração do relatório. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}


		return null;
	}


	/**
	 * Gets and Sets
	 */
	public Long getSituacao() {return situacao;}
	public void setSituacao(Long situacao) {this.situacao = situacao;}
	
	public String getNomenclatura() {return nomenclatura;}
	public void setNomenclatura(String nomenclatura) {this.nomenclatura = nomenclatura;}

	public Ocupacao getEntidade() {return entidade;}
	public void setEntidade(Ocupacao entidade) {this.entidade = entidade;}

	public List<Ocupacao> getLista() {return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			nomenclatura = new String();
			situacao = null;
			lista = new ArrayList<Ocupacao>();
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
		pagedList = new ArrayList<Ocupacao>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			if(this.situacao != null && this.situacao != 0l){
				setPagedList(ocupacaoService.search(nomenclatura, situacao, getDataTable().getFirst(), getDataTable().getRows()));
			} else {
				setPagedList(ocupacaoService.search(nomenclatura, getDataTable().getFirst(), getDataTable().getRows()));
			}

			if(count != 0){
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<Ocupacao> getPagedList() {return pagedList;}
	public void setPagedList(List<Ocupacao> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO
	
}