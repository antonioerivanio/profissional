package br.gov.ce.tce.srh.dao;

import java.util.Date;
import java.util.List;

import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.enums.TipoCursoProfissional;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface CursoServidorDAO {
	
	public PessoalCursoProfissional salvar(PessoalCursoProfissional entidade);
	public void excluir(Long cursoProfissional);
	
	public int count(Long area, String curso);
	public List<CursoProfissional> search(Long area, String curso, int first, int rows);	
	
	public int count(Long pessoal, boolean areaAtuacao, TipoCursoProfissional tipoCurso, boolean somentePosGraduacao, Date inicio, Date fim);
	public List<PessoalCursoProfissional> search(Long pessoal, boolean areaAtuacao, TipoCursoProfissional tipoCurso, boolean somentePosGraduacao, Date inicio, Date fim, int first, int rows);	
	
	public int count(Date inicio,Date fim, boolean areaAtuacao, TipoCursoProfissional tipoCurso, boolean somentePosGraduacao, TipoOcupacao tipoOcupacao, Setor setor, Long idCurso);
	public List<PessoalCursoProfissional> search(Date inicio, Date fim, boolean areaAtuacao, TipoCursoProfissional tipoCurso, boolean somentePosGraduacao, TipoOcupacao tipoOcupacao, Setor setor, Long idCurso, int first, int rows);	
	
	public PessoalCursoProfissional getByCurso(Long cursoProfissional);
	public List<PessoalCursoProfissional> findByCurso(Long cursoProfissional);	
	
	public List<CursoProfissional> search(String curso);	
	
	public List<PessoalCursoProfissional> getCursos(Date inicio, Date fim, boolean areaAtuacao, TipoCursoProfissional tipoCurso);
	
}