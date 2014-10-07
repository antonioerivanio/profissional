package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Escolaridade;

public interface EscolaridadeDAO {
	
	public int count(String descricao);
	public List<Escolaridade> search(String descricao, int first, int rows);

	public Escolaridade salvar(Escolaridade entidade);
	public void excluir(Escolaridade entidade);

	public Escolaridade getByDescricao(String descricao);
	public Escolaridade getByOrdem(Long ordem);

	public List<Escolaridade> findAll();
	
}
