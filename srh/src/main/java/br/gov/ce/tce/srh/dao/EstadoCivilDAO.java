package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.EstadoCivil;

public interface EstadoCivilDAO {

	public int count(String descricao);
	public List<EstadoCivil> search(String descricao, int first, int rows);

	public EstadoCivil salvar(EstadoCivil entidade);
	public void excluir(EstadoCivil entidade);

	public EstadoCivil getByDescricao(String descricao);

	public List<EstadoCivil> findAll();
	
}
