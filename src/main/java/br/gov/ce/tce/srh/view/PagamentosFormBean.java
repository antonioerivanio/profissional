package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.richfaces.component.UIDataTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.InformacaoPagamentos;
import br.gov.ce.tce.srh.domain.Pagamentos;
import br.gov.ce.tce.srh.domain.RemuneracaoTrabalhador;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.InformacaoPagamentosService;
import br.gov.ce.tce.srh.service.PagamentosEsocialService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("pagamentosFormBean")
@Scope("view")
public class PagamentosFormBean implements Serializable {

	static Logger logger = Logger.getLogger(PagamentosFormBean.class);

	@Autowired
	private PagamentosEsocialService pagamentosEsocialService;	
	@Autowired
	private FuncionalService funcionalService;
	@Autowired
	private InformacaoPagamentosService informacaoPagamentosService;	
	


	// entidades das telas
	private List<Funcional> servidorEnvioList;
	private List<Integer> comboAno;
	private Funcional servidorFuncional;
	private Pagamentos entidade = new Pagamentos();
	private Pagamentos pagamentosAnterior = new Pagamentos();
	boolean emEdicao = false;
	boolean emConsulta = false;
	private String anoReferencia;
	private String mesReferencia;
	private List<InformacaoPagamentos> informacaoPagamentosList;
	
	//paginação
	private UIDataTable dataTable = new UIDataTable();
	private List<RemuneracaoTrabalhador> pagedList = new ArrayList<RemuneracaoTrabalhador>();
	
	@PostConstruct
	private void init() {
		Pagamentos flashParameter = (Pagamentos)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Pagamentos());
		if(getEntidade() != null && getEntidade().getFuncional() != null) {
			servidorFuncional = getEntidade().getFuncional();
			pagamentosAnterior = entidade;
			String[] referencia = entidade.getPerApurCompetencia().split("-");			
			anoReferencia = referencia[0];
			mesReferencia = referencia[1];
			consultar();
			emEdicao = true;
		}
    }	
	
	public void carregaServidores() {
		this.servidorEnvioList = funcionalService.findServidorEvento1210(anoReferencia, mesReferencia);
	}
	public void consultar() {
		if(!mesReferencia.equalsIgnoreCase("0")  && !anoReferencia.equalsIgnoreCase("") && servidorFuncional != null) {
			try {
				entidade =  pagamentosEsocialService.getEventoS1210(mesReferencia, anoReferencia, servidorFuncional);				
				informacaoPagamentosList = informacaoPagamentosService.findInformacaoPagamentos(mesReferencia, anoReferencia, entidade, servidorFuncional.getId());	
				
				entidade.setPerApur(getPeriodoApuracaoPorDtPagamento(informacaoPagamentosList.get(0).getDtPgto()));
				entidade.setInformacaoPagamentos(informacaoPagamentosList);
	
				emConsulta = true;
			} catch (Exception e) {		
				e.printStackTrace();
				FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}
		else {
			FacesUtil.addErroMessage("É necessário informar o Ano, o Mês e o Beneficio.");
		}
	}

	private String getPeriodoApuracaoPorDtPagamento(Date dtPgto) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String periodoApuracao = dateFormat.format(dtPgto);
		return periodoApuracao;
	}

	public void salvarEvento() { 
		try {			
			if(emEdicao && pagamentosAnterior != null) {	
				pagamentosEsocialService.excluir(pagamentosAnterior);
			}
			
			if(servidorFuncional != null && entidade != null) {
				pagamentosEsocialService.salvar(entidade);
			}
			else {
				ArrayList<Pagamentos> pagamentosList = pagamentosEsocialService.geraPagamentosLote(mesReferencia, anoReferencia);
				pagamentosEsocialService.salvar(pagamentosList);				
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
	

	public Pagamentos getEntidade() {return entidade;}
	public void setEntidade(Pagamentos entidade) {this.entidade = entidade;}

	
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

	public Pagamentos getPagamentosAnterior() {
		return pagamentosAnterior;
	}

	public void setPagamentosAnterior(Pagamentos pagamentosAnterior) {
		this.pagamentosAnterior = pagamentosAnterior;
	}

	public List<InformacaoPagamentos> getInformacaoPagamentosList() {
		return informacaoPagamentosList;
	}

	public void setInformacaoPagamentosList(List<InformacaoPagamentos> informacaoPagamentosList) {
		this.informacaoPagamentosList = informacaoPagamentosList;
	}

	public boolean isEmEdicao() {
		return emEdicao;
	}

	public void setEmEdicao(boolean emEdicao) {
		this.emEdicao = emEdicao;
	}
	
	public boolean isEmConsulta() {
		return emConsulta;
	}

	public void setEmConsulta(boolean emConsulta) {
		this.emConsulta = emConsulta;
	}

	public UIDataTable getDataTable() {return dataTable;}
	public void setDataTable(UIDataTable dataTable) {this.dataTable = dataTable;}

	

	public List<RemuneracaoTrabalhador> getPagedList() {return pagedList;}
	public void setPagedList(List<RemuneracaoTrabalhador> pagedList) {this.pagedList = pagedList;}
	//FIM PAGINAÇÃO

}