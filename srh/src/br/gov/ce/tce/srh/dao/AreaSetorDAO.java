package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.AreaSetor;

public interface AreaSetorDAO {

	public int count(Long setor, String descricao);
	public List<AreaSetor> search(Long setor, String descricao, int first, int rows);

	public AreaSetor salvar(AreaSetor entidade);
	public void excluir(AreaSetor entidade);

	public AreaSetor getBySetorDescricao(Long setor, String descricao);

	public List<AreaSetor> findBySetor(Long setor);

}
