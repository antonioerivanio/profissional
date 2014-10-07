package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AreaProfissional;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.domain.CompetenciaCurso;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissionalPk;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaProfissionalService;
import br.gov.ce.tce.srh.service.CompetenciaCursoService;
import br.gov.ce.tce.srh.service.CompetenciaService;
import br.gov.ce.tce.srh.service.CursoProfissionalService;
import br.gov.ce.tce.srh.service.FuncionalService;
import br.gov.ce.tce.srh.service.PessoalCursoProfissionalService;
import br.gov.ce.tce.srh.service.PessoalService;
import br.gov.ce.tce.srh.service.sca.AuthenticationService;
import br.gov.ce.tce.srh.util.FacesUtil;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
* Use case : SRH_UC023_Manter Curso da Pessoa
* 
* @since   : Out 27, 2011, 10:59:22 AM
* @author  : robstownholanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("pessoalCursoProfissionalFormBean")
@Scope("session")
public class PessoalCursoProfissionalFormBean implements Serializable {

	static Logger logger = Logger.getLogger(PessoalCursoProfissionalFormBean.class);

	@Autowired
	private PessoalCursoProfissionalService pessoalCursoProfissionalService;

	@Autowired
	private AreaProfissionalService areaProfissionalService;

	@Autowired
	private CursoProfissionalService cursoProfissionalService;

	@Autowired
	private CompetenciaCursoService competenciaCursoService;
	
	@Autowired
	private CompetenciaService competenciaService;

	@Autowired
	private PessoalService pessoalService;
	
	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private FuncionalService funcionalService;


	// entidades das telas
	private PessoalCursoProfissional entidade = new PessoalCursoProfissional();	
	private Funcional entidadeFuncional = new Funcional();
	private List<PessoalCursoProfissional> listaPessoaCurso = new ArrayList<PessoalCursoProfissional>();

	private boolean alterar = false;

	// curso
	private AreaProfissional area = new AreaProfissional();
	private CursoProfissional curso = new CursoProfissional();
	private CursoProfissional cursoAux = new CursoProfissional();

	private List<CursoProfissional> comboCurso = new ArrayList<CursoProfissional>();
	
	// competencia
	private CompetenciaCurso competenciaCurso = new CompetenciaCurso();
	private List<CompetenciaCurso> listaCompetencias = new ArrayList<CompetenciaCurso>();

	// servidores
	private String cpf = new String();
	private String nome = new String();
	private String matricula = new String();

	// combos
	private List<AreaProfissional> comboArea;
	private List<Competencia> comboCompetencia;



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

		try {

			this.alterar = true;

			this.curso = cursoProfissionalService.getById( curso.getId() );
			this.cursoAux = this.curso;
			this.listaCompetencias = competenciaCursoService.findByCurso( curso.getId() );
			this.listaPessoaCurso = pessoalCursoProfissionalService.findByCurso( curso.getId() );

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

			pessoalCursoProfissionalService.salvar(listaPessoaCurso, listaCompetencias, this.alterar);
			this.curso = new CursoProfissional();
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
	 * Combo Area
	 * 
	 * @return
	 */
	public List<AreaProfissional> getComboArea() {

		try {

			if ( this.comboArea == null )
				this.comboArea = areaProfissionalService.findAll();

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo area. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return this.comboArea;
	}


	/**
	 * Combo Curso
	 * 
	 * @return
	 */
	public void carregaCurso() {
		this.comboCurso = getCursoByArea();
	}

	public List<CursoProfissional> getCursoByArea() {

		List<CursoProfissional> toReturn = new ArrayList<CursoProfissional>();

		try {

			if (this.area != null ) {
				for (CursoProfissional entidade : cursoProfissionalService.findByArea(area.getId())) {
					toReturn.add(entidade);
				}
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo curso. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return toReturn;
	}


	/**
	 * Carrega dados da instituicao
	 * 
	 * @param event
	 */
	public void carregaInstituicao() {

        try {

    		if ( this.curso != null ) {
    			this.cursoAux = cursoProfissionalService.getById( this.curso.getId() );
    		} else {
    			this.cursoAux = new CursoProfissional();
    		}

    		this.listaCompetencias = new ArrayList<CompetenciaCurso>();
    		this.listaPessoaCurso = new ArrayList<PessoalCursoProfissional>();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo instituição. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

	}


	/**
	 * Combo Competencia
	 * 
	 * @return
	 */
	public List<Competencia> getComboCompetencia() {
 
        try {

        	if ( this.comboCompetencia == null )
        		this.comboCompetencia = competenciaService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo competência. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboCompetencia;
	}


	/**
	 * Adicionar Competencia
	 * 
	 * @return
	 */
	public String addCompetencia() {

		try {

			// validando se o curso foi selecionado
			if ( getCursoAux() == null || getCursoAux().getId().equals(0l)) {
				FacesUtil.addErroMessage("O curso é obrigatório.");
				return null;
			}

			// validando o campo obrigatorio competencia
			if ( this.competenciaCurso.getCompetencia() == null ) {
				FacesUtil.addErroMessage("A competência é obrigatória.");
				return null;
			}

			// verificando se a competencia ja foi cadatrada
			for (CompetenciaCurso item : listaCompetencias) {
				if ( item.getCompetencia().getId().equals(this.competenciaCurso.getCompetencia().getId()) ) {
					FacesUtil.addErroMessage("A competência ja esta na lista.");
					return null;
				}
			}

			// setando competencia do curso
			getCompetenciaCurso().setCompetencia( competenciaService.getById( getCompetenciaCurso().getCompetencia().getId() ));
			getCompetenciaCurso().setCursoProfissional( this.cursoAux );
			getCompetenciaCurso().setIniciocompetencia( this.cursoAux.getFim() );

			listaCompetencias.add(competenciaCurso);

			// limpando os campos da competencia
			setCompetenciaCurso(new CompetenciaCurso());
			getCompetenciaCurso().setCompetencia(new Competencia());

		} catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao adicionar a competência. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Remover Competencia
	 * 
	 * @return
	 */
	public String excluirCompetencia() {

		try {

			listaCompetencias.remove(competenciaCurso);

			setCompetenciaCurso(new CompetenciaCurso());
			getCompetenciaCurso().setCompetencia(new Competencia());

		} catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao remover a competência. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Adicionar Servidor
	 * 
	 * @return
	 */
	public String addServidor() {

		try {

			// validando se o curso foi selecionado
			if ( getCurso() == null ) {
				FacesUtil.addErroMessage("O curso é obrigatório.");
				return null;
			}

			// validando o campo obrigatorio competencia
			if ( getEntidade().getPessoal() == null ) {
				FacesUtil.addErroMessage("O Funcionário é obrigatório.");
				return null;
			}

			// verificando se o servidor ja foi cadatrado
			for (PessoalCursoProfissional item : listaPessoaCurso) {
				if (item.getPessoal().getId().equals( this.getEntidade().getPessoal().getId() )) {
					FacesUtil.addErroMessage("O Funcionário ja esta na lista.");
					return null;
				}
			}

			// setando servidor do curso
			PessoalCursoProfissional pessoaCurso = new PessoalCursoProfissional();
			pessoaCurso.setPessoal( getEntidade().getPessoal() );
			pessoaCurso.setCursoProfissional( getCursoAux() );
			pessoaCurso.setPk(new PessoalCursoProfissionalPk());
			pessoaCurso.getPk().setPessoal( getEntidade().getPessoal().getId() );
			pessoaCurso.getPk().setCursoProfissional( getCursoAux().getId() );
			pessoaCurso.setAreaAtuacao( getEntidade().isAreaAtuacao() );
			pessoaCurso.setTempoPromocao( getEntidade().isTempoPromocao() );

			listaPessoaCurso.add(pessoaCurso);

			// limpando os campos
			setEntidade( new PessoalCursoProfissional() );

			this.cpf = new String();
			this.nome = new String();
			this.matricula = new String();

		} catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao adicionar o servidor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Excluir o servidor
	 * 
	 * @return
	 */
	public String excluirServidor() {

		try {

			listaPessoaCurso.remove(entidade);
			setEntidade( new PessoalCursoProfissional() );

		} catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao remover o servidor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}


	/**
	 * Limpar dados
	 */
	private void limpar() {

		this.alterar = false;

		// entidades a persistir
		this.entidade = new PessoalCursoProfissional();	
		this.listaPessoaCurso = new ArrayList<PessoalCursoProfissional>();

		// curso
		this.area = new AreaProfissional();
		this.curso = new CursoProfissional();
		this.cursoAux = new CursoProfissional();
		
		// competencia
		this.competenciaCurso = new CompetenciaCurso();
		this.listaCompetencias = new ArrayList<CompetenciaCurso>();

		// servidores
		this.cpf = new String();
		this.nome = new String();
		this.matricula = new String();

		// combos
		this.comboArea = null;
		this.comboCurso = null;
		this.comboCompetencia = null;
	}


	/**
	 * Gets and Sets
	 */
	public AreaProfissional getArea() {return area;}
	public void setArea(AreaProfissional area) {this.area = area;}

	public List<CursoProfissional> getComboCurso() {return comboCurso;}

	public CursoProfissional getCurso() {return curso;}
	public void setCurso(CursoProfissional curso) {this.curso = curso;}

	public CursoProfissional getCursoAux() {return cursoAux;}
	public void setCursoAux(CursoProfissional cursoAux) {this.cursoAux = cursoAux;}

	public List<CompetenciaCurso> getListaCompetencias() {return listaCompetencias;}

	public CompetenciaCurso getCompetenciaCurso() {return competenciaCurso;}
	public void setCompetenciaCurso(CompetenciaCurso competenciaCurso) {this.competenciaCurso = competenciaCurso;}

	public String getMatricula() {return matricula;}
	public void setMatricula(String matricula) {
		if (!this.matricula.equals(matricula)) {
			this.matricula = matricula;
			
			try {
				if(authenticationService.getUsuarioLogado().hasAuthority("ROLE_PESSOA_SERVIDOR")){
					Funcional funcional = funcionalService.getMatriculaAndNomeByCpfAtiva(SRHUtils.removerMascara(authenticationService.getUsuarioLogado().getCpf()));
					setEntidadeFuncional(funcional);
					if(funcional != null){
						this.matricula = funcional.getMatricula();
					} else {
						this.matricula = new String();
						this.cpf = new String();
						this.nome = new String();
					}
				} else {
					setEntidadeFuncional( funcionalService.getCpfAndNomeByMatriculaAtiva( this.matricula ));
				}
				if ( getEntidadeFuncional() != null ) {
					this.nome = getEntidadeFuncional().getNomeCompleto();
					this.cpf = getEntidadeFuncional().getPessoal().getCpf();	
				} else {
					FacesUtil.addInfoMessage("Matrícula não encontrada ou inativa.");
				}

			} catch (Exception e) {
				FacesUtil.addErroMessage("Ocorreu um erro na consulta da matricula. Operação cancelada.");
				logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}
		}
		
	} 
	
	public String getCpf() {return cpf;}
	public void setCpf(String cpf) {
		if ( !this.cpf.equals(cpf) ) {
			this.cpf = cpf;

			try {

				getEntidade().setPessoal( pessoalService.getByCpf(cpf) );
				if (getEntidade().getPessoal() != null) {
					this.nome = getEntidade().getPessoal().getNomeCompleto();
				} else {
					FacesUtil.addInfoMessage("CPF não encontrado ou inativo.");
				}

			} catch (Exception e) {
	        	FacesUtil.addErroMessage("Ocorreu um erro na consulta do CPF. Operação cancelada.");
	        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
			}

		}
	}

	public String getNome() {return this.nome;}
	public void setNome(String nome) {this.nome = nome;}

	public PessoalCursoProfissional getEntidade() {return entidade;}
	public void setEntidade(PessoalCursoProfissional entidade) {this.entidade = entidade;}

	public List<PessoalCursoProfissional> getListaPessoaCurso() {return listaPessoaCurso;}

	public boolean isAlterar() {return alterar;}

	public Funcional getEntidadeFuncional() { return entidadeFuncional;	}
	public void setEntidadeFuncional(Funcional entidadeFuncional) {	this.entidadeFuncional = entidadeFuncional;	}

}
