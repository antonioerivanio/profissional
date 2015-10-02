package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

/**
* Use case : SRH_UC036_Lançar Licença
* 
* @since   : Nov 24, 2011, 10:09:22 AM
* @author  : robson.castro@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("licencaFormBean")
@Scope("session")
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

	// combos
	private List<TipoLicenca> comboTipoLicenca;
	private List<TipoPublicacao> comboTipoPublicacao;



	/**
	 * Realizar antes de carregar tela incluir
	 * 
	 * @return
	 */
	public String prepareIncluir() {
		limpar();
		return "incluirAlterar";
	}


	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {

		try {

			this.nrProcesso = getEntidade().getNrprocesso();
			this.nome = getEntidade().getPessoal().getNomeCompleto();

			Funcional funcional = funcionalService.getByPessoaAtivo( entidade.getPessoal().getId() );			
			if (funcional != null)
				this.matricula = funcional.getMatricula();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "incluirAlterar";
	}


	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {

			this.entidade.setNrprocesso(this.nrProcesso);
			licencaService.salvar(entidade);
			limpar();

			FacesUtil.addInfoMessage("Operação realizada com sucesso.");
			logger.info("Operação realizada com sucesso.");

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());		
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao salvar. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null; 
	}


	/**
	 * Combo Tipo Licenca
	 * 
	 * @return
	 */
	public List<TipoLicenca> getComboTipoLicenca() {

		try {

			if ( this.comboTipoLicenca == null)
				this.comboTipoLicenca = tipoLicencaService.findAll();

		} catch (Exception e) {
	      	FacesUtil.addErroMessage("Erro ao carregar o campo tipo de licença. Operação cancelada.");
	       	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoLicenca;
	}


	/**
	 * Combo Licenca Especial
	 */
	public void carregaLicencaEspecial() {
		getComboLicencaEspecial();
	}

	public List<LicencaEspecial> getComboLicencaEspecial() {

		List<LicencaEspecial> toReturn = new ArrayList<LicencaEspecial>();

        try {

        	exibirComboLicencaEspecial = false;
		
			if (getEntidade().getTipoLicenca() != null ) {
				
				getEntidade().setTipoLicenca( tipoLicencaService.getById( getEntidade().getTipoLicenca().getId() ) );
	
				if ( getEntidade().getTipoLicenca().isEspecial() ) {
	
		        	for ( LicencaEspecial entidade : licencaEspecialService.findByPessoalComSaldo( this.entidade.getPessoal().getId() )){
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
	

	/**
	 * Combo Tipo Publicacao
	 * 
	 * @return
	 */
	public List<TipoPublicacao> getComboTipoPublicacao() {
        
        try {

        	if ( this.comboTipoPublicacao == null )
        		this.comboTipoPublicacao = tipoPublicacaoService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo tipo publicação. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboTipoPublicacao;
	}


	/**
	 * Limpar form
	 */
	private void limpar() {

		setEntidade(new Licenca());
		this.nrProcesso = new String();

		this.matricula = new String();
		this.nome = new String();

		this.exibirComboLicencaEspecial = false;

		this.comboTipoLicenca = null;
		this.comboTipoPublicacao = null;
	}


	/**
	 * Gets and Sets
	 */
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

				if (!SRHUtils.validarProcesso( this.nrProcesso.substring(6,10) + this.nrProcesso.substring(0,5) + this.nrProcesso.substring(11,12) ) ) {
					throw new SRHRuntimeException("O Número do Processo informado é inválido.");
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

}