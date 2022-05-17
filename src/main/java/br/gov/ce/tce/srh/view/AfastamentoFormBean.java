package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AfastamentoESocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.enums.TipoLicencaEnum;
import br.gov.ce.tce.srh.exception.AfastamentoEsocialException;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AfastamentoESocialService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("afastamentoFormBean")
@Scope("view")
public class AfastamentoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(AfastamentoFormBean.class);

	@Autowired
	private AfastamentoESocialService afastamentoESocialService;
	
	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;
	

	// entidades das telas
	private List<Funcional> servidorEnvioList;
	private Funcional servidorFuncional;
	private AfastamentoESocial entidade = new AfastamentoESocial();
	
	boolean emEdicao = false;
	
	
	//paginação
	private UIDataTable dataTable = new UIDataTable();
	private List<AfastamentoESocial> pagedList = new ArrayList<AfastamentoESocial>();

	private List<Licenca> licencaList;
	
	@PostConstruct
	private void init() {
		AfastamentoESocial flashParameter = (AfastamentoESocial)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new AfastamentoESocial());
		this.servidorEnvioList = funcionalService.findServidoresEvento2230();
		
		if(getEntidade() != null && getEntidade().getFuncional() != null) {
			servidorFuncional = getEntidade().getFuncional();
			emEdicao = true;
		}
    }	
	
	public void consultar() {
		if(servidorFuncional != null) {
			try {				
				boolean possuiCargo = getPossuiCargo(servidorFuncional.getId());
				 
				//entidade.setFuncional(servidorFuncional);
				 Licenca licenca = entidade.getLicenca();
				 entidade = afastamentoESocialService.getEvento2230ByServidor(servidorFuncional, licenca, possuiCargo);
				 entidade.setLicenca(licenca);
				
			}
			catch (AfastamentoEsocialException e) {
				FacesUtil.addErroMessage(e.getMessage());
				logger.error(e.getMessage());
			}
			catch (Exception e) {		
				e.printStackTrace();
				FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}
		else {
			FacesUtil.addErroMessage("Selecione um servidor.");
		}
	}

	public void salvarEvento() {

		try {
			if(servidorFuncional != null) {
				afastamentoESocialService.salvar(entidade);
			}
			//setEntidade( new Admissao() );

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public String editar() {
		FacesUtil.setFlashParameter("entidade", getEntidade());  
        return "incluirAlterar";
	}

	public AfastamentoESocial getEntidade() { return entidade; }	
	public void setEntidade(AfastamentoESocial entidade) {
		this.entidade = entidade;
		}

	public List<Funcional> getServidorEnvioList() {
		return servidorEnvioList;
	}

	public void setServidorEnvioList(List<Funcional> servidorEnvioList) {
		this.servidorEnvioList = servidorEnvioList;
	}

	public Funcional getServidorFuncional() {
		return servidorFuncional;
	}

	public void setServidorFuncional(Funcional servidorFuncional) {
		this.servidorFuncional = servidorFuncional;
	}
	
	public boolean isEmEdicao() {
		return emEdicao;
	}

	public void setEmEdicao(boolean emEdicao) {
		this.emEdicao = emEdicao;
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	public List<AfastamentoESocial> getPagedList() {return pagedList;}
	public void setPagedList(List<AfastamentoESocial> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO
	
	/**
	 * @author erivanio.cruz
	 * @param idFuncional
	 * @return
	 */
	private boolean getPossuiCargo(Long idFuncional) {
		return representacaoFuncionalService.temAtivaByPessoal(idFuncional);
	}
	
	public List<Licenca> getLicencaList() { return licencaList; }
	public void setLicencaList(List<Licenca> licencaList) { this.licencaList = licencaList; }

	/** metodo ajax que carrega a lista de afastamento
	 * @author erivanio.cruz
	 */
	public void carregarAfastamentoListChange() {
		boolean possuiCargo = getPossuiCargo(servidorFuncional.getId());
		
		setLicencaList(afastamentoESocialService.getLicencaList(servidorFuncional, TipoLicencaEnum.getTodosCodigos()));
	}
}