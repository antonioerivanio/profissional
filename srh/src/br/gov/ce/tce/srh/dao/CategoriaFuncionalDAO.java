package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.CategoriaFuncional;

public interface CategoriaFuncionalDAO {

	public CategoriaFuncional salvar(CategoriaFuncional entidade);

	public List<CategoriaFuncional> buscarCategorias();

	public int count(String descricaoCategoria);

	public List<CategoriaFuncional> search(String descricaoCategoria,
			int first, int rows);

	public List<CategoriaFuncional> findAll();

	public void excluir(CategoriaFuncional entidade);

	public CategoriaFuncional getById(Long id);
}
