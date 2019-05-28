package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import br.gov.ce.tce.srh.domain.CompetenciaCurso;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface PessoalCursoProfissionalService {

	public int count(Long area, String curso);
	public int count(Long pessoal, Date inicio, Date fim, boolean areaAtuacao);
	public List<CursoProfissional> search(Long area, String curso, int first, int rows);

	public void salvar(List<PessoalCursoProfissional> listaPessoaCurso, List<CompetenciaCurso> listaCompetencias, boolean alterar) throws SRHRuntimeException;
	public void excluir(Long curso);

	public List<PessoalCursoProfissional> findByCurso(Long cursoProfissional);
	
	public List<PessoalCursoProfissional> search(Long pessoal, Date inicio, Date fim, boolean areaAtuacao, int first, int rows) ;
	public List<PessoalCursoProfissional> getCursos(Long pessoal, Date inicio, Date fim, boolean areaAtuacao) ;
	

}
