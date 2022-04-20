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
	private List<Funcional> beneficiarioEnvioList;
	private Funcional beneficiarioFuncional;
	private Beneficiario entidade = new Beneficiario();
	private List<DependenteEsocial> dependentesList;
	private String tpInsc;
	private String nrInsc;
	boolean emEdicao = false;
	
	//paginação
	private UIDataTable dataTable = new UIDataTable();
	private List<Beneficiario> pagedList = new ArrayList<Beneficiario>();
	
	@PostConstruct
	private void init() {
		Beneficiario flashParameter = (Beneficiario)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Beneficiario());
		this.beneficiarioEnvioList = funcionalService.findBeneficiariosEvento2400();
		if(getEntidade() != null && getEntidade().getFuncional() != null) {
			dependentesList = dependenteEsocialTCEService.findDependenteEsocialByIdfuncional(getEntidade().getFuncional().getId());
			beneficiarioFuncional = getEntidade().getFuncional();
			emEdicao = true;
		}
		
    }	
	
	public void consultar() {
		if(beneficiarioFuncional != null) {
			try {		
				tpInsc = "1";
				nrInsc = "09499757";
				entidade =  beneficiariooEsocialService.getEventoS2400ByServidor(beneficiarioFuncional);
				dependentesList = dependenteEsocialTCEService.findByIdfuncional(beneficiarioFuncional.getId());
	
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
			if(beneficiarioFuncional != null) {
				beneficiariooEsocialService.salvar(entidade);
				
				if(dependentesList != null && !dependentesList.isEmpty()) {
					dependenteEsocialTCEService.salvar(dependentesList);
				}
			}
			//setEntidade( new Beneficiario() );

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
		consultar();
		FacesUtil.setFlashParameter("entidade", getEntidade());        
        return "incluirAlterar";
	}
	

	public Beneficiario getEntidade() {return entidade;}
	public void setEntidade(Beneficiario entidade) {this.entidade = entidade;}


	public List<Funcional> getBeneficiarioEnvioList() {
		return beneficiarioEnvioList;
	}

	public void setBeneficiarioEnvioList(List<Funcional> beneficiarioEnvioList) {
		this.beneficiarioEnvioList = beneficiarioEnvioList;
	}

	public Funcional getBeneficiarioFuncional() {
		return beneficiarioFuncional;
	}	

	public String getTpInsc() {
		return tpInsc;
	}

	public void setTpInsc(String tpInsc) {
		this.tpInsc = tpInsc;
	}

	public String getNrInsc() {
		return nrInsc;
	}

	public void setNrInsc(String nrInsc) {
		this.nrInsc = nrInsc;
	}

	public void setBeneficiarioFuncional(Funcional beneficiarioFuncional) {
		this.beneficiarioFuncional = beneficiarioFuncional;
	}

	public List<DependenteEsocial> getDependentesList() {
		return dependentesList;
	}

	public void setDependentesList(List<DependenteEsocial> dependentesList) {
		this.dependentesList = dependentesList;
	}
	

	public boolean isEmEdicao() {
		return emEdicao;
	}

	public void setEmEdicao(boolean emEdicao) {
		this.emEdicao = emEdicao;
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	

	public List<Beneficiario> getPagedList() {return pagedList;}
	public void setPagedList(List<Beneficiario> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}