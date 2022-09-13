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
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.domain.Admissao;
import br.gov.ce.tce.srh.domain.DependenteEsocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AdmissaoEsocialService;
import br.gov.ce.tce.srh.service.DependenteEsocialTCEService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("admissaoFormBean")
@Scope("view")
public class AdmissaoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(AdmissaoFormBean.class);

	@Autowired
	private AdmissaoEsocialService admissaoEsocialService;
	@Autowired
	private DependenteEsocialTCEService dependenteEsocialTCEService;
	
	@Autowired
	private FuncionalService funcionalService;
	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;
	

	// entidades das telas
	private List<Funcional> servidorEnvioList;
	private Funcional servidorFuncional;
	private Admissao entidade = new Admissao();
	private Admissao admissaoAnterior = new Admissao();	
	private List<DependenteEsocial> dependentesList;
	boolean emEdicao = false;
	boolean isRetificacao = false;
	String reciboEventoS2200 = "";
	
	//paginação
	private UIDataTable dataTable = new UIDataTable();
	private List<Admissao> pagedList = new ArrayList<Admissao>();
	
	@PostConstruct
	private void init() {
		Admissao flashParameter = (Admissao)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Admissao());
		this.servidorEnvioList = funcionalService.findServidoresEvento2200();
		if(getEntidade() != null && getEntidade().getFuncional() != null) {
			admissaoAnterior = 	getEntidade();		
			servidorFuncional = getEntidade().getFuncional();
			emEdicao = true;
			consultar();						
		}
    }	
	
	public void consultar() {
		if(servidorFuncional != null) {
			try {
				boolean possuiCargo = representacaoFuncionalService.temAtivaByPessoal(servidorFuncional.getId());
				entidade =  admissaoEsocialService.getEventoS2200ByServidor(servidorFuncional, possuiCargo);
				dependentesList = dependenteEsocialTCEService.findByIdfuncional(servidorFuncional.getId());
				if(emEdicao) {
					reciboEventoS2200 = admissaoEsocialService.findReciboEventoS2200(admissaoAnterior.getReferencia());
					if(reciboEventoS2200!= null && !reciboEventoS2200.equals("")) {
						isRetificacao = true;
					}
				}
	
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

	@Transactional
	public void salvarEvento() {

		try {
			if(servidorFuncional != null) {
				if(emEdicao) {
					if(admissaoAnterior != null) {
						List<DependenteEsocial> dependentesListExcluir = dependenteEsocialTCEService.findDependenteEsocialByIdfuncional(admissaoAnterior.getFuncional().getId());
						if(dependentesListExcluir != null && !dependentesListExcluir.isEmpty()) {
							dependenteEsocialTCEService.excluirAll(dependentesListExcluir);
						}
						admissaoEsocialService.excluir(admissaoAnterior);
					}
					  	 	
				}
				if(isRetificacao) {
					entidade.setReferencia(entidade.getReferencia()+"_RET"+reciboEventoS2200);
				}
				admissaoEsocialService.salvar(entidade);
				
				if(dependentesList != null && !dependentesList.isEmpty()) {
					dependenteEsocialTCEService.salvar(dependentesList);
				}
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
		consultar();
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
	

	public boolean isRetificacao() {
		return isRetificacao;
	}

	public void setRetificacao(boolean isRetificacao) {
		this.isRetificacao = isRetificacao;
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	

	public List<Admissao> getPagedList() {return pagedList;}
	public void setPagedList(List<Admissao> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}