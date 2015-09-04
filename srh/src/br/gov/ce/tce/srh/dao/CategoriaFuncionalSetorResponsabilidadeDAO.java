package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetor;
import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetorResponsabilidade;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface CategoriaFuncionalSetorResponsabilidadeDAO {

	public int count(Setor setor);
	public List<CategoriaFuncionalSetorResponsabilidade> search(Setor setor, int first, int rows);
	
	public CategoriaFuncionalSetorResponsabilidade salvar(CategoriaFuncionalSetorResponsabilidade entidade);
	public void excluir(CategoriaFuncionalSetorResponsabilidade entidade);

	public CategoriaFuncionalSetorResponsabilidade findById(Long id);

	public List<CategoriaFuncionalSetorResponsabilidade> findAll();
	
	public List<CategoriaFuncionalSetorResponsabilidade> findBySetor(Setor setor);
	
	public List<CategoriaFuncionalSetorResponsabilidade> findAtivaByCategoriaFuncionalSetor(CategoriaFuncionalSetor categoriaFuncionalSetor);
	
	public List<CategoriaFuncionalSetorResponsabilidade> findByCategoriaFuncionalSetor(CategoriaFuncionalSetor categoriaFuncionalSetor);
	
}
