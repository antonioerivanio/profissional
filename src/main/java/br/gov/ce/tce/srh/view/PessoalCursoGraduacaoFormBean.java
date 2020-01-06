package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.domain.CompetenciaGraduacao;
import br.gov.ce.tce.srh.domain.CursoAcademica;
import br.gov.ce.tce.srh.domain.Instituicao;
import br.gov.ce.tce.srh.domain.PessoalCursoAcademica;
import br.gov.ce.tce.srh.domain.PessoalCursoAcademicaPk;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.CompetenciaGraduacaoService;
import br.gov.ce.tce.srh.service.CompetenciaService;
import br.gov.ce.tce.srh.service.CursoAcademicaService;
import br.gov.ce.tce.srh.service.InstituicaoService;
import br.gov.ce.tce.srh.service.PessoalCursoAcademicaService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.util.FacesUtil;

@SuppressWarnings("serial")
@Component("pessoalCursoGraduacaoFormBean")
@Scope("view")
public class PessoalCursoGraduacaoFormBean implements Serializable {

	static Logger logger = Logger.getLogger(PessoalCursoGraduacaoFormBean.class);

	@Autowired
	private PessoalCursoAcademicaService pessoalCursoAcademicaService;

	@Autowired
	private CompetenciaGraduacaoService competenciaGraduacaoService;

	@Autowired
	private CursoAcademicaService cursoAcademicaService;

	@Autowired
	private CompetenciaService competenciaService;

	@Autowired
	private InstituicaoService instituicaoService;

	@Autowired
	private PessoalService pessoalService;


	// entidades das telas
	private Long idPessoa = new Long(0);
	private String nome;

	private boolean alterar = false;

	private PessoalCursoAcademica entidade = new PessoalCursoAcademica();	
	
	// curso
	private Date inicioCompetencia;

	// competencia
	private CompetenciaGraduacao competenciaGraduacao = new CompetenciaGraduacao();
	private List<CompetenciaGraduacao> listaCompetencias = new ArrayList<CompetenciaGraduacao>();
	private List<CompetenciaGraduacao> listacompetenciasExcluidas = new ArrayList<CompetenciaGraduacao>();
	private List<PessoalCursoAcademica> listaPessoalCursoAcademica = new ArrayList<PessoalCursoAcademica>();

	// combos
	private List<CursoAcademica> comboCurso;
	private List<Instituicao> comboInstituicao;
	private List<Competencia> comboCompetencia;

	@PostConstruct
	public void init() {
		PessoalCursoAcademica flashParameter = (PessoalCursoAcademica)FacesUtil.getFlashParameter("entidade");
		setEntidade(flashParameter != null ? flashParameter : new PessoalCursoAcademica());
		
		try {
			
			if(this.entidade.getPk() != null) {			
				this.alterar = true;
	
				// pegando os dados do servidor
				this.idPessoa = getEntidade().getPessoal().getId();
				this.nome = getEntidade().getPessoal().getNomeCompleto();
	
				// pegando a lista de competencias
				this.inicioCompetencia =  getEntidade().getDataConclusao();
				listaCompetencias = competenciaGraduacaoService.findByPessoaCurso(getEntidade().getPessoal().getId(), getEntidade().getCursoAcademica().getId());
				/*if ( this.listaCompetencias.size() > 0 )
					this.inicioCompetencia = listaCompetencias.get(0).getInicioCompetencia();*/
			}	

		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro ao carregar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public void salvar() {

		try {
			
			getEntidade().setDataConclusao(inicioCompetencia);
			pessoalCursoAcademicaService.salvar( getEntidade(), this.listaCompetencias, this.listacompetenciasExcluidas,this.inicioCompetencia, alterar );

			this.alterar = false;
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

	public List<CursoAcademica> getComboCurso() {

        try {

        	if ( this.comboCurso == null ) {
        		this.comboCurso = cursoAcademicaService.findAll(); 
        	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo curso. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

    	return this.comboCurso;
	}

	public List<Instituicao> getComboInstituicao() {

        try {

        	if ( this.comboInstituicao == null ) {

        		this.comboInstituicao = new ArrayList<Instituicao>();

        		for ( Instituicao entidade : instituicaoService.findAll() ) {
            		if (entidade.isCursoSuperior()) {
            			this.comboInstituicao.add(entidade);
            		}
                }	
 
        	}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo instituição. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboInstituicao;
	}

	public List<Competencia> getComboCompetencia() {

		try {

			if ( this.comboCompetencia == null ) {
				this.comboCompetencia = competenciaService.findAll(); 
			}

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo competência. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboCompetencia;
	}

	public void addCompetencia() {

		try {

			// validando se a pessoa foi selecionada
			if ( getEntidade().getPessoal() == null || getEntidade().getPessoal().getId().equals(0l) ) {
				FacesUtil.addErroMessage("O Funcionário é obrigatório. Digite o nome e efetue a pesquisa.");
				return;			
			}

			// validando se o curso foi selecionado
			if ( this.entidade.getCursoAcademica() == null ) {
				FacesUtil.addErroMessage("O curso é obrigatório.");
				return;
			}

			// validando se a data de inicio da competencia foi preenchida
			if ( this.inicioCompetencia == null ) {
				FacesUtil.addErroMessage("A data de conclusão do curso é obrigatória.");
				return;
			}

			// validando o campo obrigatorio competencia
			if ( this.competenciaGraduacao.getCompetencia() == null ) {
				FacesUtil.addErroMessage("A competência é obrigatória.");
				return;
			}

			// verificando se a competencia ja foi cadatrada para a mesma pessoa
			for (CompetenciaGraduacao item : listaCompetencias) {
				if (item.getCompetencia().getId().equals(this.competenciaGraduacao.getCompetencia().getId())
						&& item.getPessoal().getId().equals(getEntidade().getPessoal().getId())) {
					FacesUtil.addErroMessage("A competência já esta na lista para esse servidor.");
					return;
				}
			}

			// setando competencia pessoa
			this.competenciaGraduacao.setPessoal( getEntidade().getPessoal() );
			this.competenciaGraduacao.setCursoAcademica( getEntidade().getCursoAcademica() );
			this.competenciaGraduacao.setCompetencia(competenciaService.getById( getCompetenciaGraduacao().getCompetencia().getId() ) );
			this.competenciaGraduacao.setInicioCompetencia( this.inicioCompetencia );

			listaCompetencias.add(competenciaGraduacao);

			// limpando os campos da competencia
			setCompetenciaGraduacao( new CompetenciaGraduacao() );

		} catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao adicionar a Competência. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	public void excluirCompetencia() {

		try {
			
			listacompetenciasExcluidas.add(listaCompetencias.get(listaCompetencias.indexOf(competenciaGraduacao)));
			listaCompetencias.remove(competenciaGraduacao);
			setCompetenciaGraduacao(new CompetenciaGraduacao());

		} catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao remover a Competência. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
	}

	private void limpar() {

		this.idPessoa = new Long(0);
		this.nome = null;

		this.inicioCompetencia = null;

		setEntidade( new PessoalCursoAcademica() );
		getEntidade().setPk( new PessoalCursoAcademicaPk() );
		getEntidade().setCursoAcademica( new CursoAcademica() );

		setCompetenciaGraduacao(new CompetenciaGraduacao());
		this.listaCompetencias = new ArrayList<CompetenciaGraduacao>();
		this.listacompetenciasExcluidas = new ArrayList<CompetenciaGraduacao>();

		// combos
		this.comboCurso = null;
		this.comboInstituicao = null;
		this.comboCompetencia = null;
	}

	public Long getIdPessoa() {return idPessoa;}
	public void setIdPessoa(Long idPessoa) {
		if ( !this.idPessoa.equals( idPessoa ) ) {
			this.idPessoa = idPessoa;

			try {

				getEntidade().setPessoal( pessoalService.getById( this.idPessoa ));

			} catch (Exception e) {
				FacesUtil.addInfoMessage("Erro ao carregar a pessoa. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: "+ e.getMessage());
			}
			
		}
	}

	public String getNome() {return this.nome;}
	public void setNome(String nome) {this.nome = nome;}

	public Date getInicioCompetencia() {return inicioCompetencia;}
	public void setInicioCompetencia(Date inicioCompetencia) {this.inicioCompetencia = inicioCompetencia;}

	public PessoalCursoAcademica getEntidade() {return entidade;}
	public void setEntidade(PessoalCursoAcademica entidade) {this.entidade = entidade;}
	
	public CompetenciaGraduacao getCompetenciaGraduacao() {return competenciaGraduacao;}
	public void setCompetenciaGraduacao(CompetenciaGraduacao competenciaGraduacao) {this.competenciaGraduacao = competenciaGraduacao;}

	public List<CompetenciaGraduacao> getListaCompetencias() {return listaCompetencias;}

	public boolean isAlterar() {return alterar;}

	public List<PessoalCursoAcademica> getListaPessoalCursoAcademica() {return listaPessoalCursoAcademica;}
	public void setListaPessoalCursoAcademica(List<PessoalCursoAcademica> listaPessoalCursoAcademica) {this.listaPessoalCursoAcademica = listaPessoalCursoAcademica;}

}
