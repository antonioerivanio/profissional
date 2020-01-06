package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.domain.TipoLicenca;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.LicencaService;
import br.gov.ce.tce.srh.service.TipoLicencaService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.PagedListDataModel;

@SuppressWarnings("serial")
@Component("licencaFinalizarListBean")
@Scope("view")
public class LicencaFinalizarListBean implements Serializable {

	static Logger logger = Logger.getLogger(LicencaFinalizarListBean.class);

	@Autowired
	private LicencaService licencaService;

	@Autowired
	private TipoLicencaService tipoLicencaService;

	// parametro da tela de consulta
	private String nome = new String();

	// entidades das telas
	private List<Licenca> lista;
	private Licenca entidade = new Licenca();
	private TipoLicenca tipoLicenca;

	//paginação
	private int count;
	private UIDataTable dataTable = new UIDataTable();
	private PagedListDataModel dataModel = new PagedListDataModel();
	private List<Licenca> pagedList = new ArrayList<Licenca>(); 
	private int flagRegistroInicial = -1;

	// combo
	private List<TipoLicenca> comboTipoLicenca;
	private List<TipoLicenca> tipoLicencaList;

	public void consultar() {
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

		} catch (SRHRuntimeException e) {
			limparListas();
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			limparListas();
			FacesUtil.addErroMessage("Erro de conexão com a base de dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public void finalizar() {

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

		consultar();
	}

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

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public Licenca getEntidade() {return entidade;}
	public void setEntidade(Licenca entidade) {this.entidade = entidade;}
	
	public TipoLicenca getTipoLicenca() {return tipoLicenca;}
	public void setTipoLicenca(TipoLicenca tipoLicenca) {this.tipoLicenca = tipoLicenca;}

	public List<TipoLicenca> getTipoLicencaList() {return tipoLicencaList;}
	public void setTipoLicencaList(List<TipoLicenca> tipoLicencaList) {this.tipoLicencaList = tipoLicencaList;}

	public List<Licenca> getLista() {return lista;}

	//PAGINAÇÃO
	private void limparListas() {
		dataTable = new UIDataTable();
		dataModel = new PagedListDataModel();
		pagedList = new ArrayList<Licenca>(); 
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

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