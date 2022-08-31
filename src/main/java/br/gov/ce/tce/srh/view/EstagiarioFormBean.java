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

import br.gov.ce.tce.srh.domain.Admissao;
import br.gov.ce.tce.srh.domain.DependenteEsocial;
import br.gov.ce.tce.srh.domain.EstagiarioESocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.DependenteEsocialTCEService;
import br.gov.ce.tce.srh.service.EstagiarioESocialService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("estagiarioFormBean")
@Scope("view")
public class EstagiarioFormBean implements Serializable {

	static Logger logger = Logger.getLogger(EstagiarioFormBean.class);

	@Autowired
	private EstagiarioESocialService estagiarioESocialService;
	@Autowired
	private DependenteEsocialTCEService dependenteEsocialTCEService;
	
	@Autowired
	private FuncionalService funcionalService;
	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;
	

	// entidades das telas
	private List<Funcional> servidorEnvioList;
	private Funcional estagiarioFuncional;
	private EstagiarioESocial entidade = new EstagiarioESocial();
	private EstagiarioESocial estagiarioESocialAnterior = new EstagiarioESocial();
	private List<DependenteEsocial> dependentesList;
	boolean emEdicao = false;
	
	//paginação
	private UIDataTable dataTable = new UIDataTable();
	private List<EstagiarioESocial> pagedList = new ArrayList<EstagiarioESocial>();
	
	@PostConstruct
	private void init() {
		EstagiarioESocial flashParameter = (EstagiarioESocial)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new EstagiarioESocial());
		this.servidorEnvioList = funcionalService.findEstagiarioservidoresEvento2300();
		if(getEntidade() != null && getEntidade().getFuncional() != null) {
			//estagiarioESocialAnterior = getEntidade();		
			estagiarioFuncional = getEntidade().getFuncional();
			consultar();			
			emEdicao = true;
		}
		
    }	
	
	public void consultar() {
		if(estagiarioFuncional != null) {
			try {
				entidade =  estagiarioESocialService.getEventoS2300ByEstagiario(estagiarioFuncional);
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
				
              /*
               * if(emEdicao) { if(estagiarioESocialAnterior != null) { List<DependenteEsocial>
               * dependentesListExcluir =
               * dependenteEsocialTCEService.findDependenteEsocialByIdfuncional(estagiarioESocialAnterior.
               * getFuncional().getId()); if(dependentesListExcluir != null && !dependentesListExcluir.isEmpty())
               * { dependenteEsocialTCEService.excluirAll(dependentesListExcluir); }
               * estagiarioESocialService.excluir(estagiarioESocialAnterior); }
               * 
               * }
               */
				estagiarioESocialService.salvar(entidade);
				
				if(dependentesList != null && !dependentesList.isEmpty()) {
					dependenteEsocialTCEService.salvar(dependentesList);
				}
			}
				

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
	

	public EstagiarioESocial getEntidade() {return entidade;}
	public void setEntidade(EstagiarioESocial entidade) {this.entidade = entidade;}

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

	

	public List<EstagiarioESocial> getPagedList() {return pagedList;}
	public void setPagedList(List<EstagiarioESocial> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}