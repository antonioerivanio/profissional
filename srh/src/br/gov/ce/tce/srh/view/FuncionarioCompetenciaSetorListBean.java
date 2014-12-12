package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.component.html.HtmlForm;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.domain.AreaSetorCompetencia;
import br.gov.ce.tce.srh.domain.AtestoPessoa;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.domain.CompetenciaCurso;
import br.gov.ce.tce.srh.domain.CompetenciaGraduacao;
import br.gov.ce.tce.srh.domain.FuncionalAreaSetor;
import br.gov.ce.tce.srh.domain.Pessoal;
import br.gov.ce.tce.srh.domain.ServidorCompetencia;
import br.gov.ce.tce.srh.domain.sapjava.Setor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.service.AreaSetorCompetenciaService;
import br.gov.ce.tce.srh.service.AreaSetorService;
import br.gov.ce.tce.srh.service.AtestoPessoaService;
import br.gov.ce.tce.srh.service.CompetenciaCursoService;
import br.gov.ce.tce.srh.service.CompetenciaGraduacaoService;
import br.gov.ce.tce.srh.service.FuncionalAreaSetorService;
import br.gov.ce.tce.srh.service.sapjava.SetorService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : SRH_UC038_Consultar Servidor por Competência e Setor
* 
* @since   : Dez 19, 2011, 17:09:00 AM
* @author  : wesllhey.holanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("funcionarioCompetenciaSetorListBean")
@Scope("session")
public class FuncionarioCompetenciaSetorListBean implements Serializable {

	static Logger logger = Logger.getLogger(FuncionarioCompetenciaSetorListBean.class);


	@Autowired
	private AreaSetorCompetenciaService areaSetorCompetenciaService;

	@Autowired
	private SetorService setorService;

	@Autowired
	private AreaSetorService areaSetorService;
	
	@Autowired
	private FuncionalAreaSetorService funcionalAreaSetorService;
	
	@Autowired
	private AtestoPessoaService atestoPessoaService;
	
	@Autowired
	private CompetenciaCursoService competenciaCursoService;
	
	@Autowired 
	private CompetenciaGraduacaoService competenciaGraduacaoService;


	// controle de acesso do formulario
	private HtmlForm form;
	private boolean passouConsultar = false;

	// entidades das telas
	private Setor setor;
	private AreaSetor areaSetor;
	
	private Date dataInicio;
	private Date dataFinal;

	private List<ServidorCompetencia> listaServidorCompetencia = new ArrayList<ServidorCompetencia>();

	// combos
	private List<Setor> comboSetor;

	private List<AreaSetorCompetencia> lista;
	private AreaSetorCompetencia entidade;

	private FuncionalAreaSetor funcionalAreaSetor = new FuncionalAreaSetor();
	
	private AtestoPessoa atestoPessoa = new AtestoPessoa();
	private CompetenciaGraduacao competenciaGraduacao = new CompetenciaGraduacao();

	private CompetenciaCurso competenciaCurso = new CompetenciaCurso();
	
	
	/**
	 * Realizar Consulta
	 * 
	 * @return
	 */
	public String consultar() {

		try {

			// validando campos da consulta
			if (setor == null)
				throw new SRHRuntimeException("Selecione um setor.");
			if (areaSetor == null)
				throw new SRHRuntimeException("Selecione uma área.");

			// realizando consulta
			this.lista = areaSetorCompetenciaService.findByArea(areaSetor.getId());
			this.listaServidorCompetencia = new ArrayList<ServidorCompetencia>();

			dataTable();
	
			if ( this.lista.size() == 0 ) {
				FacesUtil.addInfoMessage("Nenhum registro foi encontrado.");
				logger.info("Nenhum registro foi encontrado.");
			}

			passouConsultar = true;

		} catch (SRHRuntimeException e) {
			FacesUtil.addErroMessage(e.getMessage());
			logger.warn("Ocorreu o seguinte erro: " + e.getMessage());
		} catch (Exception e) {
			FacesUtil.addErroMessage("Ocorreu algum erro na consulta. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}
		return "listar";
		
	}


    /**
     * Montando a estrutura da dateTable e implementando a subTable
     * 
     */
	private void dataTable() {

		// Pegando a lista de funcionarios da area do setor
		List<FuncionalAreaSetor> listaFuncionalAreaSetor = funcionalAreaSetorService.findyByAreaSetor( areaSetor.getId() );

		ServidorCompetencia servidorCompetencia;

		// percorrendo as competencias da area do setor
		for (AreaSetorCompetencia entidadeaAreaSetorCompetencia : lista) {

			// criando registro
			servidorCompetencia = new ServidorCompetencia();
			servidorCompetencia.setSetor(setor);
			servidorCompetencia.setAreaSetorCompetencia(entidadeaAreaSetorCompetencia);

			// percorrendo a lista de funcionarios da area do setor para verificar se tem a competencia
			for (FuncionalAreaSetor entidadeFuncionalAreaSetor : listaFuncionalAreaSetor) {

				// setando o servidor
				servidorCompetencia.setPessoal( entidadeFuncionalAreaSetor.getFuncional().getPessoal() );

				listaServidorCompetencia.add(servidorCompetencia);

				if (servidorCompetencia.getPessoal().getId() != null && servidorCompetencia.getAreaSetorCompetencia().getCompetencia().getId() != null) {
					atestoPessoa = atestoPessoaService.getByPessoalCompetencia(servidorCompetencia.getPessoal().getId(), servidorCompetencia.getAreaSetorCompetencia().getCompetencia().getId());

					competenciaCurso = competenciaCursoService.getByPessoalCompetencia(servidorCompetencia.getPessoal().getId(), servidorCompetencia.getAreaSetorCompetencia().getCompetencia().getId());

					competenciaGraduacao = competenciaGraduacaoService.getByPessoalCompetencia(servidorCompetencia.getPessoal().getId(), servidorCompetencia.getAreaSetorCompetencia().getCompetencia().getId());								
					
					
					
					if (atestoPessoa != null) {
						
						servidorCompetencia.setAtestoPessoa(atestoPessoa);
						
						if (dataInicio == null && dataFinal == null) {

							servidorCompetencia.setImg(true);
						} else

						if (dataInicio == null && atestoPessoa.getDataFim() != null && dataFinal.after(atestoPessoa.getDataFim())) {
							servidorCompetencia.setImg(true);
							
						} else if (dataInicio != null || dataFinal != null) {
							
							if (dataInicio == null || atestoPessoa.getDataInicio().after(dataInicio) || atestoPessoa.getDataInicio().equals(dataInicio)) {
								
								if (dataFinal == null || atestoPessoa.getDataFim() == null || dataFinal.after(atestoPessoa.getDataFim()) || dataFinal.equals(atestoPessoa.getDataFim())) {
									servidorCompetencia.setImg(true);
								}
							}

						}

						else {
							servidorCompetencia.setImg(false);
						}
					}
					if (competenciaCurso != null) {
												
						if (dataInicio == null && dataFinal == null) {

							servidorCompetencia.setImg(true);
						} else

						if (dataInicio == null && competenciaCurso.getCursoProfissional().getFim() != null && dataFinal.after(competenciaCurso.getCursoProfissional().getFim())) {
							servidorCompetencia.setImg(true);
							
						} else if (dataInicio != null || dataFinal != null) {
							
							if (dataInicio == null || competenciaCurso.getCursoProfissional().getInicio().after(dataInicio)|| competenciaCurso.getCursoProfissional().getInicio().equals(dataInicio)) {
								if (dataFinal == null || competenciaCurso.getCursoProfissional().getFim() == null || dataFinal.after(competenciaCurso.getCursoProfissional().getFim()) || dataFinal.equals(competenciaCurso.getCursoProfissional().getFim())) {
									servidorCompetencia.setImg(true);
								}
							}

						}

						else {
							servidorCompetencia.setImg(false);
						}
					}

					if (competenciaGraduacao != null) {
						servidorCompetencia.setCompetenciaGraduacao(competenciaGraduacao);						
						//List<CompetenciaGraduacao> listaCompetenciaGraducao = new ArrayList<CompetenciaGraduacao>();
					
						//listaCompetenciaGraducao.add(competenciaGraduacao);
						
						
						if (dataInicio == null) {

							servidorCompetencia.setImg(true);
						} else if (dataInicio != null) {
							
							if (competenciaGraduacao.getInicioCompetencia().after(dataInicio) || competenciaGraduacao.getInicioCompetencia().equals(dataInicio)) {
								servidorCompetencia.setImg(true);
							}

						}

						else {
							servidorCompetencia.setImg(false);
						}

					}
				}

				//competenciaGraduacao = new CompetenciaGraduacao();
				servidorCompetencia = new ServidorCompetencia();
				servidorCompetencia.setPessoal(new Pessoal());
				servidorCompetencia.setAreaSetorCompetencia(new AreaSetorCompetencia());
				servidorCompetencia.getAreaSetorCompetencia().setCompetencia(new Competencia());
			}
			atestoPessoa = new AtestoPessoa();
			competenciaCurso = new CompetenciaCurso();
			//competenciaGraduacao = new CompetenciaGraduacao();
		}

	}

	/**
	 * Combo Setor
	 * 
	 * @return
	 */
	public List<Setor> getComboSetor() {

        try {

        	if ( this.comboSetor == null )
        		this.comboSetor = setorService.findAll();

        } catch (Exception e) {
        	FacesUtil.addErroMessage("Erro ao carregar o campo setor. Operação cancelada.");
        	logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

        return this.comboSetor;
	}


	/**
	 * Combo Area
	 * 
	 * @return
	 */
	public void carregaArea() {
		lista = new ArrayList<AreaSetorCompetencia>();
		this.areaSetor = new AreaSetor();
	}

	public List<AreaSetor> getComboArea() {

		try {

			if( this.setor != null && this.setor.getId() != null) {
				return areaSetorService.findBySetor( this.setor.getId() );
			}

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao carregar o campo área. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		}

		return null;
	}

	public String limpaTela() {
		setEntidade(new AreaSetorCompetencia());
		return "listar";
	}

	/**
	 * Gets and Sets
	 */
	public AreaSetorCompetencia getEntidade() {return entidade;}
	public void setEntidade(AreaSetorCompetencia entidade) {this.entidade = entidade;}

	public List<AreaSetorCompetencia> getLista() {return lista;}

	public void setForm(HtmlForm form) {this.form = form;}
	public HtmlForm getForm() {
		if (!passouConsultar && lista != null) {
			lista = null;
			setor = null;
			areaSetor = null;
			comboSetor = null;

			listaServidorCompetencia = new ArrayList<ServidorCompetencia>();
			atestoPessoa = new AtestoPessoa();
			competenciaCurso = new CompetenciaCurso();
			competenciaGraduacao = new CompetenciaGraduacao();
			dataInicio = null;
			dataFinal = null;
		
		}
		passouConsultar = false;
		return form;
	}

	
	
	
	public Setor getSetor() {return setor;}
	public void setSetor(Setor setor) {this.setor = setor;}

	public AreaSetor getAreaSetor() {return areaSetor;}
	public void setAreaSetor(AreaSetor areaSetor) {this.areaSetor = areaSetor;}

	public FuncionalAreaSetor getFuncionalAreaSetor() {return funcionalAreaSetor;}
	public void setFuncionalAreaSetor(FuncionalAreaSetor funcionalAreaSetor) {this.funcionalAreaSetor = funcionalAreaSetor;}

	public List<ServidorCompetencia> getListaServidorCompetencia() {return listaServidorCompetencia;}
	public void setListaServidorCompetencia(List<ServidorCompetencia> listaServidorCompetencia) {this.listaServidorCompetencia = listaServidorCompetencia;}

	public AtestoPessoa getAtestoPessoa() {return atestoPessoa;}
	public void setAtestoPessoa(AtestoPessoa atestoPessoa) {this.atestoPessoa = atestoPessoa;}

	public CompetenciaGraduacao getCompetenciaGraduacao() {return competenciaGraduacao;}
	public void setCompetenciaGraduacao(CompetenciaGraduacao competenciaGraduacao) {this.competenciaGraduacao = competenciaGraduacao;}

	public CompetenciaCurso getCompetenciaCurso() {return competenciaCurso;}
	public void setCompetenciaCurso(CompetenciaCurso competenciaCurso) {this.competenciaCurso = competenciaCurso;}

	public Date getDataInicio() {return dataInicio;}
	public void setDataInicio(Date dataInicio) {this.dataInicio = dataInicio;}

	public Date getDataFinal() {return dataFinal;}
	public void setDataFinal(Date dataFinal) {this.dataFinal = dataFinal;}


	
}