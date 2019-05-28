package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.TipoDocumento;

public interface TipoDocumentoDAO {

	public int count(String descricao);
	public List<TipoDocumento> search(String descricao, int first, int rows);

	public TipoDocumento salvar(TipoDocumento entidade);
	public void excluir(TipoDocumento entidade);

	public TipoDocumento getByDescricao(String descricao);

	public List<TipoDocumento> findByEsfera(Long esfera);
	public List<TipoDocumento> findByDocFuncional(boolean docFuncional);
	public List<TipoDocumento> findAll();

}
