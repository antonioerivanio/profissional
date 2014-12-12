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
import br.gov.ce.tce.srh.domain.RepresentacaoSetor;
import br.gov.ce.tce.srh.domain.sapjava.Setor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.RepresentacaoCargoService;
import br.gov.ce.tce.srh.service.RepresentacaoSetorService;
import br.gov.ce.tce.srh.service.sapjava.SetorService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC027_Manter Cargo de Representação por Setor
* 
* @since   : Out 19, 2011, 12:13:00 PM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("representacaoSetorBean")
@Scope("session")
public class RepresentacaoSetorBean implements Serializable {

	static Logger logger = Logger.getLogger(RepresentacaoSetorBean.class);

	@Autowired
	private RepresentacaoSetorService representacaoSetorService;

	@Autowired
	private RepresentacaoCargoService representacaoCargoService;

	@Autowired
	private SetorService setorService;
	
	@Autowired
	private RelatorioUtil relatorioUtil;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// parametro da tela de consulta
	private RepresentacaoCargo cargo = new RepresentacaoCargo();

	// entidades das telas
	private List<RepresentacaoSetor> lista = new ArrayList<RepresentacaoSetor>();
	private RepresentacaoSetor entidade = new RepresentacaoSetor();

	// combos
	private List<RepresentacaoCargo> comboCargo;
	private List<Setor> comboSetor;
	
	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<RepresentacaoSetor> pagedList = new ArrayList<RepresentacaoSetor>();
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

			count = representacaoSetorService.count(this.cargo.getId());

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

			representacaoSetorService.salvar(entidade);
			setEntidade( new RepresentacaoSetor() );
			getEntidade().setAtivo(true);

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

			representacaoSetorService.excluir(entidade);

			FacesUtil.addInfoMessage("Registro excluído com sucesso.");
			logger.info("Registro excluído com sucesso.");

		} catch (DataAccessException e) {
			FacesUtil.addErroMessage("Existem registros filhos utilizando o registro selecionado. Exclusão não poderá ser realizada.");
			logger.error("Ocorreu o seguinte erro: " + e.getMessage());			
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao excluir. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		setEntidade( new RepresentacaoSetor() );
		getEntidade().setAtivo(true);

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
	 * Combo Setor
	 * 
	 * @return
	 */
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

			relatorioUtil.relatorio("cargodeRepresentacaoSetor.jasper", parametros, "cargodeRepresentacaoSetor.pdf");

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
		limparListas();
		return "listar";
	}



	/**
	 * Gets and Sets
	 */
	public RepresentacaoCargo getCargo() {return cargo;}
	public void setCargo(RepresentacaoCargo cargo) {this.cargo = cargo;}

	public RepresentacaoSetor getEntidade() {return entidade;}
	public void setEntidade(RepresentacaoSetor entidade) {this.entidade = entidade;}

	public List<RepresentacaoSetor> getLista(){return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new RepresentacaoSetor() );
			getEntidade().setAtivo(true);
			comboCargo = null;
			comboSetor = null;
			cargo = null;
			lista = new ArrayList<RepresentacaoSetor>();
		}
		passouConsultar = false;
		return form;
	}
	
	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new HtmlDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<RepresentacaoSetor>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			setPagedList(representacaoSetorService.search(this.cargo.getId(), getDataTable().getFirst(), getDataTable().getRows()));
			if(count != 0) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<RepresentacaoSetor> getPagedList() {return pagedList;}
	public void setPagedList(List<RepresentacaoSetor> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}