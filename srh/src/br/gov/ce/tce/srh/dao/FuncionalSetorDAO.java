package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.FuncionalSetor;

public interface FuncionalSetorDAO {

	public int count(Long pessoal);
	public List<FuncionalSetor> search(Long pessoal, int first, int rows);

	public FuncionalSetor salvar(FuncionalSetor entidade);

	public void excluir(FuncionalSetor entidade);
	public void excluirAll(Long idFuncional);

	public List<FuncionalSetor> findByPessoal(Long idPessoa);
	
	public FuncionalSetor getAtivoByFuncional(Long idFuncional);
	
	public List<FuncionalSetor> findByFuncional(Long idFuncional);

}
