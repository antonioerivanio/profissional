package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.CodigoCategoria;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalCedido;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.enums.EmpresaAreaSaude;
import br.gov.ce.tce.srh.service.CodigoCategoriaService;
import br.gov.ce.tce.srh.service.FuncionalCedidoService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.PessoaJuridicaService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("funcionalCedidoFormBean")
@Scope("view")
public class FuncionalCedidoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(FuncionalCedidoFormBean.class);
	@Autowired
	private FuncionalCedidoService funcionalCedidoService;
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private CodigoCategoriaService codigoCateogiraService;
	
	@Autowired
	private AfastamentoFormBean afastamentoFormBean;
	
	@Autowired
	private NomeacaoServidorFormBean nomeacaoServidorFormBean;
	
	@Autowired
	private LoginBean loginBean;
	
	private FuncionalCedido entidade;	
	private PessoaJuridica pessoaJuridica;
	
	private boolean bloquearDatas = false;
	private boolean alterar = false;
	private boolean isExibidoTodosOsCampos = false;

	private List<CodigoCategoria> comboCodCateg;
	private List<PessoaJuridica> comboEmpresasCadastradas;
	private List<Funcional> servidorEnvioList;	


	@PostConstruct
	public void init() {
		FuncionalCedido flashParameter = null;
	
		if(FacesUtil.getFlashParameter("entidade") != null && FacesUtil.getFlashParameter("entidade") instanceof FuncionalCedido) {
			flashParameter = (FuncionalCedido) FacesUtil.getFlashParameter("entidade");
		}

		setEntidade(flashParameter != null ? flashParameter : new FuncionalCedido());
		
		inicializarEntidade();
		
		try {
			if(this.entidade.getId() != null) {					
				this.alterar = true;							
			}
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}		
	}	
	

	public void salvar() {		
		try {
			if(funcionalCedidoService.isOk(entidade)) {
				entidade.setIdUsuarioAtualizacao(loginBean.getUsuarioLogado().getId());				
				entidade.setTpRegTrab(entidade.getFuncional().getRegime().intValue());
				entidade.setTpRegPrev(entidade.getFuncional().getPrevidencia().intValue());
				entidade.setFuncional(getFuncionalByServidorFuncional());
				entidade.setPessoaJuridica(pessoaJuridica);				
				entidade.setCodigoCategoria(entidade.getCodigoCategoria().getCodigoCategoraByList(getComboCodCateg()));
				
				funcionalCedidoService.salvar(entidade);
				
				FacesUtil.addInfoMessage("Registro salvar com sucesso!");
			}
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao Salvar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		inicializarEntidade();		
	}	
	
	private void inicializarEntidade() {
		getEntidade().setFuncional(new Funcional());		
		getEntidade().setDtAdmCed(null);
		getEntidade().setMatricOrig(null);
		getEntidade().setMatricOrig(null);		
		pessoaJuridica = new PessoaJuridica();	
		getEntidade().setPessoaJuridica(pessoaJuridica);
		getEntidade().setCodigoCategoria(null);
		getAfastamentoFormBean().setServidorFuncional(null);

		this.servidorEnvioList = funcionalService.findServidoresEvento2230();
	}
	
	public void setCnpjPessoaJuridicaChange() {
		int index = nomeacaoServidorFormBean.getComboEmpresasCadastradas().indexOf(entidade.getPessoaJuridica());
		PessoaJuridica pessoaJuridicaEncontrada = nomeacaoServidorFormBean.getComboEmpresasCadastradas().get(index);
		setPessoaJuridica(pessoaJuridicaEncontrada);
	}	


	public String voltar() {	
        return "listar";
	}
	/***
	 * Buscar na lista dados do servidor selecionado
	 * @return funcional encontrado
	 */
	private Funcional getFuncionalByServidorFuncional() {
		int index = afastamentoFormBean.getServidorEnvioList().indexOf(afastamentoFormBean.getServidorFuncional());
		Funcional funcionalEncontrado = afastamentoFormBean.getServidorEnvioList().get(index);
		return funcionalEncontrado;
	}
	
	public List<PessoaJuridica> getComboEmpresasCadastradas() {

		try {
		  if ( this.comboEmpresasCadastradas == null )
            this.comboEmpresasCadastradas = pessoaJuridicaService.findAllByTipo(EmpresaAreaSaude.NAO);

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de publicação. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboEmpresasCadastradas;
	}	
	
	public List<CodigoCategoria> getComboCodCateg(){
		try {
			this.comboCodCateg = codigoCateogiraService.findAll();
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo Codigo Categoria. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		return this.comboCodCateg;
	}


	public List<Funcional> getServidorEnvioList() { return servidorEnvioList; }
	public void setServidorEnvioList(List<Funcional> servidorEnvioList) { this.servidorEnvioList = servidorEnvioList;}

	public FuncionalCedido getEntidade() { return entidade; }
	public void setEntidade(FuncionalCedido entidade) { this.entidade = entidade; }

	public PessoaJuridica getPessoaJuridica() { return pessoaJuridica; }
	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) { this.pessoaJuridica = pessoaJuridica; }	

	public boolean isBloquearDatas() {return bloquearDatas;}
	public boolean isAlterar() {return alterar;}	
	public boolean getIsExibidoTodosOsCampos() { return isExibidoTodosOsCampos;}

	public AfastamentoFormBean getAfastamentoFormBean() { return afastamentoFormBean; }
	public NomeacaoServidorFormBean getNomeacaoServidorFormBean() { return nomeacaoServidorFormBean; }

}
