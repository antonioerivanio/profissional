package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.FuncionalAnotacao;

public interface FuncionalAnotacaoDAO {

	public int count(Long pessoal);
	public List<FuncionalAnotacao> search(Long pessoal, int first, int rows);

	public FuncionalAnotacao salvar(FuncionalAnotacao entidade);
	public void excluir(FuncionalAnotacao entidade);

	public List<FuncionalAnotacao> findByPessoal(Long idPessoa);

}
