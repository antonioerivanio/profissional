package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Ferias;
import br.gov.ce.tce.srh.domain.Parametro;
import br.gov.ce.tce.srh.domain.TipoFerias;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FeriasService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.ParametroService;
import br.gov.ce.tce.srh.service.TipoFeriasService;
import br.gov.ce.tce.srh.service.TipoPublicacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * Use case : SRH_UC037_Manter Férias
 * 
 * @since   : Jan 10, 2012, 5:33:11 PM
 * @author  : joel.barbosa@ivia.com.br
 *
 */
@SuppressWarnings("serial")
@Component("feriasFormBean")
@Scope("session")
public class FeriasFormBean implements Serializable {

	static Logger logger = Logger.getLogger(FeriasFormBean.class);

	@Autowired
	private FeriasService feriasService;

	@Autowired
	private FuncionalService funcionalService;

	@Autowired
	private TipoFeriasService tipoFeriasService;
	
	@Autowired
	private TipoPublicacaoService tipoPublicacaoService;

	@Autowired
	private ParametroService parametroService;
	

	//endidade das telas
	private Ferias entidade = new Ferias();

	private String matricula = new String();
	private String nome = new String();

	private Date inicial;
	private Date fim;

	private boolean feriasEdicao = false;
	private boolean bloquearDatas = false;
	private boolean alterar = false;

	// combos
	private List<TipoFerias> comboTipoFerias;
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

		this.alterar = true;

		try {
			Parametro p = parametroService.getByNome("FERIASEDICAO");
			//Se for SIM = 1
			if (p.getValor().equals("1")) {
				feriasEdicao = true;
			}else{
				feriasEdicao = false;
			}
			this.matricula = entidade.getFuncional().getMatricula();
			this.nome = entidade.getFuncional().getPessoal().getNomeCompleto();

			this.inicial = entidade.getInicio();
			this.fim = entidade.getFim();
			// ressalvas ou em dobro
			if ( (entidade.getTipoFerias().getId().equals(4l) || entidade.getTipoFerias().getId().equals(5l)) & !feriasEdicao ){
				this.bloquearDatas = true;
			}else{
				this.bloquearDatas = false;
			}

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

			this.entidade.setInicio(this.inicial);
			this.entidade.setFim(this.fim);

			feriasService.salvar(entidade);
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
	 * Combo Tipo de Ferias
	 * 
	 * @return
	 */
	public List<TipoFerias> getComboTipoFerias() {

		try {

			if ( this.comboTipoFerias == null )
				this.comboTipoFerias = tipoFeriasService.findAll();

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de ferias. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoFerias;
	}

	
	/**
	 * Combo Classe Referencia
	 * 
	 * @return
	 */
	public void atualizaCampos(ValueChangeEvent event) {

		getEntidade().setTipoFerias( (TipoFerias) event.getNewValue() );

		if( (entidade.getTipoFerias().getId().equals(4l) || entidade.getTipoFerias().getId().equals(5l) ) & !feriasEdicao ) {
			this.bloquearDatas = true;
		} else {
			this.bloquearDatas = false;
		}

	}


	/**
	 * Combo Tipo de Publicacao
	 * 
	 * @return
	 */
	public List<TipoPublicacao> getComboTipoPublicacao() {

		try {

			if ( this.comboTipoPublicacao == null )
				this.comboTipoPublicacao = tipoPublicacaoService.findAll();

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de publicação. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoPublicacao;
	}


	/**
	 * Limpar form
	 */
	private void limpar() {

		this.alterar = false;

		setEntidade(new Ferias());
		getEntidade().setPeriodo(1l);

		this.matricula = new String();
		this.nome = new String();
		
		this.inicial = null;
		this.fim = null;
		this.bloquearDatas = false;

		this.comboTipoFerias = null;
		this.comboTipoPublicacao = null;
	}


	/**
	 * Gets and Sets
	 */
	public Ferias getEntidade() { return entidade; }
	public void setEntidade(Ferias entidade) { this.entidade = entidade; }

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				getEntidade().setFuncional( funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula ));
				if ( getEntidade().getFuncional() != null ) {
					this.nome = getEntidade().getFuncional().getNomeCompleto();
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

	public Date getInicial() {return inicial;}
	public void setInicial(Date inicial) {

		this.inicial = inicial;

		try {

			if ( this.inicial != null && this.fim != null)
				entidade.setQtdeDias( (long) SRHUtils.dataDiff( this.inicial, this.fim ));

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

			if (this.inicial != null && this.fim != null )
				entidade.setQtdeDias( (long) SRHUtils.dataDiff( this.inicial, this.fim ) );

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro no campo data final. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}

	
	public boolean isBloquearDatas() {return bloquearDatas;}
	public boolean isAlterar() {return alterar;}


	public boolean isFeriasEdicao() {
		return feriasEdicao;
	}


	public void setFeriasEdicao(boolean feriasEdicao) {
		this.feriasEdicao = feriasEdicao;
	}
	
}
