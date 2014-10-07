package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.FuncionalAnotacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface FuncionalAnotacaoService {

	public int count(Long pessoal);
	public List<FuncionalAnotacao> search(Long pessoal, int first, int rows);

	public FuncionalAnotacao salvar(FuncionalAnotacao entidade) throws SRHRuntimeException;
	public void excluir(FuncionalAnotacao entidade) throws SRHRuntimeException;

	public List<FuncionalAnotacao> findByPessoal(Long idPessoa);

}
