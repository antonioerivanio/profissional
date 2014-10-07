package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.CompetenciaCurso;

public interface CompetenciaCursoDAO {

	public void salvar(CompetenciaCurso entidade);
	public void excluir(Long curso);

	public CompetenciaCurso getById(Long id);
	public CompetenciaCurso getByPessoalCompetencia(Long pessoal, Long competencia);

	public List<CompetenciaCurso> findByCurso(Long curso);

}
