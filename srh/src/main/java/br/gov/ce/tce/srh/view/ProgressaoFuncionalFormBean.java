package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.ClasseReferencia;
import br.gov.ce.tce.srh.domain.ReferenciaFuncional;
import br.gov.ce.tce.srh.domain.TipoMovimento;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.ClasseReferenciaService;
import br.gov.ce.tce.srh.service.ReferenciaFuncionalService;
import br.gov.ce.tce.srh.service.TipoMovimentoService;
import br.gov.ce.tce.srh.service.TipoPublicacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : SRH_UC031_Manter Progressão Funcional do Servidor
* 
* @since   : Jan 17, 2012, 18:28:00
* @author  : robson.castro@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("progressaoFuncionalFormBean")
@Scope("session")
public class ProgressaoFuncionalFormBean implements Serializable {

	static Logger logger = Logger.getLogger(ProgressaoFuncionalFormBean.class);

	@Autowired
	private ReferenciaFuncionalService  referenciaFuncionalService;
	
	@Autowired
	private TipoMovimentoService tipoMovimentoService;
	
	@Autowired
	private TipoPublicacaoService tipoPublicacaoService;

	@Autowired
	private ClasseReferenciaService classeReferenciaService;

	
	// entidades das telas
	private ReferenciaFuncional entidade = new ReferenciaFuncional();
	private Boolean exibirTodosOsCampos = true;
	private Boolean permiteSalvar = true;

	//Combos
	private List<ClasseReferencia> comboClasseReferencia;
	private List<TipoMovimento> comboTipoMovimento;
	private List<TipoPublicacao> comboTipoPublicacao;



	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {

		try {

			limpar();

			if ( this.entidade.getFim() == null) {
				exibirTodosOsCampos = true;
				permiteSalvar = true;
				getEntidade().setTipoMovimento(null);
				getEntidade().setInicio(null);
				getEntidade().setTipoPublicacao(null);
				getEntidade().setDataAto(null);
				getEntidade().setDoeAto(null);
				getEntidade().setDescricao( new String() );
			}
			
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

			referenciaFuncionalService.progressao( entidade );
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
	 * Combo Classe Referencia
	 * 
	 * @return
	 */
	public List<ClasseReferencia> getComboClasseReferencia() {

		try {

			if ( entidade.getFuncional() != null && comboClasseReferencia == null ) {

				this.comboClasseReferencia = new ArrayList<ClasseReferencia>();

        		for (ClasseReferencia entidade : classeReferenciaService.findByCargo(this.entidade.getFuncional().getOcupacao().getId())) {

    				if ( exibirTodosOsCampos == true ) {

    					this.comboClasseReferencia.add(entidade);

    				} else {
    					this.comboClasseReferencia.add(entidade);
    				}

				}

			} 

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo Classe/Referência. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboClasseReferencia;
	}


	/**
	 * Combo Tipo Movimento
	 * 
	 * @return
	 */
	public List<TipoMovimento> getComboTipoMovimento() {

        try {

        	if ( comboTipoMovimento == null ) {
        		if ( this.exibirTodosOsCampos ) {
        			comboTipoMovimento =  tipoMovimentoService.findByTipo(3L);	
        		} else {
        			comboTipoMovimento =  tipoMovimentoService.findAll();
        		}
        	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo Tipo Movimento. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return comboTipoMovimento;
	}


	/**
	 * Combo Tipo Publicacao
	 * 
	 * @return
	 */
	public List<TipoPublicacao> getComboTipoPublicacao() {

        try {

        	if ( comboTipoPublicacao == null )
        		comboTipoPublicacao = tipoPublicacaoService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo Tipo de Publicação. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return comboTipoPublicacao;
	}


	/**
	 * Limpar form
	 */
	private void limpar() {
		exibirTodosOsCampos = true;
		permiteSalvar = false;
		comboClasseReferencia = null;
		comboTipoMovimento = null;
		comboTipoPublicacao = null;
	}


	/**
	 * Gets and Sets
	 */
	public ReferenciaFuncional getEntidade() {return entidade;}
	public void setEntidade(ReferenciaFuncional entidade) {this.entidade = entidade;}

	public Boolean getExibirTodosOsCampos() {return exibirTodosOsCampos;}
	public void setExibirTodosOsCampos(Boolean exibirTodosOsCampos) {this.exibirTodosOsCampos = exibirTodosOsCampos;}


	public Boolean getPermiteSalvar() {return permiteSalvar;}
	public void setPermiteSalvar(Boolean permiteSalvar) {this.permiteSalvar = permiteSalvar;}
	
	

}
