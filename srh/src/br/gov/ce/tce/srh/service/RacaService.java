package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Raca;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface RacaService {

	public int count(String descricao);
	public List<Raca> search(String descricao, int first, int rows);

	public void salvar(Raca entidade) throws SRHRuntimeException;
	public void excluir(Raca entidade);

	public List<Raca> findAll();

}
