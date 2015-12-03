package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Acrescimo;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AcrescimoService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * Use case : SRH_UC048_Manter Acrecimos do Servidor
 * 
 * @since   : Apr 17, 2012, 10:00:29 PM
 * @author  : robstownholanda@ivia.com.br
 *
 */
@SuppressWarnings("serial")
@Component("acrescimoFormBean")
@Scope("session")
public class AcrescimoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(AcrescimoFormBean.class);

	@Autowired
	private AcrescimoService acrescimoService;

	@Autowired
	private FuncionalService funcionalService;


	//endidade das telas
	private Acrescimo entidade = new Acrescimo();

	private String matricula = new String();
	private String nome = new String();

	private Date inicio;
	private Date fim;

	private boolean bloquearDatas = false;
	private boolean alterar = false;

	// combos



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

		try {

			Funcional funcional = funcionalService.getByPessoaAtivo( getEntidade().getPessoal().getId() );
			this.matricula = funcional.getMatricula();
			this.nome = entidade.getPessoal().getNomeCompleto();

			this.inicio = entidade.getInicio();
			this.fim = entidade.getFim();

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

			this.entidade.setInicio(this.inicio);
			this.entidade.setFim(this.fim);

			acrescimoService.salvar(entidade);
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
	 * Limpar form
	 */
	private void limpar() {

		this.alterar = false;

		setEntidade(new Acrescimo());

		this.matricula = new String();
		this.nome = new String();
		
		this.inicio = null;
		this.fim = null;
		this.bloquearDatas = false;
	}


	/**
	 * Gets and Sets
	 */
	public Acrescimo getEntidade() { return entidade; }
	public void setEntidade(Acrescimo entidade) { this.entidade = entidade; }

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				Funcional funcional = funcionalService.getCpfAndNomeByMatriculaAtiva( matricula );
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

	public String getNome() { return nome; }
	public void setNome(String nome) { this.nome = nome; }

	public Date getInicio() {return inicio;}
	public void setInicio(Date inicio) {

		this.inicio = inicio;

		try {			
			if (this.inicio != null){
				
				entidade.setQtdeDias(null);
				
				if (this.fim != null)
					entidade.setQtdeDias( (long) SRHUtils.dataDiff( this.inicio, this.fim ));
			}
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro no campo data inicial. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	public Date getFim() { return fim; }
	public void setFim(Date fim) {

		this.fim = fim; 

		try {			
			if (this.fim != null){
				
				entidade.setQtdeDias(null);
				
				if (this.inicio != null){
					entidade.setQtdeDias( (long) SRHUtils.dataDiff( this.inicio, this.fim ) );
				}
			}
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro no campo data final. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	
	public void apagaDatas(){
		this.inicio = null;
		this.fim = null;
	}
	
	public boolean isBloquearDatas() {return bloquearDatas;}
	public boolean isAlterar() {return alterar;}
	
}
