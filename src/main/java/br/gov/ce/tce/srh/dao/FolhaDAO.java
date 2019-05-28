package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Folha;

public interface FolhaDAO {

	public int count(String descricao);
	public List<Folha> search(String descricao, int first, int rows);

	public Folha salvar(Folha entidade);
	public void excluir(Folha entidade);

	public Folha getByCodigo(String codigo);
	public Folha getByDescricao(String descricao);

	public List<Folha> findByAtivo(Boolean ativo);

}
