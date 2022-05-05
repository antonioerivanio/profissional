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

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.RemuneracaoTrabalhador;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.RemuneracaoTrabalhadorEsocialService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("remuneracaoTrabalhadorFormBean")
@Scope("view")
public class RemuneracaoTrabalhadorFormBean implements Serializable {

	static Logger logger = Logger.getLogger(RemuneracaoTrabalhadorFormBean.class);

	@Autowired
	private RemuneracaoTrabalhadorEsocialService remuneracaoTrabalhadorEsocialService;	
	@Autowired
	private FuncionalService funcionalService;

	

	// entidades das telas
	private List<Funcional> servidorEnvioList;
	private List<Integer> comboAno;
	private Funcional servidorFuncional;
	private RemuneracaoTrabalhador entidade = new RemuneracaoTrabalhador();
	boolean emEdicao = false;
	private String anoReferencia;
	private String mesReferencia;
	
	//paginação
	private UIDataTable dataTable = new UIDataTable();
	private List<RemuneracaoTrabalhador> pagedList = new ArrayList<RemuneracaoTrabalhador>();
	
	@PostConstruct
	private void init() {
		RemuneracaoTrabalhador flashParameter = (RemuneracaoTrabalhador)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new RemuneracaoTrabalhador());
		if(getEntidade() != null && getEntidade().getFuncional() != null) {
			servidorFuncional = getEntidade().getFuncional();
			emEdicao = true;
		}
    }	
	
	public void carregaServidores() {
		this.servidorEnvioList = funcionalService.findServidoresEvento1200(anoReferencia, mesReferencia);
	}
	public void consultar() {
		if(servidorFuncional != null) {
			try {
				entidade =  remuneracaoTrabalhadorEsocialService.getEventoS1200ByServidor(servidorFuncional);	
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

	public void salvarEvento() { 
		try {
			
			remuneracaoTrabalhadorEsocialService.salvar(mesReferencia, anoReferencia, servidorFuncional);
				
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
	
	public List<Integer> getComboAno() {

		try {

			if ( this.comboAno == null )
				this.comboAno = SRHUtils.popularComboAno(2);

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar o combo do Ano. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboAno;
	}	
	

	public RemuneracaoTrabalhador getEntidade() {return entidade;}
	public void setEntidade(RemuneracaoTrabalhador entidade) {this.entidade = entidade;}

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

	
	public String getAnoReferencia() {
		return anoReferencia;
	}

	public void setAnoReferencia(String anoReferencia) {
		this.anoReferencia = anoReferencia;
	}

	public String getMesReferencia() {
		return mesReferencia;
	}

	public void setMesReferencia(String mesReferencia) {
		this.mesReferencia = mesReferencia;
	}

	public boolean isEmEdicao() {
		return emEdicao;
	}

	public void setEmEdicao(boolean emEdicao) {
		this.emEdicao = emEdicao;
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	

	public List<RemuneracaoTrabalhador> getPagedList() {return pagedList;}
	public void setPagedList(List<RemuneracaoTrabalhador> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}