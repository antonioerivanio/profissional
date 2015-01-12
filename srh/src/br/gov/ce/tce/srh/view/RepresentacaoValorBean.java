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
import br.gov.ce.tce.srh.domain.RepresentacaoValor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.RepresentacaoCargoService;
import br.gov.ce.tce.srh.service.RepresentacaoValorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC028_Manter Cargo de Representação por Valor
* 
* @since   : Out 20, 2011, 14:47:10 PM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("representacaoValorBean")
@Scope("session")
public class RepresentacaoValorBean implements Serializable {

	static Logger logger = Logger.getLogger(RepresentacaoValorBean.class);

	@Autowired
	private RepresentacaoValorService representacaoValorService;

	@Autowired
	private RepresentacaoCargoService representacaoCargoService;

	@Autowired
	private RelatorioUtil relatorioUtil;
	

	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametro da tela de consulta
	private RepresentacaoCargo cargo;

	// entidades das telas
	private List<RepresentacaoValor> lista;
	private RepresentacaoValor entidade = new RepresentacaoValor();

	// combos
	private List<RepresentacaoCargo> comboCargo;

	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<RepresentacaoValor> pagedList = new ArrayList<RepresentacaoValor>();
	private int flagRegistroInicial = 0;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			// validando campos da cosnulta
			if ( this.cargo == null )
				throw new SRHRuntimeException("Selecione um cargo.");

			count = representacaoValorService.count(cargo.getId());

			if (count == 0) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			flagRegistroInicial = -1;
			passouConsultar = true;

		} catch (SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
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

		return null;
	}


	/**
	 * Realizar Exclusao
	 * 
	 * @return
	 */
	public String excluir() {

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

		return consultar();
	}


	/**
	 * Combo Cargo
	 * 
	 * @return
	 */
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


	/**
	 * Emitir Relatorio
	 * 
	 * @return
	 * 
	 */
	public String relatorio() {

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

		return null;
	}
	
	public String limpaTela() {
		this.cargo = new RepresentacaoCargo();
		this.comboCargo = null;
		return "listar";
	}

	/**
	 * Gets and Sets
	 */
	public RepresentacaoCargo getCargo() {return cargo;}
	public void setCargo(RepresentacaoCargo cargo) {this.cargo = cargo;}

	public RepresentacaoValor getEntidade() {return entidade;}
	public void setEntidade(RepresentacaoValor entidade) {this.entidade = entidade;}

	public List<RepresentacaoValor> getLista(){return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new RepresentacaoValor() );
			comboCargo = null;
			cargo = null;
			lista = new ArrayList<RepresentacaoValor>();
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
		pagedList = new ArrayList<RepresentacaoValor>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

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