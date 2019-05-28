package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetor;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface CategoriaFuncionalSetorService {

	public int count(Setor setor);
	
	public List<CategoriaFuncionalSetor> search(Setor setor, int first, int rows);

	public CategoriaFuncionalSetor salvar(CategoriaFuncionalSetor entidade);
	
	public void excluir(CategoriaFuncionalSetor entidade);

	public CategoriaFuncionalSetor findById(Long id);

	public List<CategoriaFuncionalSetor> findAll();
	
	public List<CategoriaFuncionalSetor> findBySetor(Setor setor);

}
