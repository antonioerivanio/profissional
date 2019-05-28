package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.FuncionalSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface FuncionalSetorService {

	public int count(Long pessoal);
	public List<FuncionalSetor> search(Long pessoal, int first, int rows);

	public FuncionalSetor salvar(FuncionalSetor entidade) throws SRHRuntimeException;

	public void excluir(FuncionalSetor entidade) throws SRHRuntimeException;
	public void excluirAll(Long idFuncional) throws SRHRuntimeException;

	public List<FuncionalSetor> findByPessoal(Long idPessoa);
	
	public FuncionalSetor getAtivoByFuncional(Long idFuncional);

}
