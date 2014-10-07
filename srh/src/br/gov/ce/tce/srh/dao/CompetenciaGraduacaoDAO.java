package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.CompetenciaGraduacao;

public interface CompetenciaGraduacaoDAO {

	public void salvar(CompetenciaGraduacao entidade);
	public void excluir(Long pessoal, Long cursoAcademica);
	public void excluir(Long pessoa, Long curso, Long competencia);

	public CompetenciaGraduacao getById(Long id);

	public List<CompetenciaGraduacao> findByPessoaCurso(Long pessoa, Long curso);
	public CompetenciaGraduacao getByPessoalCompetencia(Long pessoal, Long competencia);


}
