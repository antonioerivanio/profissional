package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Cbo;
import br.gov.ce.tce.srh.domain.ClasseReferencia;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.TipoMovimento;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CboService;
import br.gov.ce.tce.srh.service.ClasseReferenciaService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.OcupacaoService;
import br.gov.ce.tce.srh.service.ReclassificacaoFuncionalService;
import br.gov.ce.tce.srh.service.TipoMovimentoService;
import br.gov.ce.tce.srh.service.TipoOcupacaoService;
import br.gov.ce.tce.srh.service.TipoPublicacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : SRH_UC042_Manter Reclassificação Ocupacional do Servidor
* 
* @since   : Fev 09, 2012, 10:00:00
* @author  : robson.castro@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("reclassificacaoOcupacionalFormBean")
@Scope("session")
public class ReclassificacaoOcupacionalFormBean implements Serializable {

	static Logger logger = Logger.getLogger(ReclassificacaoOcupacionalFormBean.class);

	@Autowired
	private ReclassificacaoFuncionalService reclassificacaoService;

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private TipoMovimentoService tipoMovimentoService;
	
	@Autowired
	private TipoPublicacaoService tipoPublicacaoService;
	
	@Autowired
	private TipoOcupacaoService tipoOcupacaoService;
	
	@Autowired
	private OcupacaoService ocupacaoService;
	
	@Autowired
	private ClasseReferenciaService classeReferenciaService;
	
	@Autowired
	private CboService cboService;


	// entidades das telas
	private Funcional entidade = new Funcional();
	private TipoOcupacao tipoOcupacao = new TipoOcupacao();
	
	private Cbo cbo1;
	private Cbo cbo2;
	private Cbo cbo3;
	
	//combos
	private List<TipoOcupacao> comboTipoOcupacao;
	private List<Ocupacao> comboCargoFuncao;
	private List<ClasseReferencia> comboClasseReferencia;
	private List<TipoMovimento> comboTipoMovimentoEntrada;
	private List<TipoMovimento> comboTipoMovimentoSaida;
	private List<Cbo> comboCBO1;
	private List<Cbo> comboCBO2;
	private List<Cbo> comboCBO3;
	private List<Cbo> comboCBO4;
	private List<TipoPublicacao> comboTipoPublicacao;

	private Boolean exibirTodosOsCampos = false;



	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {

		limpar();

		this.entidade = funcionalService.getById( this.entidade.getId() );
		this.tipoOcupacao = getEntidade().getOcupacao().getTipoOcupacao();
		
		// CBOs
		cbo1 = null; cbo2 = null; cbo3 = null; 
		if ( this.entidade.getCbo() != null) {
			this.cbo3 = cboService.getByCodigo( this.entidade.getCbo().getCodigo().substring(0, this.entidade.getCbo().getCodigo().length() - 2) );
			this.cbo2 = cboService.getByCodigo( this.cbo3.getCodigo().substring(0, this.cbo3.getCodigo().length() - 1) );
			this.cbo1 = cboService.getByCodigo( this.cbo2.getCodigo().substring(0, this.cbo2.getCodigo().length() - 1) );				
		}

		// exibir todos os campos?
		if ( this.entidade.getSaida() == null ) {
			exibirTodosOsCampos = true;
			entidade.setCbo(null);
			entidade.setOcupacao(null);
			entidade.setTipoMovimentoEntrada(null);
			entidade.setDescricaoNomeacao(null);
			entidade.setTipoMovimentoSaida(null);
			entidade.setDescricaoSaida(null);
			entidade.setDoeSaida(null);
			entidade.setSaida(null);
			entidade.setTipoPublicacaoSaida(null);
			entidade.setExercicio(null);
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

			reclassificacaoService.reclassificar(entidade);
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
	 * Combo Tipo Ocupacao
	 * 
	 * @return
	 */
	public List<TipoOcupacao> getComboTipoOcupacao() {

        try {

        	if ( this.comboTipoOcupacao == null )
        		this.comboTipoOcupacao = tipoOcupacaoService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo Tipo Ocupação. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboTipoOcupacao;
	}


	/**
	 * Combo Cargo/Funcao
	 * 
	 * @return
	 */
	public void carregaCargoFuncao() {
		comboCargoFuncao = null;
		comboClasseReferencia = null;
		getComboCargoFuncao();
	}

	public List<Ocupacao> getComboCargoFuncao() {

        try {

        	if ( this.tipoOcupacao != null && this.comboCargoFuncao == null ) {
        		this.comboCargoFuncao = ocupacaoService.findByTipoOcupacao(tipoOcupacao.getId());
	       	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo Cargo/Função. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboCargoFuncao;
	}


	/**
	 * Combo Classe/Referência
	 * 
	 * @return
	 */
	public void carregaClasseReferencia() {
		comboClasseReferencia = null;
		getComboClasseReferencia();
	}

	public List<ClasseReferencia> getComboClasseReferencia() {

		try {

        	if ( this.entidade.getOcupacao() != null && this.comboClasseReferencia == null )
        		this.comboClasseReferencia = classeReferenciaService.findByCargo(this.entidade.getOcupacao().getId());

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo Classe/Referência. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboClasseReferencia;
	}


	/**
	 * Combo Motivo Entrada
	 * 
	 * @return
	 */
	public List<TipoMovimento> getComboTipoMovimentoEntrada() {

		try {

			if( this.comboTipoMovimentoEntrada == null ) {

				if ( exibirTodosOsCampos == true ) { 
					this.comboTipoMovimentoEntrada = tipoMovimentoService.findByTipo(3l);
				} else { 
					this.comboTipoMovimentoEntrada = tipoMovimentoService.findAll();
				}

			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo Motivo Entrada. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoMovimentoEntrada;
	}


	/**
	 * Combo Motivo Saida
	 * 
	 * @return
	 */
	public List<TipoMovimento> getComboTipoMovimentoSaida() {

		try {

			if ( this.comboTipoMovimentoSaida == null ) {

				if ( exibirTodosOsCampos == true ) { 
					this.comboTipoMovimentoSaida = tipoMovimentoService.findByTipo(2l);
				} else {
					this.comboTipoMovimentoSaida = tipoMovimentoService.findAll();
				}

			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo Motivo Saida. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoMovimentoSaida;
	}


	/**
	 * Combo CBO 01
	 * 
	 * @return
	 */
	public List<Cbo> getComboCBO1() {

        try {

        	if( this.comboCBO1 == null )
        		this.comboCBO1 = cboService.findByNivel( 1L );

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo CBO 01. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboCBO1;
	}


	/**
	 * Combo CBO 02
	 * 
	 * @return
	 */
	public void carregaCbo2() {
		this.cbo2 = null;
		this.comboCBO2 = null;
		this.cbo3 = null;
		this.comboCBO3 = null;
		this.entidade.setCbo(null);
		this.comboCBO4 = null;
		getComboCBO2();
	}
	
	public List<Cbo> getComboCBO2() {

        try {

        	if( this.cbo1 != null && comboCBO2 == null ) {
        		this.cbo1 = cboService.getById( this.cbo1.getId() );
        		this.comboCBO2 = cboService.findByNivelCodigo( 2L, this.cbo1.getCodigo() );
        	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo CBO 02. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboCBO2;
	}


	/**
	 * Combo CBO 03
	 * 
	 * @return
	 */
	public void carregaCbo3() {
		this.cbo3 = null;
		this.comboCBO3 = null;
		this.entidade.setCbo(null);
		this.comboCBO4 = null;
		getComboCBO3();
	}
	
	public List<Cbo> getComboCBO3() {

        try {

        	if( this.cbo2 != null && this.comboCBO3 == null ) {
        		this.cbo2 = cboService.getById( this.cbo2.getId() );
        		this.comboCBO3 = cboService.findByNivelCodigo( 3l, this.cbo2.getCodigo() );
        	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo CBO 03. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboCBO3;
	}


	/**
	 * Combo CBO 04
	 * 
	 * @return
	 */
	public void carregaCbo4() {
		this.entidade.setCbo(null);
		this.comboCBO4 = null;
		getComboCBO4();
	}

	public List<Cbo> getComboCBO4() {

        try {

        	if( this.cbo3 != null && this.comboCBO4 == null ) {
        		this.cbo3 = cboService.getById( this.cbo3.getId() );
        		this.comboCBO4 = cboService.findByNivelCodigo( 4l, this.cbo3.getCodigo() );
        	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo CBO 04. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboCBO4;
	}
	

	/**
	 * Combo Tipo de Publicação 
	 * 
	 * @return
	 */
	public List<TipoPublicacao> getComboTipoPublicacao() {

        try {

        	if( this.comboTipoPublicacao == null )
        		this.comboTipoPublicacao = tipoPublicacaoService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo tipo de publicação. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboTipoPublicacao;
	}
	


	/**
	 * Limpar form
	 */
	private void limpar() {

		this.exibirTodosOsCampos = true;
		this.tipoOcupacao = new TipoOcupacao();

		this.cbo1 = null;
		this.cbo2 = null;
		this.cbo3 = null;

		comboTipoOcupacao = null;
		comboCargoFuncao = null;
		comboClasseReferencia = null;
		comboTipoMovimentoEntrada = null;
		comboTipoMovimentoSaida = null;
		comboCBO1 = null;
		comboCBO2 = null;
		comboCBO3 = null;
		comboCBO4 = null;
		comboTipoPublicacao = null;
	}


	/**
	 * Gets and Sets
	 */
	public Funcional getEntidade() {return entidade;}
    public void setEntidade(Funcional entidade) {this.entidade = entidade;}

	public TipoOcupacao getTipoOcupacao() {return tipoOcupacao;}
	public void setTipoOcupacao(TipoOcupacao tipoOcupacao) {this.tipoOcupacao = tipoOcupacao;}

	public Boolean getExibirTodosOsCampos() {return exibirTodosOsCampos;}
	public void setExibirTodosOsCampos(Boolean exibirTodosOsCampos) {this.exibirTodosOsCampos = exibirTodosOsCampos;}

	public Cbo getCbo1() {return cbo1;}
	public void setCbo1(Cbo cbo1) {this.cbo1 = cbo1;}

	public Cbo getCbo2() {return cbo2;}
	public void setCbo2(Cbo cbo2) {this.cbo2 = cbo2;}

	public Cbo getCbo3() {return cbo3;}
	public void setCbo3(Cbo cbo3) {this.cbo3 = cbo3;}

}