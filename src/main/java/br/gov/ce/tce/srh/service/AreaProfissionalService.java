package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.AreaProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface AreaProfissionalService {

	public int count(String descricao);
	public List<AreaProfissional> search(String descricao, int first, int rows);

	public void salvar(AreaProfissional entidade) throws SRHRuntimeException;
	public void excluir(AreaProfissional entidade);

	public List<AreaProfissional> findAll();

}
