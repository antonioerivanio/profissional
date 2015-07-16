package br.gov.ce.tce.srh.dao;

import java.util.Date;
import java.util.List;

import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface CursoServidorDAO {

	public int count(Long area, String curso);
	public int count(Long pessoal, boolean areaAtuacao, boolean posGraduacao, boolean profissional, Date inicio, Date fim);
	public int count(Date inicio,Date fim, boolean areaAtuacao,boolean posGraduacao, boolean profissional, TipoOcupacao tipoOcupacao, Setor setor);
	
	public PessoalCursoProfissional salvar(PessoalCursoProfissional entidade);
	public void excluir(Long cursoProfissional);

	public PessoalCursoProfissional getByCurso(Long cursoProfissional);
	public List<PessoalCursoProfissional> findByCurso(Long cursoProfissional);
	
	public List<CursoProfissional> search(Long area, String curso, int first, int rows);
	public List<PessoalCursoProfissional> search(Long pessoal, boolean areaAtuacao, boolean posGraduacao, boolean profissional, Date inicio, Date fim, int first, int rows);
	public List<PessoalCursoProfissional> search(Date inicio, Date fim, boolean areaAtuacao, boolean posGraduacao, boolean profissional, TipoOcupacao tipoOcupacao, Setor setor, int first, int rows);
	
	public List<PessoalCursoProfissional> getCursos(Long pessoal, boolean areaAtuacao, boolean posGraduacao, boolean profissional, Date inicio, Date fim) ;
	public List<PessoalCursoProfissional> getCursos(Date inicio, Date fim, boolean areaAtuacao, boolean posGraduacao, boolean profissional);
	//public List<PessoalCursoProfissional> getCursos(Long pessoal, boolean areaAtuacao, Date inicio,Date fim);

}