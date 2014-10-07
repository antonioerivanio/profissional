package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.TipoDependencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface TipoDependenciaService {

	public int count(String descricao);
	public List<TipoDependencia> search(String descricao, int first, int rows);

	public void salvar(TipoDependencia entidade) throws SRHRuntimeException;
	public void excluir(TipoDependencia entidade);

}
