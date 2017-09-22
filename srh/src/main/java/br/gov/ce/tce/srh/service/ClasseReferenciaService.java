package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.ClasseReferencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface ClasseReferenciaService {

	public int count(Long id);
	public List<ClasseReferencia> search(Long id, int first, int rows);

	public void salvar(ClasseReferencia entidade) throws SRHRuntimeException;
	public void excluir(ClasseReferencia entidade);

	public List<ClasseReferencia> findByCargo(Long cargo);

}
