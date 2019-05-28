package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.PessoalCursoAcademica;
import br.gov.ce.tce.srh.sca.domain.Usuario;

public interface PessoalCursoAcademicaDAO {

	public int count(Long pessoa);
	public int count(Usuario usuarioLogado);
	public List<PessoalCursoAcademica> search(Long pessoa, int first, int rows);
	public List<PessoalCursoAcademica> search(Usuario usuarioLogado, int first,	int rows);

	public PessoalCursoAcademica salvar(PessoalCursoAcademica entidade);
	public void excluir(PessoalCursoAcademica entidade);

	public PessoalCursoAcademica getByCursoPessoa(Long curso, Long pessoal);

	public List<PessoalCursoAcademica> findByPessoa(Long pessoal);
	public List<PessoalCursoAcademica> findByCursoAcademica(Long cursoAcademica);

}
