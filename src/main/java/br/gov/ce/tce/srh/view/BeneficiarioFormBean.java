package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Beneficiario;
import br.gov.ce.tce.srh.domain.DependenteEsocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.BeneficiarioEsocialService;
import br.gov.ce.tce.srh.service.DependenteEsocialTCEService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("beneficiarioFormBean")
@Scope("view")
public class BeneficiarioFormBean implements Serializable {

	static Logger logger = Logger.getLogger(BeneficiarioFormBean.class);

	@Autowired
	private BeneficiarioEsocialService beneficiariooEsocialService;
	@Autowired
	private DependenteEsocialTCEService dependenteEsocialTCEService;
	
	@Autowired
	private FuncionalService funcionalService;
	
	

	// entidades das telas
	private List<Funcional> servidorEnvioList;
	private Funcional servidorFuncional;
	private Beneficiario entidade = new Beneficiario();
	private List<DependenteEsocial> dependentesList;
	
	//paginação
	private UIDataTable dataTable = new UIDataTable();
	private List<Beneficiario> pagedList = new ArrayList<Beneficiario>();
	
	@PostConstruct
	private void init() {
		Beneficiario flashParameter = (Beneficiario)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Beneficiario());
		this.servidorEnvioList = funcionalService.findServidoresEvento2200();
		
    }	
	
	public void consultar() {
		if(servidorFuncional != null) {
			try {				
				entidade =  beneficiariooEsocialService.getEventoS2400ByServidor(servidorFuncional);
				dependentesList = dependenteEsocialTCEService.findByIdfuncional(servidorFuncional.getId());
	
			} catch (Exception e) {		
				e.printStackTrace();
				FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}
		else {
			FacesUtil.addErroMessage("Selecione um servidor.");
		}
	}

	public void salvar() {

		try {
			if(entidade.getId().equals(new Long(0))) {
				beneficiariooEsocialService.salvar(entidade);
				
				if(dependentesList != null && dependentesList.isEmpty()) {
					dependenteEsocialTCEService.salvar(dependentesList);
				}
			}
			setEntidade( new Beneficiario() );

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
	

	public Beneficiario getEntidade() {return entidade;}
	public void setEntidade(Beneficiario entidade) {this.entidade = entidade;}

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

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	

	public List<Beneficiario> getPagedList() {return pagedList;}
	public void setPagedList(List<Beneficiario> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}