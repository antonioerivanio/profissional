package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.RepresentacaoValor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface RepresentacaoValorService {

	public int count(Long cargo);
	public List<RepresentacaoValor> search(Long cargo, int first, int rows);

	public void salvar(RepresentacaoValor entidade) throws SRHRuntimeException;
	public void excluir(RepresentacaoValor entidade);

}
