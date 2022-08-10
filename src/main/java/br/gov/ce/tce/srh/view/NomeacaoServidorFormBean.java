package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.event.ValueChangeEvent;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Cbo;
import br.gov.ce.tce.srh.domain.ClasseReferencia;
import br.gov.ce.tce.srh.domain.CodigoCategoria;
import br.gov.ce.tce.srh.domain.EspecialidadeCargo;
import br.gov.ce.tce.srh.domain.Folha;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.FuncionalCedido;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.OrientacaoCargo;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.domain.Situacao;
import br.gov.ce.tce.srh.domain.TipoMovimento;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.domain.Vinculo;

import br.gov.ce.tce.srh.enums.LeiIncorporacao;
import br.gov.ce.tce.srh.enums.TipodeEmpresa;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Entidade;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.EntidadeService;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.CboService;
import br.gov.ce.tce.srh.service.ClasseReferenciaService;
import br.gov.ce.tce.srh.service.CodigoCategoriaService;
import br.gov.ce.tce.srh.service.EspecialidadeCargoService;
import br.gov.ce.tce.srh.service.FolhaService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.NomeacaoFuncionalService;
import br.gov.ce.tce.srh.service.OcupacaoService;
import br.gov.ce.tce.srh.service.OrientacaoCargoService;
import br.gov.ce.tce.srh.service.PessoaJuridicaService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.service.SituacaoService;
import br.gov.ce.tce.srh.service.TipoMovimentoService;
import br.gov.ce.tce.srh.service.TipoOcupacaoService;
import br.gov.ce.tce.srh.service.TipoPublicacaoService;
import br.gov.ce.tce.srh.service.VinculoService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("nomeacaoServidorFormBean")
@Scope("view")
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
	
	@Autowired
	private PessoaJuridicaService pessoaJuridicaService;	

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
	private Boolean digitarMatricula = false;


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
	private List<PessoaJuridica> instituicaoEnsinoList;
	private List<CodigoCategoria> comboCodCategList;


	@PostConstruct
	public void init() {
		
		Funcional flashParameter = null; 
		
		if (FacesUtil.getFlashParameter("entidade") != null && FacesUtil.getFlashParameter("entidade") instanceof Funcional) {
	      flashParameter = (Funcional) FacesUtil.getFlashParameter("entidade");
	    }
		
		if (flashParameter == null) {			
			setEntidade( new Funcional() );
			getEntidade().setProporcionalidade( 100l );
			getEntidade().setQtdQuintos( 0l );
			getEntidade().setRegime( 1l );
			getEntidade().setIRRF(true);
			getEntidade().setAtivoPortal(true);
			exibirTodosOsCampos = true;

		} else {			
			
			try {
				
				this.alterar = true;			
				
				this.entidade = funcionalService.getById( flashParameter.getId() );

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
			
		}
		
	}	

	public void salvar() {

		try {

			if ( leiIncorporacao != null )
				entidade.setLeiIncorporacao(leiIncorporacao.getDescricao());			

			if(alterar) {
				nomeacaoFuncionalService.alterarNomeacao(entidade);
			} else {		
				entidade.setCodigoCategoria(entidade.getCodigoCategoria().getCodigoCategoraByList(getComboCodCateg()));
				nomeacaoFuncionalService.nomear(entidade);			
			}
			
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

	public void carregaClasseReferenciaEhEspecialidadeCargo() {

		if( entidade.getOcupacao() != null ) {
			
			this.entidade.getOcupacao().setId(entidade.getOcupacao().getId());

			this.comboClasseReferencia = null;
			getComboClasseReferencia();

			this.comboEspecialidadeCargo = null;
			getComboEspecialidadeCargo();
		}
	}

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

	public Boolean getDigitarMatricula() {return digitarMatricula;}
	public void setDigitarMatricula(boolean digitarMatricula) {
		
		if(!digitarMatricula){
			entidade.setMatricula("");
		}
	}	
	
	public List<PessoaJuridica> getComboEmpresasCadastradas() {

		try {

			if ( this.instituicaoEnsinoList == null ) {
				this.instituicaoEnsinoList = pessoaJuridicaService.findAllByTipo(TipodeEmpresa.INSTITUICAO_ENSINO);
			}

		} catch (Exception e) {
			FacesUtil.addInfoMessage("Erro ao carregar o campo tipo de publicação. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.instituicaoEnsinoList;
	}
	
	public List<CodigoCategoria> getComboCodCateg(){
		try {
			this.comboCodCategList = nomeacaoFuncionalService.getCategoriaListAll();
		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo Codigo Categoria. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		
		return this.comboCodCategList;
	}
}