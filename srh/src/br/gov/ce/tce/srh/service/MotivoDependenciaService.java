package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.MotivoDependencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface MotivoDependenciaService {

	public int count(String descricao);
	public List<MotivoDependencia> search(String descricao, int first, int rows);

	public void salvar(MotivoDependencia entidade) throws SRHRuntimeException;
	public void excluir(MotivoDependencia entidade);

	public List<MotivoDependencia> findByTipo(Long Tipo);
}
