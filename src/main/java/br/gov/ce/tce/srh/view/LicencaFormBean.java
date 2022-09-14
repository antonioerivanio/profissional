package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.domain.LicencaEspecial;
import br.gov.ce.tce.srh.domain.TipoLicenca;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.LicencaEspecialService;
import br.gov.ce.tce.srh.service.LicencaService;
import br.gov.ce.tce.srh.service.TipoLicencaService;
import br.gov.ce.tce.srh.service.TipoPublicacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

@SuppressWarnings("serial")
@Component("licencaFormBean")
@Scope("view")
public class LicencaFormBean implements Serializable {

	static Logger logger = Logger.getLogger(LicencaFormBean.class);

	@Autowired
	private LicencaService  licencaService;

	@Autowired
	private TipoLicencaService tipoLicencaService;
	
	@Autowired
	private TipoPublicacaoService tipoPublicacaoService;
	
	@Autowired
	private LicencaEspecialService licencaEspecialService;
	
	@Autowired
	private FuncionalService funcionalService;

	// entidades das telas
	private Licenca entidade = new Licenca();
	private String nrProcesso = new String();

	private String matricula = new String();
	private String nome = new String();

	private Boolean excluirTempoServico = false;
	private Boolean excluirRetribuicaoFinanceira = false;
	private Boolean exibirComboLicencaEspecial = false;
	private Boolean alterar = false;

	// combos
	private List<TipoLicenca> comboTipoLicenca;
	private List<TipoPublicacao> comboTipoPublicacao;
	
	@PostConstruct
	public void init() {
		
		Licenca flashParameter = (Licenca)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new Licenca());

		try {
			if (this.entidade.getId() != null) {				
				this.nrProcesso = getEntidade().getNrprocesso();
				this.nome = getEntidade().getPessoal().getNomeCompleto();
				
				Funcional funcional = funcionalService.getByPessoaAtivo( entidade.getPessoal().getId() );			
				if (funcional != null)
					this.matricula = funcional.getMatricula();
				
				alterar = true;
			}
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}
	
	public void salvar() {
		try {
			this.entidade.setNrprocesso(this.nrProcesso);			
			String mensagem = licencaService.salvar(entidade);			
			
			limpar();

			if (mensagem == null){				
				FacesUtil.addInfoMessage("Operação realizada com sucesso.");
				logger.info("Operação realizada com sucesso.");
			} else {				
				FacesUtil.addInfoMessage(mensagem);
				logger.info(mensagem);
			}
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());		
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
 
	}
	
	public List<TipoLicenca> getComboTipoLicenca() {

		try {
			if ( this.comboTipoLicenca == null) {
				this.comboTipoLicenca = tipoLicencaService.findAll();
			}
		} catch (Exception e) {
	      	FacesUtil.addErroMessage("Erro ao carregar o campo tipo de licença. Operação cancelada.");
	       	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoLicenca;
	}


	public void carregaLicencaEspecial() {
		getComboLicencaEspecial();
	}

	
	public List<LicencaEspecial> getComboLicencaEspecial() {

		List<LicencaEspecial> toReturn = new ArrayList<LicencaEspecial>();

        try {
        	exibirComboLicencaEspecial = false;
		
			if (getEntidade().getTipoLicenca() != null ) {
				
				getEntidade().setTipoLicenca( tipoLicencaService.getById( getEntidade().getTipoLicenca().getId() ) );
	
				if ( getEntidade().getTipoLicenca().isEspecial() && this.entidade.getPessoal() != null) {
					
					List<LicencaEspecial> licencas = licencaEspecialService.findByPessoalComSaldo( this.entidade.getPessoal().getId() );
	
		        	for ( LicencaEspecial entidade : licencas ){
		        		exibirComboLicencaEspecial = true;
		            	toReturn.add(entidade);	
		            }
	
				}

			}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo licença especial. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return toReturn;
	}
	
	
	public List<TipoPublicacao> getComboTipoPublicacao() {
        
        try {
        	if ( this.comboTipoPublicacao == null ) {
        		this.comboTipoPublicacao = tipoPublicacaoService.findAll();
        	}
        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo tipo publicação. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboTipoPublicacao;
	}

	
	private void limpar() {

		setEntidade(new Licenca());
		this.nrProcesso = new String();

		this.matricula = new String();
		this.nome = new String();

		this.excluirTempoServico = false;
		this.excluirRetribuicaoFinanceira = false;
		this.exibirComboLicencaEspecial = false;
		this.alterar = false;

		this.comboTipoLicenca = null;
		this.comboTipoPublicacao = null;
		
	}


	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				Funcional funcional = funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula );
				if (funcional != null) {
					getEntidade().setPessoal( funcional.getPessoal() );
					this.nome = getEntidade().getPessoal().getNomeCompleto();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matricula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}
	
	public Licenca getEntidade() {return entidade;}
	public void setEntidade(Licenca entidade) {this.entidade = entidade;}

	public String getNrProcesso() {return nrProcesso;}
	public void setNrProcesso(String nrProcesso) {
		
		if ( !nrProcesso.equals(this.nrProcesso) ) {
			
			this.nrProcesso = nrProcesso;

			try {

				if (this.nrProcesso != null && ! this.nrProcesso.equals("")) {
				
				/*	if (!SRHUtils.validarProcesso( this.nrProcesso.substring(6,10) + this.nrProcesso.substring(0,5) + this.nrProcesso.substring(11,12) ) ) {
						throw new SRHRuntimeException("O Número do Processo informado é inválido.");
					}*/
				
				}

			} catch (SRHRuntimeException e) {
				FacesUtil.addErroMessage(e.getMessage());
				logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro no campo processo. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
			
		}
		
	}

	public String getNome() {return this.nome;}
	public void setNome(String nome) {this.nome = nome;}
	
	public Boolean getExcluirTempoServico() {return excluirTempoServico;}
	public void setExcluirTempoServico(Boolean excluirTempoServico) {this.excluirTempoServico = excluirTempoServico;}

	public Boolean getExcluirRetribuicaoFinanceira() {return excluirRetribuicaoFinanceira;}
	public void setExcluirRetribuicaoFinanceira(Boolean excluirRetribuicaoFinanceira) {this.excluirRetribuicaoFinanceira = excluirRetribuicaoFinanceira;}

	public Boolean getExibirComboLicencaEspecial() {return exibirComboLicencaEspecial;}
	public void setExibirComboLicencaEspecial(Boolean exibirComboLicencaEspecial) {this.exibirComboLicencaEspecial = exibirComboLicencaEspecial;}
	
	public Boolean getAlterar() { return alterar; }
	public void setAlterar(Boolean alterar) { this.alterar = alterar; }


	public Boolean exibirContarDiasEmDobro() {
		if (entidade.getLicencaEspecial() != null)
			return true;
		
		return false;
	}
	
	
	public void atualizarCampos() {
		
		if (entidade.getContarDiasEmDobro() != null && entidade.getContarDiasEmDobro() > 0){
			this.entidade.setInicio(this.entidade.getDoe());
			this.entidade.setFim(this.entidade.getDoe());
			this.setExcluirRetribuicaoFinanceira(false);
			this.setExcluirTempoServico(false);
		} 
		
	}

}