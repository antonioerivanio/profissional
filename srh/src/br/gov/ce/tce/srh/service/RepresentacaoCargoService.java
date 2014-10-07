package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.RepresentacaoCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface RepresentacaoCargoService {

	public int count(String nomenclatura);
	public List<RepresentacaoCargo> search(String nomenclatura, int first, int rows);

	public void salvar(RepresentacaoCargo entidade) throws SRHRuntimeException;
	public void excluir(RepresentacaoCargo entidade);

	public List<RepresentacaoCargo> findAll();

}
