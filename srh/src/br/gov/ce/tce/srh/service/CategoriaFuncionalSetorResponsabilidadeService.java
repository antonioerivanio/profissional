package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CategoriaFuncionalSetorResponsabilidade;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface CategoriaFuncionalSetorResponsabilidadeService {

	public int count(Setor setor, int opcaoAtiva);	
	public List<CategoriaFuncionalSetorResponsabilidade> search(Setor setor, int opcaoAtiva, int first, int rows);

	public CategoriaFuncionalSetorResponsabilidade salvar(CategoriaFuncionalSetorResponsabilidade entidade);	
	public void excluir(CategoriaFuncionalSetorResponsabilidade entidade);

	public CategoriaFuncionalSetorResponsabilidade findById(Long id);

	public List<CategoriaFuncionalSetorResponsabilidade> findAll();
	
	public List<CategoriaFuncionalSetorResponsabilidade> findBySetor(Setor setor);
	
}
