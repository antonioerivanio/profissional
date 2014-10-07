package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.MotivoDependencia;

public interface MotivoDependenciaDAO {

	public int count(String descricao);
	public List<MotivoDependencia> search(String descricao, int first, int rows);

	public MotivoDependencia salvar(MotivoDependencia entidade);
	public void excluir(MotivoDependencia entidade);

	public MotivoDependencia getByDescricao(String descricao);

}
