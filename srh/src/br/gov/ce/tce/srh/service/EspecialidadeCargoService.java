package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.EspecialidadeCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface EspecialidadeCargoService {

	public void salvar(EspecialidadeCargo entidade) throws SRHRuntimeException;

	public void excluir(EspecialidadeCargo entidade);
	public void excluirAll(Long ocupacao);

	public List<EspecialidadeCargo> findByOcupacao(Long ocupacao);
	
	public EspecialidadeCargo getById(Long id);

}
