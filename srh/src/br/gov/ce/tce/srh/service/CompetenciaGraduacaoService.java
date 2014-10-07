package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CompetenciaGraduacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface CompetenciaGraduacaoService {

	public void salvar(CompetenciaGraduacao entidade) throws SRHRuntimeException;
	public void excluir(Long pessoa, Long curso);
	public void excluir(Long pessoa, Long curso, Long competencia);

	public CompetenciaGraduacao getByPessoalCompetencia(Long pessoal, Long competencia);

	public List<CompetenciaGraduacao> findByPessoaCurso(Long pessoa, Long curso);

}
