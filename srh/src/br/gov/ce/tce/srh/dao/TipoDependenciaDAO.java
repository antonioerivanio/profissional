package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.TipoDependencia;

public interface TipoDependenciaDAO {

	public int count(String descricao);
	public List<TipoDependencia> search(String descricao, int first, int rows);

	public TipoDependencia salvar(TipoDependencia entidade);
	public void excluir(TipoDependencia entidade);

	public TipoDependencia getByDescricao(String descricao);
	
	public List<TipoDependencia> findAll();

}
