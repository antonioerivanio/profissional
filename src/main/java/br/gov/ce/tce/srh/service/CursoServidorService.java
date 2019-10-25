package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import br.gov.ce.tce.srh.domain.CompetenciaCurso;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.enums.TipoCursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface CursoServidorService {

	public void salvar(List<PessoalCursoProfissional> listaPessoaCurso, List<CompetenciaCurso> listaCompetencias, boolean alterar) throws SRHRuntimeException;
	public void excluir(Long curso);	
	public int count(Long area, String curso);
	public List<CursoProfissional> search(Long area, String curso, int first, int rows);
	public List<PessoalCursoProfissional> findByCurso(Long cursoProfissional);
	public List<PessoalCursoProfissional> getCursos(Date inicio,Date fim, boolean areaAtuacao, TipoCursoProfissional tipoCurso);
		
	// CursoServidorListBean
	public int count(Long pessoal, boolean areaAtuacao, TipoCursoProfissional tipoCurso, boolean somentePosGraduacao, Date inicio, Date fim);
	public List<PessoalCursoProfissional> search(Long pessoal, boolean areaAtuacao, TipoCursoProfissional tipoCurso, boolean somentePosGraduacao, Date inicio, Date fim, int first, int rows);
	
	// CursoPeriodoListBean
	public int count(Date inicio, Date fim, boolean areaAtuacao, TipoCursoProfissional tipoCurso, boolean somentePosGraduacao, TipoOcupacao tipoOcupacao, Setor setor, Long idCurso);
	public List<PessoalCursoProfissional> search(Date inicio, Date fim, boolean areaAtuacao, TipoCursoProfissional tipoCurso, boolean somentePosGraduacao, TipoOcupacao tipoOcupacao, Setor setor, Long idCurso, int first, int rows);
			
	// CursoConsultaBean
	public List<CursoProfissional> search(String curso);	

}