package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Raca;

public interface RacaDAO {

	public int count(String descricao);
	public List<Raca> search(String descricao, int first, int rows);

	public Raca salvar(Raca entidade);
	public void excluir(Raca entidade);

	public Raca getByDescricao(String descricao);

	public List<Raca> findAll();

}
