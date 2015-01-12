package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.richfaces.component.html.HtmlDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.domain.TipoLicenca;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.LicencaService;
import br.gov.ce.tce.srh.service.TipoLicencaService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;
import br.gov.ce.tce.srh.util.RelatorioUtil;

/**
* Use case : SRH_UC036_Lançar Licença
* 
* @since   : Nov 15, 2011, 10:03:00 AM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("licencaFinalizarListBean")
@Scope("session")
public class LicencaFinalizarListBean implements Serializable {

	static Logger logger = Logger.getLogger(LicencaFinalizarListBean.class);

	@Autowired
	private LicencaService licencaService;

	@Autowired
	private TipoLicencaService tipoLicencaService;

	@Autowired
	private FuncionalService funcionalService;

	@Autowired
	private RelatorioUtil relatorioUtil;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;
	
	// parametro da tela de consulta
	private String nome = new String();

	// entidades das telas
	private List<Licenca> lista;
	private Licenca entidade = new Licenca();
	private TipoLicenca tipoLicenca;

	//paginação
	private int count;
	private HtmlDataTable dataTable = new HtmlDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Licenca> pagedList = new ArrayList<Licenca>(); 
	private int flagRegistroInicial = -1;

	// combo
	private List<TipoLicenca> comboTipoLicenca;
	private List<TipoLicenca> tipoLicencaList;



	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			// validando campos da entidade
			//if ( getEntidade().getPessoal() == null )
				//throw new SRHRuntimeException("Selecione um funcionário.");

			if ( this.tipoLicenca != null ) {
			
				//count = licencaService.count(nome, this.tipoLicenca);
				count = licencaService.search(nome, this.tipoLicenca, getDataTable().getFirst(), getDataTable().getRows()).size();
			} else {
				//count = licencaService.count(getEntidade().getPessoal().getId());
				count = licencaService.search(nome, getDataTable().getFirst(), getDataTable().getRows()).size();
			}

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
			FacesUtil.addErroMessage("Erro de conexão com a base de dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "listar";
	}
	
	
	/**
	 * Finalizar Licença
	 * 
	 * @return
	 */
	public String finalizar() {

		try {
			
			//licencaService.excluir(entidade);
			licencaService.finalizar(entidade);
			
			FacesUtil.addInfoMessage("Licença finalizada com sucesso.");
			logger.info("Licença finalizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao excluir a Licença. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return consultar();
	}
	

	/**
	 * Combo Tipo Licenca
	 * 
	 * @return
	 */
	public List<TipoLicenca> getComboTipoLicenca() {

		try {

			if ( this.comboTipoLicenca == null) {
				tipoLicencaList = tipoLicencaService.findAll();
				this.comboTipoLicenca = new ArrayList<TipoLicenca>();
				for (TipoLicenca tp: tipoLicencaList) {
					if (tp.getId() == 1 || tp.getId() == 2)
						this.comboTipoLicenca.add(tp);
				}
			}

		} catch (Exception e) {
	      	FacesUtil.addErroMessage("Erro ao carregar o campo tipo de licença. Operação cancelada.");
	       	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoLicenca;
	}


	public String limpaTela() {
		setEntidade(new Licenca());
		return "listar";
	}
	
	/**
	 * Gets and Sets
	 */
	
	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public Licenca getEntidade() {return entidade;}
	public void setEntidade(Licenca entidade) {this.entidade = entidade;}
	
	public TipoLicenca getTipoLicenca() {return tipoLicenca;}
	public void setTipoLicenca(TipoLicenca tipoLicenca) {this.tipoLicenca = tipoLicenca;}

	public List<TipoLicenca> getTipoLicencaList() {return tipoLicencaList;}
	public void setTipoLicencaList(List<TipoLicenca> tipoLicencaList) {this.tipoLicencaList = tipoLicencaList;}

	public List<Licenca> getLista() {return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public boolean isPassouConsultar() {
		return passouConsultar;
	}


	public void setPassouConsultar(boolean passouConsultar) {
		this.passouConsultar = passouConsultar;
	}


	public HtmlForm getForm() {
		if (!passouConsultar) {
			setEntidade( new Licenca() );
			setTipoLicenca( null );
			this.nome = new String();
			this.lista = new ArrayList<Licenca>();
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
		pagedList = new ArrayList<Licenca>(); 
	}

	public HtmlDataTable getDataTable() {return dataTable;}
	public void setDataTable(HtmlDataTable dataTable) {this.dataTable = dataTable;}

	public PagedListDataModel getDataModel() {
		if( flagRegistroInicial != getDataTable().getFirst() ) {
			flagRegistroInicial = getDataTable().getFirst();
			
			if ( this.tipoLicenca != null) {
				setPagedList(licencaService.search(nome, this.tipoLicenca, getDataTable().getFirst(), getDataTable().getRows()));
			} else { 
				//setPagedList(licencaService.search(getEntidade().getPessoal().getId(), getDataTable().getFirst(), getDataTable().getRows()));
				setPagedList(licencaService.search(nome, getDataTable().getFirst(), getDataTable().getRows()));
			}

			if ( count != 0 ) {
				dataModel = new PagedListDataModel(getPagedList(), count);
			} else {
				limparListas();
			}
		}
		return dataModel;
	}

	public List<Licenca> getPagedList() {return pagedList;}
	public void setPagedList(List<Licenca> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO
}