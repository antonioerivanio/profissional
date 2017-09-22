package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Folha;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface FolhaService {

	public int count(String descricao);
	public List<Folha> search(String descricao, int first, int rows);

	public void salvar(Folha entidade) throws SRHRuntimeException;
	public void excluir(Folha entidade);

	public List<Folha> findByAtivo(Boolean ativo);

}
