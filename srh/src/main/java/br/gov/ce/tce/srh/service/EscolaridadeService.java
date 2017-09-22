package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Escolaridade;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface EscolaridadeService {

	public int count(String descricao);
	public List<Escolaridade> search(String descricao, int first, int rows);

	public void salvar(Escolaridade entidade) throws SRHRuntimeException;
	public void excluir(Escolaridade entidade);

	public List<Escolaridade> findAll();

}
