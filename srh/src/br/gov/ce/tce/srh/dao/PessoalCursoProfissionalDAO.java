package br.gov.ce.tce.srh.dao;

import java.util.Date;
import java.util.List;

import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.domain.PessoalCursoProfissional;

public interface PessoalCursoProfissionalDAO {

	public int count(Long area, String curso);
	public int count(Long pessoal, Date inicio, Date fim, boolean areaAtuacao);
	public List<CursoProfissional> search(Long area, String curso, int first, int rows);

	public PessoalCursoProfissional salvar(PessoalCursoProfissional entidade);
	public void excluir(Long cursoProfissional);

	public PessoalCursoProfissional getByCurso(Long cursoProfissional);

	public List<PessoalCursoProfissional> findByCurso(Long cursoProfissional);
	public List<PessoalCursoProfissional> search(Long pessoal, Date inicio, Date fim, boolean areaAtuacao, int first, int rows);
	public List<PessoalCursoProfissional> getCursos(Long pessoal, Date inicio, Date fim, boolean areaAtuacao);

}
