package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.ReferenciaFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface ReferenciaFuncionalDAO {

	public int count(String matricula);
	public List<ReferenciaFuncional> search(String matricula, int first, int rows);

	public int count(Long idPessoa);
	public List<ReferenciaFuncional> search(Long idPessoa, int first, int rows);
	
	public void salvar(ReferenciaFuncional entidade) throws SRHRuntimeException;
	public void excluirAll(Long idFuncional) throws SRHRuntimeException;

	public ReferenciaFuncional getById(Long id);
	public ReferenciaFuncional getAtivoByFuncional(Long idFuncional);

	public List<ReferenciaFuncional> findByPessoa(Long idPessoa);
	public List<ReferenciaFuncional> findByFuncional(Long idFuncional);

}
