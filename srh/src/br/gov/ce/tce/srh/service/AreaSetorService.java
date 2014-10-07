package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.AreaSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface AreaSetorService {

	public int count(Long setor, String descricao);
	public List<AreaSetor> search(Long setor, String descricao, int first, int rows);

	public void salvar(AreaSetor entidade) throws SRHRuntimeException;
	public void excluir(AreaSetor entidade);

	public List<AreaSetor> findBySetor(Long setor);

}
