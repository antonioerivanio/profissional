package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CategoriaFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface CadastroCategoriaFuncionalService {

	public void salvar(CategoriaFuncional categoria) throws SRHRuntimeException;

	public List<CategoriaFuncional> listarCategoriasFuncionais();

	public int count(String descricaoCategoria);

	public List<CategoriaFuncional> search(String descricaoCategoria,
			int first, int rows);

	public List<CategoriaFuncional> findAll();

	public void excluir(CategoriaFuncional entidade);

	public CategoriaFuncional getById(Long id);

	public List<CategoriaFuncional> findBySetor(Setor setor);
}
