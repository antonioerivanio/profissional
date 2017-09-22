package br.gov.ce.tce.srh.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import br.gov.ce.tce.srh.domain.AtestoPessoa;
import br.gov.ce.tce.srh.domain.CompetenciaCurso;
import br.gov.ce.tce.srh.domain.CompetenciaGraduacao;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.PessoalCursoAcademica;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.domain.ServidorCompetencia;
import br.gov.ce.tce.srh.sapjava.service.SetorService;
import br.gov.ce.tce.srh.service.AtestoPessoaService;
import br.gov.ce.tce.srh.service.CompetenciaCursoService;
import br.gov.ce.tce.srh.service.CompetenciaGraduacaoService;
import br.gov.ce.tce.srh.service.PessoalCursoAcademicaService;
import br.gov.ce.tce.srh.util.FacesUtil;

/**
* Use case : SRH_UC042_Manter Emitir Dociê do Servidor 
* 
* @since   : Fev 09, 2012, 11:16:00
* @author  : wesllhey.holanda@ivia.com.br
*/
@SuppressWarnings("serial")
@Component("funcionarioCompetenciaSetorFormBean")
@Scope("session")
public class FuncionarioCompetenciaSetorFormBean implements Serializable {

	static Logger logger = Logger.getLogger(FuncionarioCompetenciaSetorFormBean.class);

	
	@Autowired
	private SetorService setorService;
	
	@Autowired
	private AtestoPessoaService atestoPessoaService;
	
	@Autowired
	private CompetenciaCursoService competenciaCursoService;
	
	@Autowired
	private CompetenciaGraduacaoService competenciaGraduacaoService;
	
	@Autowired
	private PessoalCursoAcademicaService pessoalCursoAcademicaService;
	
	// entidades das telas
	private ServidorCompetencia servidorCompetencia = new ServidorCompetencia();
	
	//Listas
	private AtestoPessoa entidadeAtestoPessoa = new AtestoPessoa();
	private PessoalCursoAcademica pessoalCursoAcademica = new PessoalCursoAcademica();
 
	private PessoalCursoProfissional pessoalCursoProfissional = new PessoalCursoProfissional();
	private CompetenciaCurso competenciaCurso = new CompetenciaCurso();
	private CompetenciaGraduacao competenciaGraduacao = new CompetenciaGraduacao();
	
	private List<PessoalCursoAcademica> listaAcademica = new ArrayList<PessoalCursoAcademica>();
	
	private boolean cursoProfissional = false;
	private boolean cursoGraduacao = false;
	private boolean atestoPessoa = false;
	
	private List<CursoProfissional> listaCursoProfissional = new ArrayList<CursoProfissional>();
	
	public String getTestaTela() {
		return "Aqui";
	}


	/**
	 * Realizar antes de carregar tela de visualizacao
	 * 
	 * @return
	 */
	public String visualizar() {
		try {

			servidorCompetencia.setSetor(setorService.getById(servidorCompetencia.getSetor().getId()));
			
			competenciaCurso = competenciaCursoService.getByPessoalCompetencia(servidorCompetencia.getPessoal().getId(), servidorCompetencia.getAreaSetorCompetencia().getCompetencia().getId());

			entidadeAtestoPessoa = atestoPessoaService.getByPessoalCompetencia(servidorCompetencia.getPessoal().getId(), servidorCompetencia.getAreaSetorCompetencia().getCompetencia().getId());
			
			competenciaGraduacao = competenciaGraduacaoService.getByPessoalCompetencia(servidorCompetencia.getPessoal().getId(), servidorCompetencia.getAreaSetorCompetencia().getCompetencia().getId());
			
			
			
			
			if(entidadeAtestoPessoa != null){
				atestoPessoa  = true;
			}
			
			if(competenciaCurso != null){
				cursoProfissional = true;
			}
			
			if (competenciaCurso != null) {
				listaAcademica = pessoalCursoAcademicaService.findByCursoAcademica(competenciaGraduacao.getCursoAcademica().getId());
				cursoGraduacao = true;
				
			}
			

		} catch (Exception e) {
			FacesUtil.addErroMessage("Erro ao visualizar os dados. Operação cancelada.");
			logger.fatal("Ocorreu o seguinte erro: " + e.getMessage());
		} 
			
		
		return "incluirAlterar";
	}


	/**
	 * Gets and Sets
	 */
	
	public ServidorCompetencia getServidorCompetencia() {return servidorCompetencia;}
	public void setServidorCompetencia(ServidorCompetencia servidorCompetencia) {this.servidorCompetencia = servidorCompetencia;}
	
	public boolean isCursoProfissional() {return cursoProfissional;}
	public void setCursoProfissional(boolean cursoProfissional) {this.cursoProfissional = cursoProfissional;}

	public boolean isCursoGraduacao() {return cursoGraduacao;}
	public void setCursoGraduacao(boolean cursoGraduacao) {this.cursoGraduacao = cursoGraduacao;}

	public boolean isAtestoPessoa() {return atestoPessoa;}
	public void setAtestoPessoa(boolean atestoPessoa) {this.atestoPessoa = atestoPessoa;}

	public List<PessoalCursoAcademica> getListaAcademica() {return listaAcademica;}
	public void setListaAcademica(List<PessoalCursoAcademica> listaAcademica) {this.listaAcademica = listaAcademica;}

	public PessoalCursoAcademica getPessoalCursoAcademica() {return pessoalCursoAcademica;}
	public void setPessoalCursoAcademica(PessoalCursoAcademica pessoalCursoAcademica) {this.pessoalCursoAcademica = pessoalCursoAcademica;}

	public PessoalCursoProfissional getPessoalCursoProfissional() {return pessoalCursoProfissional;}
	public void setPessoalCursoProfissional(PessoalCursoProfissional pessoalCursoProfissional) {this.pessoalCursoProfissional = pessoalCursoProfissional;}

	public AtestoPessoa getListaAtestoPessoa() {return entidadeAtestoPessoa;}
	public void setListaAtestoPessoa(AtestoPessoa listaAtestoPessoa) {this.entidadeAtestoPessoa = listaAtestoPessoa;}

	public CompetenciaCurso getCompetenciaCurso() {return competenciaCurso;}
	public void setCompetenciaCurso(CompetenciaCurso competenciaCurso) {this.competenciaCurso = competenciaCurso;}

	public List<CursoProfissional> getListaCursoProfissional() {return listaCursoProfissional;}
	public void setListaCursoProfissional(List<CursoProfissional> listaCursoProfissional) {this.listaCursoProfissional = listaCursoProfissional;}

	public CompetenciaGraduacao getCompetenciaGraduacao() {return competenciaGraduacao;}
	public void setCompetenciaGraduacao(CompetenciaGraduacao competenciaGraduacao) {this.competenciaGraduacao = competenciaGraduacao;}

}