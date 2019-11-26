package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AreaSetorCompetencia;
import br.gov.ce.tce.srh.domain.AtestoPessoa;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaSetorCompetenciaService;
import br.gov.ce.tce.srh.service.AtestoPessoaService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.RepresentacaoFuncionalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("atestoPessoaFormBean")
@Scope("view")
public class AtestoPessoaFormBean implements Serializable {

	static Logger logger = Logger.getLogger(AtestoPessoaFormBean.class);
	
	@Autowired
	private AtestoPessoaService atestoPessoaService;
	
	@Autowired
	private AreaSetorCompetenciaService areaSetorCompetenciaService;

	@Autowired
	private RepresentacaoFuncionalService representacaoFuncionalService;

	@Autowired
	private FuncionalService funcionalService;

	private boolean alterar = false;
	private boolean passouConsultar = false;

	// entidades das telas
	private AtestoPessoa entidade = new AtestoPessoa();

	private Funcional funcional;
	private String matricula = new String();
	private String nome = new String();

	private Long idResponsavel = new Long(0);
	private String nomeResponsavel;

	// combos
	private List<Competencia> comboCompetencia;

	@PostConstruct
	public void init() {
		
		AtestoPessoa flashParameter = (AtestoPessoa)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new AtestoPessoa());

		if(this.entidade.getId() != null) {			
			this.alterar = true;
			this.passouConsultar = true;
			
			this.funcional = funcionalService.getByPessoaAtivo( getEntidade().getPessoal().getId() );
			
			this.matricula = this.funcional.getMatricula();
			this.nome = getEntidade().getPessoal().getNomeCompleto();
			
			this.idResponsavel = getEntidade().getResponsavel().getId();
			this.nomeResponsavel = getEntidade().getResponsavel().getFuncional().getNomeCompleto();
		}

	}

	public void carregar() {

		try {

			if ( this.funcional == null )
				throw new SRHRuntimeException("Selecione um funcionário.");

			this.funcional = funcionalService.getById( this.funcional.getId() );
			this.passouConsultar = true;

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public void salvar() {
		
		try {

			atestoPessoaService.salvar(entidade);
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

	public List<Competencia> getComboCompetencia() {

		try {

			if ( this.comboCompetencia == null ) {
				this.comboCompetencia = new ArrayList<Competencia>();
				for(AreaSetorCompetencia areaSetorCompetencia : areaSetorCompetenciaService.findBySetor( this.funcional.getSetor().getId() )) {
					this.comboCompetencia.add( areaSetorCompetencia.getCompetencia() );
				}
			}

		} catch (Exception e){
			FacesUtil.addInfoMessage("Erro ao carregar o campo competência. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: "+ e.getMessage());
		} 
		
		return this.comboCompetencia;
	}

	private void limpar() {

		setEntidade( new AtestoPessoa() );

		this.alterar = false;

		this.funcional = null;

		this.matricula = new String();
		this.nome = new String();

		this.idResponsavel = new Long(0);
		this.nomeResponsavel = null;

		this.comboCompetencia = null;
	}
	
	public AtestoPessoa getEntidade() {return entidade;}
	public void setEntidade(AtestoPessoa entidade) {this.entidade = entidade;}

	public Funcional getFuncional() {return funcional;}
	public void setFuncional(Funcional funcional) {this.funcional = funcional;}

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if ( !this.matricula.equalsIgnoreCase(matricula) ) {
			this.matricula = matricula;

			try {

				this.funcional = funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula ); 
				if ( this.funcional != null) {
					this.nome = this.funcional.getNomeCompleto();
					getEntidade().setPessoal( this.funcional.getPessoal() );
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matricula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() {return nome;}
	public void setNome(String nome) {this.nome = nome;}

	public Long getIdResponsavel() {return idResponsavel;}
	public void setIdResponsavel(Long idResponsavel) {
		if ( !this.idResponsavel.equals( idResponsavel )) {
			this.idResponsavel = idResponsavel;

			try {

				getEntidade().setResponsavel( representacaoFuncionalService.getByid( this.idResponsavel ) );

			} catch (Exception e) {
				FacesUtil.addInfoMessage("Erro ao carregar o responsavel. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: "+ e.getMessage());
			}
			
		}
	}

	public String getNomeResponsavel() {return nomeResponsavel;}
	public void setNomeResponsavel(String nomeResponsavel) {this.nomeResponsavel = nomeResponsavel;}

	public boolean isAlterar() {return alterar;}
	public boolean isPassouConsultar() {return passouConsultar;}
	
}
