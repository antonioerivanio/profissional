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

import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.InfoRemuneracaoPeriodoAnteriores;
import br.gov.ce.tce.srh.domain.InfoRemuneracaoPeriodoApuracao;
import br.gov.ce.tce.srh.domain.ItensRemuneracaoTrabalhador;
import br.gov.ce.tce.srh.domain.RemuneracaoOutraEmpresa;
import br.gov.ce.tce.srh.domain.RemuneracaoBeneficio;
import br.gov.ce.tce.srh.domain.RemuneracaoTrabalhador;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.DemonstrativosDeValoresService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.InfoRemuneracaoPeriodoAnterioresService;
import br.gov.ce.tce.srh.service.InfoRemuneracaoPeriodoApuracaoService;
import br.gov.ce.tce.srh.service.ItensRemuneracaoTrabalhadorService;
import br.gov.ce.tce.srh.service.RemuneracaoBeneficioEsocialService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("remuneracaoBeneficioFormBean")
@Scope("view")
public class RemuneracaoBeneficioFormBean implements Serializable {

	static Logger logger = Logger.getLogger(RemuneracaoBeneficioFormBean.class);

	@Autowired
	private RemuneracaoBeneficioEsocialService remuneracaoBeneficioEsocialService;	
	@Autowired
	private FuncionalService funcionalService;
	@Autowired
	private DemonstrativosDeValoresService demonstrativosDeValoresService;	
	@Autowired
	private InfoRemuneracaoPeriodoAnterioresService infoRemuneracaoPeriodoAnterioresService;
	@Autowired
	private InfoRemuneracaoPeriodoApuracaoService infoRemuneracaoPeriodoApuracaoService;
	@Autowired
	private ItensRemuneracaoTrabalhadorService itensRemuneracaoTrabalhadorService;	

	

	// entidades das telas
	private List<Funcional> servidorEnvioList;
	private List<Integer> comboAno;
	private Funcional servidorFuncional;
	private RemuneracaoBeneficio entidade = new RemuneracaoBeneficio();
	private RemuneracaoBeneficio remuneracaoBeneficioAnterior = new RemuneracaoBeneficio();
	boolean emEdicao = false;
	boolean emConsulta = false;
	private String anoReferencia;
	private String mesReferencia;
	private List<DemonstrativosDeValores> demonstrativosDeValoresList;
	private List<InfoRemuneracaoPeriodoAnteriores> infoRemuneracaoPeriodoAnterioresList;
	private List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList; 
	private List<RemuneracaoOutraEmpresa> remuneracaoOutraEmpresaList;
	private List<InfoRemuneracaoPeriodoApuracao> infoRemuneracaoPeriodoApuracaoList;
	
	//paginação
	private UIDataTable dataTable = new UIDataTable();
	private List<RemuneracaoTrabalhador> pagedList = new ArrayList<RemuneracaoTrabalhador>();
	
	@PostConstruct
	private void init() {
		RemuneracaoBeneficio flashParameter = (RemuneracaoBeneficio)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new RemuneracaoBeneficio());
		if(getEntidade() != null && getEntidade().getFuncional() != null) {
			servidorFuncional = getEntidade().getFuncional();
			remuneracaoBeneficioAnterior = entidade;
			String[] referencia = entidade.getPerApur().split("-");			
			anoReferencia = referencia[0];
			mesReferencia = referencia[1];
			consultar();
			emEdicao = true;
		}
    }	
	
	public void carregaServidores() {
		this.servidorEnvioList = funcionalService.findBeneficiosEventoS1207(anoReferencia, mesReferencia);
	}
	public void consultar() {
		if(!mesReferencia.equalsIgnoreCase("0")  && !anoReferencia.equalsIgnoreCase("") && servidorFuncional != null) {
			try {
				boolean isEstagiario = false;
				entidade =  remuneracaoBeneficioEsocialService.getEventoS1207(mesReferencia, anoReferencia, servidorFuncional);
				//remuneracaoOutraEmpresaList = remuneracaoOutraEmpresaService.findRemuneracaoOutraEmpresaRPA(mesReferencia, anoReferencia, entidade, BeneficioFuncional.getId());
				demonstrativosDeValoresList = demonstrativosDeValoresService.findDemonstrativosDeValoresBeneficio(mesReferencia, anoReferencia, entidade, servidorFuncional.getId());				
				infoRemuneracaoPeriodoAnterioresList = infoRemuneracaoPeriodoAnterioresService.findInfoRemuneracaoPeriodoAnteriores(mesReferencia, anoReferencia, demonstrativosDeValoresList, servidorFuncional.getId(), isEstagiario);
				infoRemuneracaoPeriodoApuracaoList = infoRemuneracaoPeriodoApuracaoService.findInfoRemuneracaoPeriodoApuracao(mesReferencia, anoReferencia, demonstrativosDeValoresList, servidorFuncional.getId(), isEstagiario);
				itensRemuneracaoTrabalhadorList = itensRemuneracaoTrabalhadorService.findS1207ByDemonstrativosDeValores(demonstrativosDeValoresList);
				
				entidade.setDmDev(demonstrativosDeValoresList);
				//entidade.setRemunOutrEmpr(remuneracaoOutraEmpresaList);
				
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

	public void salvarEvento() { 
		try {
			boolean isEstagiario = false;
			if(emEdicao && remuneracaoBeneficioAnterior != null) {	
				remuneracaoBeneficioEsocialService.excluir(remuneracaoBeneficioAnterior);
			}
			
			if(servidorFuncional != null && entidade != null) {
				remuneracaoBeneficioEsocialService.salvar(entidade);
			}
			else {
				ArrayList<RemuneracaoBeneficio> remuneracaoBeneficioList = remuneracaoBeneficioEsocialService.geraRemuneracaoBeneficioLote(mesReferencia, anoReferencia, isEstagiario);
				remuneracaoBeneficioEsocialService.salvar(remuneracaoBeneficioList);
				//remuneracaoBeneficioEsocialService.salvar(mesReferencia, anoReferencia, isEstagiario);
				//System.out.println("Gera todo mundo!");
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
	

	public RemuneracaoBeneficio getEntidade() {return entidade;}
	public void setEntidade(RemuneracaoBeneficio entidade) {this.entidade = entidade;}

	
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

	public List<DemonstrativosDeValores> getDemonstrativosDeValoresList() {
		return demonstrativosDeValoresList;
	}

	public void setDemonstrativosDeValoresList(List<DemonstrativosDeValores> demonstrativosDeValoresList) {
		this.demonstrativosDeValoresList = demonstrativosDeValoresList;
	}

	public List<InfoRemuneracaoPeriodoAnteriores> getInfoRemuneracaoPeriodoAnterioresList() {
		return infoRemuneracaoPeriodoAnterioresList;
	}

	public void setInfoRemuneracaoPeriodoAnterioresList(
			List<InfoRemuneracaoPeriodoAnteriores> infoRemuneracaoPeriodoAnterioresList) {
		this.infoRemuneracaoPeriodoAnterioresList = infoRemuneracaoPeriodoAnterioresList;
	}

	public List<ItensRemuneracaoTrabalhador> getItensRemuneracaoTrabalhadorList() {
		return itensRemuneracaoTrabalhadorList;
	}

	public void setItensRemuneracaoTrabalhadorList(List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList) {
		this.itensRemuneracaoTrabalhadorList = itensRemuneracaoTrabalhadorList;
	}

	public List<RemuneracaoOutraEmpresa> getRemuneracaoOutraEmpresaList() {
		return remuneracaoOutraEmpresaList;
	}

	public void setRemuneracaoOutraEmpresaList(List<RemuneracaoOutraEmpresa> remuneracaoOutraEmpresaList) {
		this.remuneracaoOutraEmpresaList = remuneracaoOutraEmpresaList;
	}

	public List<InfoRemuneracaoPeriodoApuracao> getInfoRemuneracaoPeriodoApuracaoList() {
		return infoRemuneracaoPeriodoApuracaoList;
	}

	public void setInfoRemuneracaoPeriodoApuracaoList(
			List<InfoRemuneracaoPeriodoApuracao> infoRemuneracaoPeriodoApuracaoList) {
		this.infoRemuneracaoPeriodoApuracaoList = infoRemuneracaoPeriodoApuracaoList;
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