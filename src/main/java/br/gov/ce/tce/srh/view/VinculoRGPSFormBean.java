package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import com.ibm.icu.math.BigDecimal;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.domain.VinculoRGPS;
import br.gov.ce.tce.srh.enums.TipoVinculoRGPS;
import br.gov.ce.tce.srh.enums.TipodeEmpresa;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.service.AuthenticationService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.PessoaJuridicaService;
import br.gov.ce.tce.srh.service.VinculoRGPSService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("vinculoRGPSFormBean")
@Scope("view")
public class VinculoRGPSFormBean implements Serializable {

	static Logger logger = Logger.getLogger(VinculoRGPSFormBean.class);

	@Autowired
	private VinculoRGPSService vinculoRGPSService;
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private AuthenticationService authenticationService;

	private VinculoRGPS entidade = new VinculoRGPS();

	private String matricula = new String();
	private String matriculaConsulta = new String();
	private String nome = new String();
	private String valorOutraEmpresaStr = new String();
	

	private Date inicial;
	private Date fim;

	private boolean bloquearDatas = false;
	private boolean alterar = false;

	private List<PessoaJuridica> comboEmpresasCadastradas;
	private PessoaJuridica pessoaJuridica;
	private TipoVinculoRGPS codigoTipoVinculoRGPS;
	
	private Integer pagina = 1;

	@PostConstruct
	public void init() {
		VinculoRGPS flashParameter = (VinculoRGPS) FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new VinculoRGPS());
		this.pagina = (Integer) FacesUtil.getFlashParameter("pagina");
		this.matriculaConsulta = (String) FacesUtil.getFlashParameter("matricula");
		this.comboEmpresasCadastradas = pessoaJuridicaService.findAll();
		this.pessoaJuridica = entidade.getPessoaJuridica();
		this.codigoTipoVinculoRGPS = TipoVinculoRGPS.getByCodigo(entidade.getTipoEsocial());
		if(entidade.getValorOutraEmpresa() != null) {
			this.valorOutraEmpresaStr = entidade.getValorOutraEmpresa().toString();	
		}
		
		try {
			if(this.entidade.getId() != null) {					
				this.alterar = true;
				this.matricula = entidade.getFuncional().getMatricula();
				this.nome = entidade.getFuncional().getPessoal().getNomeCompleto();	
				this.inicial = entidade.getInicio();
				this.fim = entidade.getFim();							
			}
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}	
	
	public void salvar() {
		
		if(verificaVinculoRGPS()) {
			try {	
				this.entidade.setInicio(this.inicial);
				this.entidade.setFim(this.fim);
				this.entidade.setValorOutraEmpresa(SRHUtils.valorMonetarioStringParaBigDecimal2(valorOutraEmpresaStr));
				this.entidade.setPessoaJuridica(pessoaJuridica);
				this.entidade.setTipoEsocial(codigoTipoVinculoRGPS.getCodigo());			
				this.entidade.setUsuario(authenticationService.getUsuarioLogado());
				this.entidade.setDataAlteracao(SRHUtils.getDataHoraAtual());
				
				vinculoRGPSService.salvar(entidade);
				
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
		
	}
	
	private boolean verificaVinculoRGPS() {
		if (pessoaJuridica == null ) {
			FacesUtil.addErroMessage("A Empresa Vinculada é um campo obrigatório.");
			return false;
		} 
		else if(codigoTipoVinculoRGPS == null) {
			FacesUtil.addErroMessage("Outra fonte remuneração é um campo obrigatório.");
			return false;
		}
		else if(this.inicial == null || this.inicial.before(entidade.getFuncional().getExercicio())) {
			FacesUtil.addErroMessage("A Data Inicio deve ser maior ou igual a data exercício funcional.");
			return false;
		}
		else if(valorOutraEmpresaStr == null || valorOutraEmpresaStr == "" || SRHUtils.valorMonetarioStringParaBigDecimal2(valorOutraEmpresaStr).equals(new BigDecimal(0))) {
			FacesUtil.addErroMessage("O Valor deve ser informado.");
			return false;
		}
		return true;
	}

	public String voltar() {
		if (entidade.getId() != null) {
			if (!alterar) {
				pagina = 1;				
			}
			
			matriculaConsulta = matricula;
		}
		
		FacesUtil.setFlashParameter("matricula", matriculaConsulta);
		FacesUtil.setFlashParameter("pagina", pagina);
        return "listar";
	}
	
	public List<PessoaJuridica> getComboEmpresasCadastradas() {

		try {

			if ( this.comboEmpresasCadastradas == null ) {
			    List<TipodeEmpresa> tipodeEmpresas = new ArrayList<TipodeEmpresa>();
			    tipodeEmpresas.add(TipodeEmpresa.PLANOS_SAUDE);
				this.comboEmpresasCadastradas = pessoaJuridicaService.findAllNotTipo(tipodeEmpresas);
			}

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de publicação. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboEmpresasCadastradas;
	}	
	
	public VinculoRGPS getEntidade() { return entidade; }
	public void setEntidade(VinculoRGPS entidade) { this.entidade = entidade; }

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( matricula != null && (this.matricula == null || !this.matricula.equals(matricula)) ) {
			this.matricula = matricula;

			try {

				getEntidade().setFuncional( funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula ));
				if ( getEntidade().getFuncional() != null ) {
					this.nome = getEntidade().getFuncional().getNomeCompleto();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matrícula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public Date getInicial() {return inicial;}
	public void setInicial(Date inicial) {
		this.inicial = inicial;
	}

	public Date getFim() { return fim; }
	public void setFim(Date fim) {
		this.fim = fim;
	}	
	
	public PessoaJuridica getPessoaJuridica() {
		return pessoaJuridica;
	}

	public void setPessoaJuridica(PessoaJuridica pessoaJuridica) {
		this.pessoaJuridica = pessoaJuridica;
	}

	public TipoVinculoRGPS getCodigoTipoVinculoRGPS() {
		return codigoTipoVinculoRGPS;
	}

	public void setCodigoTipoVinculoRGPS(TipoVinculoRGPS codigoTipoVinculoRGPS) {
		this.codigoTipoVinculoRGPS = codigoTipoVinculoRGPS;
	}
	
	public String getValorOutraEmpresaStr() {
		return valorOutraEmpresaStr;
	}

	public void setValorOutraEmpresaStr(String valorOutraEmpresaStr) {
		this.valorOutraEmpresaStr = valorOutraEmpresaStr;
	}

	public List<TipoVinculoRGPS> getComboTipoVinculoRGPS() {
		return Arrays.asList(TipoVinculoRGPS.values());
	}

	public boolean isBloquearDatas() {return bloquearDatas;}
	public boolean isAlterar() {return alterar;}
			
	public void atualizaBloqueioDeDatas(){
		
		this.inicial = null;
		this.fim = null;
		this.bloquearDatas = false;
	}
}
