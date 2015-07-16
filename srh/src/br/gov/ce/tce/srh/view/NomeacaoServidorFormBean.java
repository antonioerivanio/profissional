package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Cbo;
import br.gov.ce.tce.srh.domain.ClasseReferencia;
import br.gov.ce.tce.srh.domain.EspecialidadeCargo;
import br.gov.ce.tce.srh.domain.Folha;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.LeiIncorporacao;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.OrientacaoCargo;
import br.gov.ce.tce.srh.domain.Situacao;
import br.gov.ce.tce.srh.domain.TipoMovimento;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.domain.Vinculo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Entidade;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.EntidadeService;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.CboService;
import br.gov.ce.tce.srh.service.ClasseReferenciaService;
import br.gov.ce.tce.srh.service.EspecialidadeCargoService;
import br.gov.ce.tce.srh.service.FolhaService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.NomeacaoFuncionalService;
import br.gov.ce.tce.srh.service.OcupacaoService;
import br.gov.ce.tce.srh.service.OrientacaoCargoService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.service.SituacaoService;
import br.gov.ce.tce.srh.service.TipoMovimentoService;
import br.gov.ce.tce.srh.service.TipoOcupacaoService;
import br.gov.ce.tce.srh.service.TipoPublicacaoService;
import br.gov.ce.tce.srh.service.VinculoService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : SRH_UC030_Manter Nomeação do Servidor
* 
* @since   : Dez 05, 2011, 10:10:00 AM
* @author  : robson.castro@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("nomeacaoServidorFormBean")
@Scope("session")
public class NomeacaoServidorFormBean implements Serializable {

	static Logger logger = Logger.getLogger(NomeacaoServidorFormBean.class);

	@Autowired
	private NomeacaoFuncionalService nomeacaoFuncionalService;

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private TipoMovimentoService tipoMovimentoService;
	
	@Autowired
	private TipoPublicacaoService tipoPublicacaoService;
	
	@Autowired
	private FolhaService folhaService;
	
	@Autowired
	private TipoOcupacaoService tipoOcupacaoService;
	
	@Autowired
	private SituacaoService situacaoService;
	
	@Autowired
	private VinculoService vinculoService;
	
	@Autowired
	private SetorService setorService;
	
	@Autowired
	private CboService cboService;
	
	@Autowired
	private OcupacaoService ocupacaoService;
	
	@Autowired
	private ClasseReferenciaService classeReferenciaService;
	
	@Autowired
	private EspecialidadeCargoService especialidadeCargoService;
	
	@Autowired
	private OrientacaoCargoService orientacaoCargoService;

	@Autowired
	private PessoalService pessoalService;
	
	@Autowired
	private EntidadeService entidadeService;


	// entidades das telas
	private Funcional entidade = new Funcional();

	private String nome;
	private Long pessoa = new Long(0);

	private TipoOcupacao tipoOcupacao;
	private LeiIncorporacao leiIncorporacao;
	private EspecialidadeCargo especialidadeCargo; 

	private Cbo cbo1;
	private Cbo cbo2;
	private Cbo cbo3;

	// controle dos campos
	private Boolean alterar = false;
	private Boolean exibirTodosOsCampos = false;
	private Boolean tpOcupacaoCargoComissionado = false;
	private Boolean liberarQtdQuintos = false;
	private Boolean exibirPrevSuperSec = false;
	private Boolean exibirOrgaoEhSalario = false;


	// combos
	private List<TipoMovimento> comboTipoEntrada;
	private List<TipoPublicacao> comboTipoPublicacao;
	private List<Folha> comboTipoFolha;
	private List<TipoOcupacao> comboTipoOcupacao;
	private List<Ocupacao> comboCargoFuncao;
	private List<ClasseReferencia> comboClasseReferencia;
	private List<EspecialidadeCargo> comboEspecialidadeCargo;
	private List<OrientacaoCargo> comboOrientacaoCargo;
	private List<Situacao> comboSituacao;
	private List<Vinculo> comboVinculo;
	private List<Setor> comboSetor;
	private List<Cbo> comboCBO1;
	private List<Cbo> comboCBO2;
	private List<Cbo> comboCBO3;
	private List<Cbo> comboCBO4;
	private List<LeiIncorporacao> comboLeiIncorporacao;
	private List<Entidade> comboOrgaoOrigem;




	/**
	 * Realizar antes de carregar tela incluir
	 * 
	 * @return
	 */
	public String prepareIncluir() {
		limpar();

		setEntidade( new Funcional() );
		getEntidade().setProporcionalidade( 100l );
		getEntidade().setQtdQuintos( 0l );
		getEntidade().setRegime( 1l );
		getEntidade().setIRRF(true);

		return "incluir";
	}


	/**
	 * Realizar antes de carregar tela alterar
	 * 
	 * @return
	 */
	public String prepareAlterar() {

		try {

			limpar();
			this.alterar = true;

			this.entidade = funcionalService.getById( this.entidade.getId() );

			// dados pessoais
			this.pessoa = getEntidade().getPessoal().getId();
			this.nome = getEntidade().getPessoal().getNomeCompleto();

			// CBOs
			if ( this.entidade.getCbo() != null) {
				this.cbo3 = cboService.getByCodigo( this.entidade.getCbo().getCodigo().substring(0, this.entidade.getCbo().getCodigo().length() - 2) );
				this.cbo2 = cboService.getByCodigo( this.cbo3.getCodigo().substring(0, this.cbo3.getCodigo().length() - 1) );
				this.cbo1 = cboService.getByCodigo( this.cbo2.getCodigo().substring(0, this.cbo2.getCodigo().length() - 1) );				
			}

			// carrgar dados ocupacao
			if ( this.entidade.getOcupacao() != null )
				this.tipoOcupacao = getEntidade().getOcupacao().getTipoOcupacao();
			
			// exibir todos os campos?
			exibirTodosOsCampos = false;
			if(entidade.getSaida() == null)
				exibirTodosOsCampos = true;

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return "alterar";
	}
	

	/**
	 * Realizar salvar
	 * 
	 * @return
	 */
	public String salvar() {

		try {

			if ( leiIncorporacao != null )
				entidade.setLeiIncorporacao( leiIncorporacao.getDescricao() );

			nomeacaoFuncionalService.nomear( entidade );			

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
	 * Realizar alterar
	 * 
	 * @return
	 */
	public String alterar() {

		try {

			if ( leiIncorporacao != null )
				entidade.setLeiIncorporacao( leiIncorporacao.getDescricao() );
			
			nomeacaoFuncionalService.alterarNomeacao( entidade );			

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
	 * Combo Tipo Entrada
	 * 
	 * @return
	 */
	public List<TipoMovimento> getComboTipoEntrada() {

        try {

        	if( this.comboTipoEntrada == null ) {
        		if ( this.alterar ) {
        			this.comboTipoEntrada = tipoMovimentoService.findAll();
        		} else {
        			this.comboTipoEntrada = tipoMovimentoService.findByTipo(1L);	
        		}
        	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo tipo entrada. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
        
        return this.comboTipoEntrada;
	}


	/**
	 * Combo Tipo Publicacao
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
	 * Combo Tipo Folha
	 * 
	 * @return
	 */
	public List<Folha> getComboTipoFolha() {

		try {

			if( this.comboTipoFolha == null )
				this.comboTipoFolha = folhaService.findByAtivo(true);
	
        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo tipo folha. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboTipoFolha;
	}


	/**
	 * Combo Tipo Ocupação
	 * @return
	 */
	public List<TipoOcupacao> getComboTipoOcupacao() {

        try {

        	if( this.comboTipoOcupacao == null )
        		this.comboTipoOcupacao = tipoOcupacaoService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo tipo ocupação. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboTipoOcupacao;
	}


	/**
	 * Combo Cargo
	 * 
	 * @return
	 */
	public void carregaCargoFuncao() {

		if ( getTipoOcupacao() != null) {

			setTipoOcupacao( tipoOcupacaoService.getById( getTipoOcupacao().getId() ));

			if( getTipoOcupacao().getDescricao().equalsIgnoreCase("Ocupante somente Cargo Comissionado") ) {
				this.tpOcupacaoCargoComissionado = true;
				this.entidade.setFolha(new Folha());
				this.entidade.getFolha().setId(2l);
				this.entidade.setPrevidencia(2L);
				this.entidade.setSupSecIntegral(false);
				this.entidade.setAbonoPrevidenciario(false);
				this.entidade.setProporcionalidade(100L);
				this.entidade.setSalarioOrigem(new BigDecimal(0));
				this.entidade.setSalarioOrigemMonetario("0");
				this.entidade.setRegime(1L);
			} else {
				this.tpOcupacaoCargoComissionado = false;
			}

			this.comboCargoFuncao = null;
			this.comboClasseReferencia = null;
			getComboCargoFuncao();			
		}
	}

	public List<Ocupacao> getComboCargoFuncao() {

        try {

        	if( getTipoOcupacao() != null && this.comboCargoFuncao == null )
        		this.comboCargoFuncao = ocupacaoService.findByTipoOcupacao( getTipoOcupacao().getId() );

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo cargo. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboCargoFuncao;
	}


	/**
	 * Combo Classe Referencia
	 * 
	 * @return
	 */
	public void carregaClasseReferenciaEhEspecialidadeCargo() {

		if( entidade.getOcupacao() != null ) {
			
			this.entidade.getOcupacao().setId(entidade.getOcupacao().getId());

			this.comboClasseReferencia = null;
			getComboClasseReferencia();

			this.comboEspecialidadeCargo = null;
			getComboEspecialidadeCargo();
		}
	}

	/**
	 * Combo Orientação Cargo
	 * 
	 * @return
	 */
	public void carregaOrientacaoCargo() {

		if( entidade.getEspecialidadeCargo() != null ) {
			
			this.entidade.getEspecialidadeCargo().setId(entidade.getEspecialidadeCargo().getId());

			this.comboOrientacaoCargo = null;
			getComboOrientacaoCargo();
		}
	}
	
	public List<ClasseReferencia> getComboClasseReferencia() {

        try {

        	if( this.entidade.getOcupacao() != null && this.comboClasseReferencia == null )
    			this.comboClasseReferencia = classeReferenciaService.findByCargo(this.entidade.getOcupacao().getId());

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo classe referencia. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboClasseReferencia;
	}


	/**
	 * Combo Especialidade
	 * 
	 * @return
	 */
	public List<EspecialidadeCargo> getComboEspecialidadeCargo() {

        try {

        	if( this.entidade.getOcupacao() != null && this.comboEspecialidadeCargo == null )
    			this.comboEspecialidadeCargo = especialidadeCargoService.findByOcupacao(this.entidade.getOcupacao().getId());

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo especialidade. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboEspecialidadeCargo;
	}
	

	/**
	 * Combo Orientação Cargo
	 * 
	 * @return
	 */
	public List<OrientacaoCargo> getComboOrientacaoCargo() {

        try {

        	if( this.entidade.getOcupacao() != null && this.comboOrientacaoCargo == null )
        		especialidadeCargo = especialidadeCargoService.getById(this.entidade.getEspecialidadeCargo().getId());
    			this.comboOrientacaoCargo = orientacaoCargoService.findByEspecialidade(especialidadeCargo.getEspecialidade().getId());

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo especialidade. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboOrientacaoCargo;
	}
	
	
	/**
	 * Combo Situação
	 * 
	 * @return
	 */
	public List<Situacao> getComboSituacao() {

        try {

        	if( this.comboSituacao == null )
        		this.comboSituacao = situacaoService.findAll();
        
        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo situação. Operação cancelada");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboSituacao;
	}


	/**
	 * Combo Vinculo
	 * 
	 * @return
	 */
	public List<Vinculo> getComboVinculo() {

        try {

        	if( this.comboVinculo == null )
        		this.comboVinculo = vinculoService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo vinculo. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboVinculo;
	}


	/**
	 * Combo Setor
	 * 
	 * @return
	 */
	public List<Setor> getComboSetor() {

        try {

        	if( this.comboSetor == null )
        		this.comboSetor = setorService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboSetor;
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
	 * Combo Lei Incorporacao
	 * 
	 * @return
	 */
	public List<LeiIncorporacao> getComboLeiIncorporacao() {

        try {

        	if( this.comboLeiIncorporacao == null )
        		this.comboLeiIncorporacao = LeiIncorporacao.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo lei incorporação. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboLeiIncorporacao;
	}


	/**
	 * Combo Orgão Origem
	 * 
	 * @return
	 */
	public List<Entidade> getComboOrgaoOrigem() {

        try {

        	if( this.comboOrgaoOrigem == null )
        		this.comboOrgaoOrigem = entidadeService.listaOrgaoOrigemComRestricaoTipoEsfera();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo orgão origem. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboOrgaoOrigem;
	}


	/**
	 * Liberar o campo Qtd. Quintos a Lei Incorporacao
	 * 
	 */
	public void carregaQtdQuintos() {

		if( leiIncorporacao != null ) {

			if( LeiIncorporacao.LEI_INCOPORACAO_02.equals(leiIncorporacao) ) {
				this.liberarQtdQuintos = true;
			} else {
				this.liberarQtdQuintos = false;
				entidade.setQtdQuintos(0L);
			}

		}
	}


	/**
	 * Liberar os campos Abono e SUPSEC conforme a Previdencia
	 * 
	 */
	public void carregaPrevidenciaSupSec() {

		if( entidade.getPrevidencia() != null ) {

			if( entidade.getPrevidencia() == 2 ) {
				this.exibirPrevSuperSec = true;
			} else {
				this.exibirPrevSuperSec = false;
				entidade.setAbonoPrevidenciario(false);
				entidade.setSupSecIntegral(true);
			}

		}
	}


	/**
	 * Liberar o campo Orgao Origem conforme o Vinculo
	 * 
	 */
	public void carregaOrgaoEhSalario() {

		if( entidade.getVinculo() != null ) {

			this.entidade.setVinculo( vinculoService.getById( entidade.getVinculo().getId() ) );

			if( this.entidade.getVinculo().getOrgaoOrigem() == true ) {
				this.exibirOrgaoEhSalario = true;
			} else {
				this.exibirOrgaoEhSalario = false;
			}

		}
	}
	

	/**
	 * Limpar form
	 */
	private void limpar() {

		this.alterar = false;

		this.pessoa = new Long(0);
		this.nome = null;

		// CBOs
		this.cbo1 = null;
		this.cbo2 = null;
		this.cbo3 = null;

		this.tpOcupacaoCargoComissionado = false;
		this.exibirTodosOsCampos = true;
		this.tipoOcupacao = null;
		this.leiIncorporacao = null;

		// combos
		this.comboTipoEntrada = null;
		this.comboTipoPublicacao = null;
		this.comboTipoFolha = null;
		this.comboTipoOcupacao = null;
		this.comboCargoFuncao = null;
		this.comboClasseReferencia = null;
		this.comboEspecialidadeCargo = null;
		this.comboOrientacaoCargo = null;
		this.comboSituacao = null;
		this.comboVinculo = null;
		this.comboSetor = null;
		this.comboCBO1 = null;
		this.comboCBO2 = null;
		this.comboCBO3 = null;
		this.comboCBO4 = null;
		this.comboLeiIncorporacao = null;
		this.comboOrgaoOrigem = null;
	}
	
	public String limpaTela() {
		limpar();
		return "nomeacaoServidorForm";
	}	
	

	/**
	 * Gets and Sets
	 */
	public String getNome() {return this.nome;}
	public void setNome(String nome) {this.nome = nome;}

	public Funcional getEntidade() {return entidade;}
	public void setEntidade(Funcional entidade) {this.entidade = entidade;}
	
	public Long getPessoa() {return pessoa;}
	public void setPessoa(Long pessoa) {
		if ( !this.pessoa.equals(pessoa) ) {
			this.pessoa = pessoa;
			getEntidade().setPessoal( pessoalService.getById( this.pessoa ) );
			if (getEntidade().getPessoal() != null )
				this.nome = getEntidade().getPessoal().getNomeCompleto();
		}
	}

	public TipoOcupacao getTipoOcupacao() {return tipoOcupacao;}
	public void setTipoOcupacao(TipoOcupacao tipoOcupacao) {this.tipoOcupacao = tipoOcupacao;}

	public EspecialidadeCargo getEspecialidadeCargo() {return especialidadeCargo;}
	public void setEspecialidadeCargo(EspecialidadeCargo especialidadeCargo) {this.especialidadeCargo = especialidadeCargo;}

	public Boolean getTpOcupacaoCargoComissionado() {return tpOcupacaoCargoComissionado;}
	public void setTpOcupacaoCargoComissionado(Boolean tpOcupacaoCargoComissionado) {this.tpOcupacaoCargoComissionado = tpOcupacaoCargoComissionado;}

	public LeiIncorporacao getLeiIncorporacao() {
		if(entidade.getLeiIncorporacao() != null)			
			leiIncorporacao = LeiIncorporacao.getByDescricao(entidade.getLeiIncorporacao());			
		return leiIncorporacao;
	}
	public void setLeiIncorporacao(LeiIncorporacao leiIncorporacao) {this.leiIncorporacao = leiIncorporacao;}	

	public Cbo getCbo1() {return cbo1;}
	public void setCbo1(Cbo cbo1) {this.cbo1 = cbo1;}

	public Cbo getCbo2() {return cbo2;}
	public void setCbo2(Cbo cbo2) {this.cbo2 = cbo2;}

	public Cbo getCbo3() {return cbo3;}
	public void setCbo3(Cbo cbo3) {this.cbo3 = cbo3;}

	public void recarregaPagina(ValueChangeEvent event) {}

	public Boolean getAlterar() {return alterar;}
	public Boolean getExibirTodosOsCampos() {return exibirTodosOsCampos;}
	public Boolean getLiberarQtdQuintos() {return liberarQtdQuintos;}
	public Boolean getExibirPrevSuperSec() {return exibirPrevSuperSec;}
	public Boolean getExibirOrgaoEhSalario() {return exibirOrgaoEhSalario;}


}