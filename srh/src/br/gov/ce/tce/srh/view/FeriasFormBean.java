package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Ferias;
import br.gov.ce.tce.srh.domain.TipoFerias;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.FeriasService;
import br.gov.ce.tce.srh.service.FuncionalService;
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
	
	private Ferias entidade = new Ferias();

	private String matricula = new String();
	private String nome = new String();

	private Date inicial;
	private Date fim;

	private boolean bloquearDatas = false;
	private boolean alterar = false;

	private List<TipoFerias> comboTipoFerias;
	private List<TipoPublicacao> comboTipoPublicacao;

	public String prepareIncluir() {
		limpar();
		return "incluirAlterar";
	}

	public String prepareAlterar() {

		this.alterar = true;

		try {
			
			this.matricula = entidade.getFuncional().getMatricula();
			this.nome = entidade.getFuncional().getPessoal().getNomeCompleto();

			this.inicial = entidade.getInicio();
			this.fim = entidade.getFim();
			
			if(entidade.getTipoFerias().consideraSomenteQtdeDias())
				bloquearDatas = true;
			else
				bloquearDatas = false;

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "incluirAlterar";
	}	
	
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
		atualizaQtdeDias();
	}

	public Date getFim() { return fim; }
	public void setFim(Date fim) {
		this.fim = fim;
		atualizaQtdeDias();
	}	
	
	public boolean isBloquearDatas() {return bloquearDatas;}
	public boolean isAlterar() {return alterar;}
			
	public void atualizaBloqueioDeDatas(){
		
		this.inicial = null;
		this.fim = null;
		this.entidade.setQtdeDias(null);
		this.bloquearDatas = false;
		
		if ( entidade.getTipoFerias() != null && entidade.getTipoFerias().consideraSomenteQtdeDias() ) {
			this.bloquearDatas = true;
		}	
	}
	
	private void atualizaQtdeDias(){
		try {
			
			if( this.inicial != null && this.fim != null )
				entidade.setQtdeDias( (long) SRHUtils.dataDiff( this.inicial, this.fim ));
		
		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu um erro na atualização da quantidade de dias. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
	}
	
}
