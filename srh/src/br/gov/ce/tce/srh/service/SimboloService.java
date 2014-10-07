package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Simbolo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface SimboloService {

	public void salvar(Simbolo entidade) throws SRHRuntimeException;

	public void excluir(Simbolo entidade);
	public void excluirAll(Long ocupacao);

	public List<Simbolo> findByOcupacao(Long ocupacao);

}
