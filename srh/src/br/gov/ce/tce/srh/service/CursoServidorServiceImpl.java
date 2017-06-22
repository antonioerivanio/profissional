package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CursoServidorDAO;
import br.gov.ce.tce.srh.domain.CompetenciaCurso;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

@Service("cursoServidorService")
public class CursoServidorServiceImpl implements CursoServidorService {

	@Autowired
	private CursoServidorDAO dao;

	@Autowired
	private CompetenciaCursoService competenciaCursoService;


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
				throw new SRHRuntimeException("Este curso já foi cadastrado.");

		}

		// excluindo as competencias do curso
		competenciaCursoService.excluir(listaCompetencias.get(0).getCursoProfissional().getId());		
		
		// excluindo o curso das pessoas
		dao.excluir(listaPessoaCurso.get(0).getCursoProfissional().getId());	

		// salvando os cursos da pessoa
		for (PessoalCursoProfissional curso : listaPessoaCurso) {
			dao.salvar(curso);
		}

		// salvando as competencias
		for (CompetenciaCurso competencia : listaCompetencias) {
			competenciaCursoService.salvar(competencia);
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
	
	public int count(Long pessoal, boolean areaAtuacao, boolean posGraduacao, boolean profissional, Date inicio, Date fim) {
		return dao.count(pessoal, areaAtuacao, posGraduacao, profissional, inicio,  fim);
	}

	@Override
	public List<CursoProfissional> search(String curso) {
		return dao.search(curso);
	}

	@Override
	public List<CursoProfissional> search(Long area, String curso, int first, int rows) {
		return dao.search(area, curso, first, rows);
	}
	
	@Override
	public List<PessoalCursoProfissional> search(Long pessoal, boolean areaAtuacao,boolean posGraduacao, boolean profissional, Date inicio,Date fim, int first, int rows) {
		return dao.search(pessoal, areaAtuacao, posGraduacao, profissional, inicio, fim, first, rows);
	}

//	@Override
//	public List<PessoalCursoProfissional> getCursos(Long pessoal, boolean areaAtuacao) {
//		return dao.getCursos(pessoal, areaAtuacao);
//	}

	@Override
	public List<PessoalCursoProfissional> getCursos(Long pessoal, boolean areaAtuacao, boolean posGraduacao, boolean profissional, Date inicio,Date fim){
		return dao.getCursos(pessoal, areaAtuacao, posGraduacao, profissional, inicio, fim);
	}
	 

	@Override
	public List<PessoalCursoProfissional> findByCurso(Long cursoProfissional) {
		return dao.findByCurso(cursoProfissional);
	}


	/**
	 * Validar:
	 *  Deve ser cadastrado ao menos uma competencia.
	 *  Deve ser cadastrado ao menos um servidor
	 *  
	 */
	private void validarDados(List<PessoalCursoProfissional> listaPessoaCurso,
			List<CompetenciaCurso> listaCompetencias) 
			throws SRHRuntimeException {

		if (listaCompetencias == null || listaCompetencias.size() == 0)
			throw new SRHRuntimeException("Nenhuma competência cadastrada, é obrigatório pelo menos uma competência.");

		if (listaPessoaCurso == null || listaPessoaCurso.size() == 0)
			throw new SRHRuntimeException("Nenhum servidor cadastrado, é obrigatório pelo menos um servidor.");

	}


	public void setDAO(CursoServidorDAO dao) {this.dao = dao;}
	public void setCompetenciaCursoService(CompetenciaCursoService competenciaCursoService) {this.competenciaCursoService = competenciaCursoService;}


	@Override
	public int count(Date inicio, Date fim, boolean areaAtuacao, boolean posGraduacao, boolean profissional, TipoOcupacao tipoOcupacao, Setor setor, Long idCurso) {
		return dao.count(inicio, fim, areaAtuacao, posGraduacao, profissional, tipoOcupacao, setor, idCurso);
	}


	@Override
	public List<PessoalCursoProfissional> getCursos(Date inicio, Date fim,boolean areaAtuacao, boolean posGraduacao, boolean profissional) {
		return dao.getCursos(inicio, fim, areaAtuacao, posGraduacao, profissional);
	}


	@Override
	public List<PessoalCursoProfissional> search(Date inicio, Date fim, boolean areaAtuacao, boolean posGraduacao, boolean profissional, TipoOcupacao tipoOcupacao, Setor setor, Long idCurso, int first, int rows) {
		return dao.search(inicio, fim, areaAtuacao, posGraduacao, profissional, tipoOcupacao, setor, idCurso, first, rows);
	}

}
