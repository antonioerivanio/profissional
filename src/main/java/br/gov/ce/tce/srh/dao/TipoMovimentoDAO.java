package br.gov.ce.tce.srh.dao;

import java.util.List;
import br.gov.ce.tce.srh.domain.TipoMovimento;

public interface TipoMovimentoDAO {

	public int count(String descricao);
	public List<TipoMovimento> search(String descricao, int first, int rows);

	public TipoMovimento salvar(TipoMovimento entidade);
	public void excluir(TipoMovimento entidade);

	public TipoMovimento getById(Long id);
	public TipoMovimento getByDescricao(String descricao);

	public List<TipoMovimento> findByTipo(Long tipo);
	public List<TipoMovimento> findByDescricaoForId(Long id);
	
	public List<TipoMovimento> findAll();

}
