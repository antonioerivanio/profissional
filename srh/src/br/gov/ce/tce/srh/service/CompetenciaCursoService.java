package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CompetenciaCurso;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface CompetenciaCursoService {

	public void salvar(CompetenciaCurso entidade) throws SRHRuntimeException;
	public void excluir(Long curso);

	public CompetenciaCurso getByPessoalCompetencia(Long pessoal, Long competencia);

	public List<CompetenciaCurso> findByCurso(Long curso);

}
