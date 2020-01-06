package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

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

@SuppressWarnings("serial")
@Component("classeReferenciaFormBean")
@Scope("view")
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
	
	@PostConstruct
	private void init() {		
		ClasseReferencia flashParameter = (ClasseReferencia)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new ClasseReferencia());
		
		if (entidade.getId() == null) {
			alterar = false;
			limpar(alterar);
		} else {
			try {
				alterar = true;
				limpar(alterar);
				this.cargo = this.getEntidade().getSimbolo().getOcupacao();

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro ao carregar os dados. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
			
		}
    }
	
	public void salvar() {

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
	}

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

	private void limpar(boolean alterar) {

		if (!alterar)
			setEntidade( new ClasseReferencia() );
		setCargo( null );

		this.comboCargo = null;
		this.comboSimbolo = null;
		this.comboEscolaridade = null;
	}

	public ClasseReferencia getEntidade() {return entidade;}
	public void setEntidade(ClasseReferencia entidade) {this.entidade = entidade;}

	public Ocupacao getCargo() {return cargo;}
	public void setCargo(Ocupacao cargo) {this.cargo = cargo;}

}
