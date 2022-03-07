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
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AdmissaoESocialTCEService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("admissaoFormBean")
@Scope("view")
public class AdmissaoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(AdmissaoFormBean.class);

	@Autowired
	private AdmissaoESocialTCEService admissaoESocialTCEService;
	
	@Autowired
	private FuncionalService funcionalService;
	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;
	

	// entidades das telas
	private List<Funcional> servidorEnvioList;
	private Funcional servidorFuncional;
	private Admissao entidade = new Admissao();
	
	//paginação
	private UIDataTable dataTable = new UIDataTable();
	private List<Admissao> pagedList = new ArrayList<Admissao>();
	
	@PostConstruct
	private void init() {
		Admissao flashParameter = (Admissao)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Admissao());
		this.servidorEnvioList = funcionalService.findServidoresEvento2200();
		
    }	
	
	public void consultar() {
		if(servidorFuncional != null) {
			try {
				boolean possuiCargo = representacaoFuncionalService.temAtivaByPessoal(servidorFuncional.getId());
				entidade =  admissaoESocialTCEService.getEventoS2200ByServidor(servidorFuncional, possuiCargo);
	
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
				admissaoESocialTCEService.salvar(entidade);
			}
			setEntidade( new Admissao() );

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
	

	public Admissao getEntidade() {return entidade;}
	public void setEntidade(Admissao entidade) {this.entidade = entidade;}

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

	

	public List<Admissao> getPagedList() {return pagedList;}
	public void setPagedList(List<Admissao> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}