package br.gov.ce.tce.srh.view;

import java.io.Serializable;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.LicencaEspecial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.LicencaEspecialService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : SRH_UC035_Manter Período de Licença Especial
* 
* @since   : Out 27, 2011, 10:59:22 AM
* @author  : wesllhey.holanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("licencaEspecialFormBean")
@Scope("session")
public class LicencaEspecialFormBean implements Serializable {

	static Logger logger = Logger.getLogger(PessoalCursoGraduacaoFormBean.class);

	@Autowired
	private LicencaEspecialService licencaEspecialService;

	@Autowired
	private FuncionalService funcionalService;


	// entidades das telas
	private LicencaEspecial entidade = new LicencaEspecial();	

	private String matricula;
	private String nome = new String();

	private Long anoInicial;
	private Long anoFinal;

	private boolean alterar = false;



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

		this.alterar = true;

		this.nome = entidade.getPessoal().getNomeCompleto();
		this.anoInicial = entidade.getAnoinicial();
		this.anoFinal = entidade.getAnofinal();

		try {

			Funcional funcional = funcionalService.getByPessoaAtivo( entidade.getPessoal().getId() );
			
			if (funcional != null)
				this.matricula = funcional.getMatricula();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
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

			this.entidade.setAnoinicial( this.anoInicial );
			this.entidade.setAnofinal( this.anoFinal );

			licencaEspecialService.salvar(entidade);
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
	 * Limpar dados
	 */	
	private void limpar() {

		this.alterar = false;
		setEntidade( new LicencaEspecial() );

		this.matricula = new String();
		this.nome = new String();

		this.anoInicial = null;
		this.anoFinal = null;
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
				if ( funcional != null ) {
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
	
	public String getNome() {return this.nome;}
	public void setNome(String nome) {this.nome = nome;}

	
	public Long getAnoInicial() {return anoInicial;}
	public void setAnoInicial(Long anoInicial) {
		this.anoInicial = anoInicial;
		entidade.setQtdedias(null);

		try {

			if ( this.anoFinal != null )
				entidade.setQtdedias( licencaEspecialService.calcularQtdeDias( this.anoInicial, this.anoFinal ) );

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro no campo ano inicial. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public Long getAnoFinal() {return anoFinal;}
	public void setAnoFinal(Long anoFinal) {
		this.anoFinal = anoFinal;
		entidade.setQtdedias(null);

		try {

			if ( this.anoInicial != null )
				entidade.setQtdedias( licencaEspecialService.calcularQtdeDias( this.anoInicial , this.anoFinal) );

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro no campo ano final. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public LicencaEspecial getEntidade() {return entidade;}
    public void setEntidade(LicencaEspecial entidade) {this.entidade = entidade;}

	public boolean isAlterar() {return alterar;}

}