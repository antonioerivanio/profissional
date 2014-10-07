package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.domain.TipoMovimento;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.ExoneracaoFuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.service.TipoMovimentoService;
import br.gov.ce.tce.srh.service.TipoPublicacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : SRH_UC034_Manter Exoneração do Servidor
* 
* @since   : Dez 19, 2011, 17:59:02 AM
* @author  : wesllhey.holanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("exoneracaoFormBean")
@Scope("session")
public class ExoneracaoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(ExoneracaoFormBean.class);

	@Autowired
	private ExoneracaoFuncionalService exoneracaoService;

	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;
	
	@Autowired
	private TipoMovimentoService tipoMovimentoService;
	
	@Autowired
	private TipoPublicacaoService tipoPublicacaoService;

	// entidades das telas
	private Funcional funcional;
	private RepresentacaoFuncional representacao;

	// combos
	List<TipoPublicacao> comboTipoPublicacao;
	List<TipoMovimento> comboTipoMovimento;



	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {

			// exonerar cargo efetivo
			if ( this.funcional != null)
				exoneracaoService.exonerar(funcional);

			// exonerar representacao
			if ( this.representacao != null)
				representacaoFuncionalService.exonerar(representacao);

			this.funcional = null;
			this.representacao = null;

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
	 * Combo Tipo Publicacao
	 * 
	 * @return
	 */
	public List<TipoPublicacao> getComboTipoPublicacao() {

		try {

			if ( this.comboTipoPublicacao == null)
				this.comboTipoPublicacao = tipoPublicacaoService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo tipo publicação. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoPublicacao;
	}


	/**
	 * Combo Tipo Movimento Saida
	 * 
	 * @return
	 */
	public List<TipoMovimento> getComboTipoMovimentoSaida() {

		try {

			if ( this.comboTipoMovimento == null)
				this.comboTipoMovimento = tipoMovimentoService.findByTipo(2l);

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo tipo de movimento saida. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoMovimento;
	}


	/**
	 * Gets and Sets
	 */
	public Funcional getFuncional() {return funcional;}
	public void setFuncional(Funcional funcional) {this.funcional = funcional;}

	public RepresentacaoFuncional getRepresentacao() {return representacao;}
	public void setRepresentacao(RepresentacaoFuncional representacao) {this.representacao = representacao;}

}