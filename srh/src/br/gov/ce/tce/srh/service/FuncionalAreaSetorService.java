package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.FuncionalAreaSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface FuncionalAreaSetorService {

	public int count(Long funcional);
	public List<FuncionalAreaSetor> search(Long funcional, int first, int rows);

	public void salvar(FuncionalAreaSetor entidade) throws SRHRuntimeException;
	public void excluir(FuncionalAreaSetor entidade) throws SRHRuntimeException;

	public List<FuncionalAreaSetor> findByFuncional(Long funcional);
	public List<FuncionalAreaSetor> findByFuncionalComSetorAtual(Long funcional);
	public List<FuncionalAreaSetor> findyByAreaSetor(Long areaSetor);

}
