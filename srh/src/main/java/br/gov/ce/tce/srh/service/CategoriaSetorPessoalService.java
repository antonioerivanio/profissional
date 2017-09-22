package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CategoriaSetorPessoal;

public interface CategoriaSetorPessoalService {

	public void salvar(CategoriaSetorPessoal entidade);

	public void excluir(CategoriaSetorPessoal entidade);

	public int count(Long pessoa);

	public List<CategoriaSetorPessoal> search(Long pessoa, int first, int rows);

}
