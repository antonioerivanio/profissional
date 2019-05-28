package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Rubrica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface RubricaService {

	public int count(String descricao);
	public List<Rubrica> search(String descricao, int first, int rows);

	public void salvar(Rubrica entidade) throws SRHRuntimeException;
	public void excluir(Rubrica entidade);

}
