package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Instituicao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface InstituicaoService {

	public int count(String descricao);
	public List<Instituicao> search(String descricao, int first, int rows);

	public void salvar(Instituicao entidade) throws SRHRuntimeException;
	public void excluir(Instituicao entidade);

	public List<Instituicao> findAll();

}
