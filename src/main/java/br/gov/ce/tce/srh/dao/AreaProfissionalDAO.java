package br.gov.ce.tce.srh.dao;

import java.util.List;
import br.gov.ce.tce.srh.domain.AreaProfissional;

public interface AreaProfissionalDAO {

	public int count(String descricao);
	public List<AreaProfissional> search(String descricao, int first, int rows);

	public AreaProfissional salvar(AreaProfissional entidade);
	public void excluir(AreaProfissional entidade);

	public AreaProfissional getByDescricao(String descricao);

	public List<AreaProfissional> findAll();

}
