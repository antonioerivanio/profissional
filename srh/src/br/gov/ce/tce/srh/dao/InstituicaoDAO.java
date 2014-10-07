package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Instituicao;

public interface InstituicaoDAO {

	public int count(String descricao);
	public List<Instituicao> search(String descricao, int first, int rows);

	public Instituicao salvar(Instituicao entidade);
	public void excluir(Instituicao entidade);

	public Instituicao getByDescricao(String descricao);

	public List<Instituicao> findAll();
	
}
