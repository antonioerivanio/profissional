package br.gov.ce.tce.srh.dao.sapjava;

import java.util.List;

import br.gov.ce.tce.srh.domain.sapjava.Setor;

public interface SetorDAO {

	public List<Setor> findAll();
	
	public List<Setor> findTodosAtivos();

	// TODO - remover
	public Setor getById(Long id);

}
