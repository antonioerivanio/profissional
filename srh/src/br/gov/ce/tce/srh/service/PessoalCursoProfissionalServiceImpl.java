package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.PessoalCursoProfissionalDAO;
import br.gov.ce.tce.srh.domain.CompetenciaCurso;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("pessoalCursoProfissionalService")
public class PessoalCursoProfissionalServiceImpl implements PessoalCursoProfissionalService {

	@Autowired
	private PessoalCursoProfissionalDAO dao;

	@Autowired
	private CompetenciaCursoService competenciaCursoService;
	
	@Autowired
	private FuncionalService funcionalService;


	/**
	 * 
	 * @param alterar: TRUE | FALSE
	 *
	 */
	@Override
	@Transactional
	public void salvar(List<PessoalCursoProfissional> listaPessoaCurso,
			List<CompetenciaCurso> listaCompetencias,
			boolean alterar) throws SRHRuntimeException {

		// validando dados obrigatorios.
		validarDados(listaPessoaCurso, listaCompetencias);

		// somente quando for insercao
		if (!alterar) {

			// verificando se o curso ja existe
			PessoalCursoProfissional existe = dao.getByCurso(listaPessoaCurso.get(0).getCursoProfissional().getId());
			if (existe != null)
				throw new SRHRuntimeException("Este curso ja foi cadastrado.");

		}
		
		//Zacarias Gomes - 19/05/2014
		//altera��o para permitir cadatrar curso de conselheiro/auditor/procurador que n�o possui compet�ncia.
		// excluindo as competencias do curso
		if(!listaCompetencias.isEmpty()){
			competenciaCursoService.excluir(listaCompetencias.get(0).getCursoProfissional().getId());		
		}
		// excluindo o curso das pessoas
		dao.excluir(listaPessoaCurso.get(0).getCursoProfissional().getId());	

		// salvando os cursos da pessoa
		for (PessoalCursoProfissional curso : listaPessoaCurso) {
			dao.salvar(curso);
		}

		// salvando as competencias
		if(!listaCompetencias.isEmpty()){
			for (CompetenciaCurso competencia : listaCompetencias) {
				competenciaCursoService.salvar(competencia);
			}
		}
	}


	@Override
	@Transactional
	public void excluir(Long curso) {

		// excluindo as competencias do curso
		competenciaCursoService.excluir(curso);		

		// excluindo o curso das pessoas
		dao.excluir(curso);	

	}


	@Override
	public int count(Long area, String curso) {
		return dao.count(area, curso);
	}
	
	public int count(Long pessoal, Date inicio, Date fim, boolean areaAtuacao) {
		return dao.count(pessoal,inicio,fim,areaAtuacao);
	}


	@Override
	public List<CursoProfissional> search(Long area, String curso, int first, int rows) {
		return dao.search(area, curso, first, rows);
	}
	
	@Override
	public List<PessoalCursoProfissional> search(Long pessoal, Date inicio, Date fim, boolean areaAtuacao, int first, int rows) {
		return dao.search(pessoal,inicio, fim, areaAtuacao, first, rows);
	}

	@Override
	public List<PessoalCursoProfissional> getCursos(Long pessoal, Date inicio, Date fim, boolean areaAtuacao) {
		return dao.getCursos(pessoal,inicio, fim, areaAtuacao);
	}

	@Override
	public List<PessoalCursoProfissional> findByCurso(Long cursoProfissional) {
		return dao.findByCurso(cursoProfissional);
	}


	/**
	 * Validar:
	 * � Deve ser cadastrado ao menos uma competencia.
	 * � Deve ser cadastrado ao menos um servidor
	 *  
	 */
	private void validarDados(List<PessoalCursoProfissional> listaPessoaCurso,
			List<CompetenciaCurso> listaCompetencias) 
			throws SRHRuntimeException {
		
		boolean isCompetenciaObrigatoria = true;
		
		if (listaPessoaCurso == null || listaPessoaCurso.size() == 0)
			throw new SRHRuntimeException("Nenhum servidor cadastrado, � obrigat�rio pelo menos um servidor.");
		//Zacarias Gomes - 19/05/2014
		//altera��o para permitir cadatrar curso de conselheiro/auditor/procurador que n�o possui compet�ncia.
		if (listaCompetencias == null || listaCompetencias.size() == 0){
			for (PessoalCursoProfissional pessoal : listaPessoaCurso) {
				Funcional funcional = funcionalService.getByPessoaAtivo(pessoal.getPessoal().getId());
				if(funcional.getOcupacao().getId()==8 || funcional.getOcupacao().getId()==9||funcional.getOcupacao().getId()==10)
					isCompetenciaObrigatoria = false;
				else
					isCompetenciaObrigatoria = true;
		}
		
		}
		if (isCompetenciaObrigatoria && (listaCompetencias == null || listaCompetencias.size() == 0))
			throw new SRHRuntimeException("Nenhuma compet�ncia cadastrada, � obrigat�rio pelo menos uma compet�ncia.");

		

	}


	public void setDAO(PessoalCursoProfissionalDAO dao) {this.dao = dao;}
	public void setCompetenciaCursoService(CompetenciaCursoService competenciaCursoService) {this.competenciaCursoService = competenciaCursoService;}

}
