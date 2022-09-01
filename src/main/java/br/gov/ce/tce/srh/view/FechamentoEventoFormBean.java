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
import br.gov.ce.tce.srh.domain.FechamentoEventoEsocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.DependenteEsocialTCEService;
import br.gov.ce.tce.srh.service.FechamentoEventoEsocialService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("fechamentoEventoFormBean")
@Scope("view")
public class FechamentoEventoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(FechamentoEventoFormBean.class);

	@Autowired
	private FechamentoEventoEsocialService fechamentoEventoEsocialService;
	@Autowired
	private DependenteEsocialTCEService dependenteEsocialTCEService;
	
	@Autowired
	private FuncionalService funcionalService;
	
	

	// entidades das telas
	private List<Funcional> fechamentoEventoEsocialList;
	private Funcional servidorFuncional;
	private FechamentoEventoEsocial entidade = new FechamentoEventoEsocial();

	private String tpInsc;
	private String nrInsc;
	boolean emEdicao = false;
	
	//paginação
	private UIDataTable dataTable = new UIDataTable();
	private List<FechamentoEventoEsocial> pagedList = new ArrayList<FechamentoEventoEsocial>();
	
	@PostConstruct
	private void init() {
		FechamentoEventoEsocial flashParameter = (FechamentoEventoEsocial)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new FechamentoEventoEsocial());
		this.fechamentoEventoEsocialList = funcionalService.findBeneficiariosEvento2410();
		if(getEntidade() != null && getEntidade().getFuncional() != null) {
			servidorFuncional = getEntidade().getFuncional();
			emEdicao = true;
		}
		
    }	
	
	public void consultar() {
		if(servidorFuncional != null) {
			try {		
				tpInsc = "1";
				nrInsc = "09499757";
				entidade =  fechamentoEventoEsocialService.getEventoS1299ByServidor(servidorFuncional);				
	
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
			if(servidorFuncional != null) {
				fechamentoEventoEsocialService.salvar(entidade);			
			}			

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
	

	public FechamentoEventoEsocial getEntidade() {return entidade;}
	public void setEntidade(FechamentoEventoEsocial entidade) {this.entidade = entidade;}


	public List<Funcional> getBeneficiarioEnvioList() {
		return fechamentoEventoEsocialList;
	}

	public void setBeneficiarioEnvioList(List<Funcional> beneficiarioEnvioList) {
		this.fechamentoEventoEsocialList = beneficiarioEnvioList;
	}

	public Funcional getbeneficiarioFuncional() {
		return servidorFuncional;
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
		this.servidorFuncional = beneficiarioFuncional;
	}

	public Funcional getBeneficiarioFuncional() {
		return servidorFuncional;
	}

	public boolean isEmEdicao() {
		return emEdicao;
	}

	public void setEmEdicao(boolean emEdicao) {
		this.emEdicao = emEdicao;
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	

	public List<FechamentoEventoEsocial> getPagedList() {return pagedList;}
	public void setPagedList(List<FechamentoEventoEsocial> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}