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

import br.gov.ce.tce.srh.domain.DependenteEsocial;
import br.gov.ce.tce.srh.domain.EstatutarioESocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.DependenteEsocialTCEService;
import br.gov.ce.tce.srh.service.EstatutarioESocialService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("estatutarioFormBean")
@Scope("view")
public class EstatutarioFormBean implements Serializable {

	static Logger logger = Logger.getLogger(EstatutarioFormBean.class);

	@Autowired
	private EstatutarioESocialService estatutarioESocialService;
	
	@Autowired
	private DependenteEsocialTCEService dependenteEsocialTCEService;
	
	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;
	

	// entidades das telas
	private List<Funcional> servidorEnvioList;
	private Funcional estagiarioFuncional;
	private EstatutarioESocial entidade = new EstatutarioESocial();
	private List<DependenteEsocial> dependentesList;
	boolean emEdicao = false;
	
	//paginação
	private UIDataTable dataTable = new UIDataTable();
	private List<EstatutarioESocial> pagedList = new ArrayList<EstatutarioESocial>();
	
	@PostConstruct
	private void init() {
		EstatutarioESocial flashParameter = (EstatutarioESocial)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new EstatutarioESocial());
		this.servidorEnvioList = funcionalService.findEstagiarioservidoresEvento2300();
		if(getEntidade() != null && getEntidade().getFuncional() != null) {
			dependentesList = dependenteEsocialTCEService.findDependenteEsocialByIdfuncional(getEntidade().getFuncional().getId());
			estagiarioFuncional = getEntidade().getFuncional();
			emEdicao = true;
		}
		
    }	
	
	public void consultar() {
		if(estagiarioFuncional != null) {
			try {
				entidade =  estatutarioESocialService.getEventoS2300ByEstatutario(estagiarioFuncional);
				dependentesList = dependenteEsocialTCEService.findByIdfuncional(estagiarioFuncional.getId());
	
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
			if(estagiarioFuncional != null) {
				estatutarioESocialService.salvar(entidade);
				
				if(dependentesList != null && !dependentesList.isEmpty()) {
					dependenteEsocialTCEService.salvar(dependentesList);
				}
			}
			setEntidade( new EstatutarioESocial() );

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			e.printStackTrace();
		}
	}
	
	public String editar() {
		consultar();
		FacesUtil.setFlashParameter("entidade", getEntidade());        
        return "incluirAlterar";
	}
	

	public EstatutarioESocial getEntidade() {return entidade;}
	public void setEntidade(EstatutarioESocial entidade) {this.entidade = entidade;}

	public List<Funcional> getServidorEnvioList() {
		return servidorEnvioList;
	}

	public void setServidorEnvioList(List<Funcional> servidorEnvioList) {
		this.servidorEnvioList = servidorEnvioList;
	}

	

	public FuncionalService getFuncionalService() {
		return funcionalService;
	}

	public void setFuncionalService(FuncionalService funcionalService) {
		this.funcionalService = funcionalService;
	}

	public Funcional getEstagiarioFuncional() {
		return estagiarioFuncional;
	}

	public void setEstagiarioFuncional(Funcional estagiarioFuncional) {
		this.estagiarioFuncional = estagiarioFuncional;
	}
	

	public boolean isEmEdicao() {
		return emEdicao;
	}

	public void setEmEdicao(boolean emEdicao) {
		this.emEdicao = emEdicao;
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	

	public List<EstatutarioESocial> getPagedList() {return pagedList;}
	public void setPagedList(List<EstatutarioESocial> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}