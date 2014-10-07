package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.CursoAcademica;

public interface CursoAcademicaDAO {

	public int count(String descricao);
	public int count(Long area, String descricao);

	public List<CursoAcademica> search(String descricao, int first, int rows);
	public List<CursoAcademica> search(Long area, String descricao, int first, int rows);

	public CursoAcademica salvar(CursoAcademica entidade);
	public void excluir(CursoAcademica entidade);

	public CursoAcademica getById(Long id);
	public CursoAcademica getByAreaDescricao(Long area, String descricao);

	public List<CursoAcademica> findAll();

}
