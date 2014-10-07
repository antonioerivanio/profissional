package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.ClasseReferencia;
import br.gov.ce.tce.srh.domain.Escolaridade;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.Simbolo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.ClasseReferenciaService;
import br.gov.ce.tce.srh.service.EscolaridadeService;
import br.gov.ce.tce.srh.service.OcupacaoService;
import br.gov.ce.tce.srh.service.SimboloService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : SRH_UC006_Manter Classe e Referência do Cargo
* 
* @since   : Sep 14, 2011, 11:02:06 AM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("classeReferenciaFormBean")
@Scope("session")
public class ClasseReferenciaFormBean implements Serializable {

	static Logger logger = Logger.getLogger(ClasseReferenciaFormBean.class);

	@Autowired
	private ClasseReferenciaService classeReferenciaService;

	@Autowired
	private OcupacaoService ocupacaoService;

	@Autowired
	private SimboloService simboloService;

	@Autowired
	private EscolaridadeService escolaridadeService;


	// entidades das telas
	private ClasseReferencia entidade = new ClasseReferencia();
	private Ocupacao cargo;
	private boolean alterar;

	// combos
	private List<Ocupacao> comboCargo;
	private List<Simbolo> comboSimbolo;
	private List<Escolaridade> comboEscolaridade;



	/**
	 * Realizar antes de carregar tela incluir
	 * 
	 * @return
	 */
	public String prepareIncluir() {
		alterar = false;
		limpar(alterar);
		return "incluirAlterar";
	}


	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {

		try {
			alterar = true;
			limpar(alterar);
			this.cargo = this.getEntidade().getSimbolo().getOcupacao();

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
			
			alterar = false;
			classeReferenciaService.salvar(entidade);
			limpar(alterar);

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
	 * Combo Cargo
	 * 
	 * @return
	 */
	public List<Ocupacao> getComboCargo() {

		try {

			if ( this.comboCargo == null ) {
				this.comboCargo = new ArrayList<Ocupacao>();
				for (Ocupacao entidade : ocupacaoService.findAll()) {
					// somente os que nao forem cargo isolados
					if (!entidade.isCargoIsolado()) {
						this.comboCargo.add(entidade);
					}
				}
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo cargo. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboCargo;
	}


	/**
	 * Combo Simbolo
	 * 
	 * @return
	 */
	public void carregaSimbolo() {
		this.comboSimbolo = null;
	}

	public List<Simbolo> getComboSimbolo() {

		try {

			if ( getCargo() != null && comboSimbolo == null ) {
				this.comboSimbolo =  simboloService.findByOcupacao( getCargo().getId() );
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo simbolo. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboSimbolo;
	}


	/**
	 * Combo Escolaridade
	 * 
	 * @return
	 */
	public List<Escolaridade> getComboEscolaridade() {

		try {

			if ( this.comboEscolaridade == null )
				this.comboEscolaridade = escolaridadeService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar a escolaridade. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboEscolaridade;
	}


	/**
	 * Limpar form
	 */
	private void limpar(boolean alterar) {

		if (!alterar)
			setEntidade( new ClasseReferencia() );
		setCargo( null );

		this.comboCargo = null;
		this.comboSimbolo = null;
		this.comboEscolaridade = null;
	}


	/**
	 * Gets and Sets
	 */
	public ClasseReferencia getEntidade() {return entidade;}
	public void setEntidade(ClasseReferencia entidade) {this.entidade = entidade;}

	public Ocupacao getCargo() {return cargo;}
	public void setCargo(Ocupacao cargo) {this.cargo = cargo;}

}
