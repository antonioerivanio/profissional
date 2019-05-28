package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Especialidade;

public interface EspecialidadeDAO {

	public int count(String descricao);
	public List<Especialidade> search(String descricao, int first, int rows);

	public Especialidade salvar(Especialidade entidade);
	public void excluir(Especialidade entidade);

	public Especialidade getById(Long id);
	public Especialidade getByDescricao(String descricao);

	public List<Especialidade> findByDescricao(String descricao);
	public List<Especialidade> findAll();

}
