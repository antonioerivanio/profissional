package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.TipoDocumento;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface TipoDocumentoService {

	public int count(String descricao);
	public List<TipoDocumento> search(String descricao, int first, int rows);

	public void salvar(TipoDocumento entidade) throws SRHRuntimeException;
	public void excluir(TipoDocumento entidade);

	public List<TipoDocumento> findByEsfera(Long esfera);
	public List<TipoDocumento> findByDocfuncional(boolean docFuncional);
	public List<TipoDocumento> findAll();

}
