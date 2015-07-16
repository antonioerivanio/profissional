package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import br.gov.ce.tce.srh.domain.CompetenciaGraduacao;
import br.gov.ce.tce.srh.domain.PessoalCursoAcademica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sca.domain.Usuario;

public interface PessoalCursoAcademicaService {

	public int count(Long pessoa);
	public int count(Usuario usuarioLogado);
	public List<PessoalCursoAcademica> search(Long pessoa, int first, int rows);
	public List<PessoalCursoAcademica> search(Usuario usuarioLogado, int first, int rows);

	public void salvar(PessoalCursoAcademica pessoalCursoAcademica, List<CompetenciaGraduacao> listaCompetencias, 
			List<CompetenciaGraduacao> listacompetenciasExcluidas, Date inicioCompetencia, boolean alterar) throws SRHRuntimeException;

	public void excluir(PessoalCursoAcademica entidade);

	public List<PessoalCursoAcademica> findByPessoa(Long pessoal);
	public List<PessoalCursoAcademica> findByCursoAcademica(Long cursoAcademica);

}
