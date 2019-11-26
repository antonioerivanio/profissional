package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.RepresentacaoCargo;
import br.gov.ce.tce.srh.domain.RepresentacaoFuncional;
import br.gov.ce.tce.srh.domain.RepresentacaoSetor;
import br.gov.ce.tce.srh.domain.TipoDocumento;
import br.gov.ce.tce.srh.domain.TipoPublicacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoSetorService;
import br.gov.ce.tce.srh.service.TipoDocumentoService;
import br.gov.ce.tce.srh.service.TipoPublicacaoService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("representacaoFuncionalFormBean")
@Scope("view")
public class RepresentacaoFuncionalFormBean implements Serializable {

	static Logger logger = Logger.getLogger(PessoalCursoGraduacaoFormBean.class);

	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;

	@Autowired
	private FuncionalService funcionalService;
	
	@Autowired
	private SetorService setorService;
	
	@Autowired
	private TipoDocumentoService tipoDocumentoService;

	@Autowired
	private RepresentacaoSetorService representacaoSetorService;
	
	@Autowired
	private TipoPublicacaoService tipoPublicacaoService;


	// entidades das telas
	private RepresentacaoFuncional entidade = new RepresentacaoFuncional();	

	private String matricula = new String();
	private String nome = new String();

	private boolean alterar = false;
	
	// combos
	private List<TipoDocumento> comboTipoDocumento;
	private List<Setor> comboSetor;
	private List<RepresentacaoCargo> comboRepresentacaoCargo;
	private List<TipoPublicacao> comboTipoPublicacao;
	
	private Boolean exibirTodosOsCampos = false;
	private Boolean alterarMenu = false;

	@PostConstruct
	public void init() {
		
		RepresentacaoFuncional flashParameter = (RepresentacaoFuncional)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new RepresentacaoFuncional());
		
		if(this.entidade.getId() != null) {
			this.alterar = true;
	
			this.nome = getEntidade().getFuncional().getNomeCompleto();
			this.matricula = getEntidade().getFuncional().getMatricula();		
			
			exibirTodosOsCampos = true;
			alterarMenu = true;
			if(entidade.getFim() == null){
				exibirTodosOsCampos = false;
				alterar = false;
			}
		}
	}

	public void salvar() {

		try {

			representacaoFuncionalService.salvar(entidade);
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
	}

	public List<TipoDocumento> getComboTipoDocumento() {

		try {

			if ( this.comboTipoDocumento == null)
				this.comboTipoDocumento = tipoDocumentoService.findByDocfuncional(true);

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo tipo de documento. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoDocumento;
	}

	public List<Setor> getComboSetor() {

		try {

			if ( this.comboSetor == null)
				this.comboSetor = setorService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboSetor;
	}

	public void carregaCargo() {
		this.comboRepresentacaoCargo = null;
		getComboRepresentacaoCargo();			
	}

	public List<RepresentacaoCargo> getComboRepresentacaoCargo() {

		try {

			if ( this.entidade.getSetor() != null && this.comboRepresentacaoCargo == null ) {

				this.comboRepresentacaoCargo = new ArrayList<RepresentacaoCargo>();
				
				List<RepresentacaoSetor> representacaoSetorList = representacaoSetorService.findBySetorAtivo( getEntidade().getSetor().getId(), true );
				
				for ( RepresentacaoSetor reprSetor : representacaoSetorList) {
					this.comboRepresentacaoCargo.add( reprSetor.getRepresentacaoCargo() );
				}
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo representação cargo. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboRepresentacaoCargo;
	}

	public List<TipoPublicacao> getComboTipoPublicacao() {

		try {

			if ( this.comboTipoPublicacao == null )
				this.comboTipoPublicacao = tipoPublicacaoService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo tipo publicação. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboTipoPublicacao;
	}
	
	private void limpar() {

		setEntidade(new RepresentacaoFuncional());

		this.alterar = false;
		this.matricula = new String();
		this.nome = new String();

		this.comboTipoDocumento = null;
		this.comboSetor = null;
		this.comboTipoDocumento = null;
		this.comboRepresentacaoCargo = null;
		this.comboTipoPublicacao = null;
		
		this.exibirTodosOsCampos = false;
		this.alterarMenu = false;
	}
		
	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equals(matricula) ) {
			this.matricula = matricula;

			try {

				Funcional funcional = funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula );
				if (funcional != null) {
					getEntidade().setFuncional( funcional );
					getEntidade().setSetor( funcional.getSetor() );
					this.comboRepresentacaoCargo = null;
					this.nome = funcional.getNomeCompleto();
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matrícula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() {return this.nome;}
	public void setNome(String nome) {this.nome = nome;}

	public RepresentacaoFuncional getEntidade() {return entidade;}
    public void setEntidade(RepresentacaoFuncional entidade) {this.entidade = entidade;}

	public boolean isAlterar() {return alterar;}


	public Boolean getExibirTodosOsCampos() {
		return exibirTodosOsCampos;
	}


	public void setExibirTodosOsCampos(Boolean exibirTodosOsCampos) {
		this.exibirTodosOsCampos = exibirTodosOsCampos;
	}


	public Boolean getAlterarMenu() {
		return alterarMenu;
	}


	public void setAlterarMenu(Boolean alterarMenu) {
		this.alterarMenu = alterarMenu;
	}

}