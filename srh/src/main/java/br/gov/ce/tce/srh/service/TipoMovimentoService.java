package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.TipoMovimento;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface TipoMovimentoService {

	public int count(String descricao);
	public List<TipoMovimento> search(String descricao, int first, int rows);

	public void salvar(TipoMovimento entidade) throws SRHRuntimeException;
	public void excluir(TipoMovimento entidade);

	public TipoMovimento getById(Long id);

	public List<TipoMovimento> findByTipo(Long tipo);
	public List<TipoMovimento> findByDescricaoForId(Long id);
	
	public List<TipoMovimento> findAll();

}
