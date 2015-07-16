package br.gov.ce.tce.srh.sapjava.dao;

import java.util.List;

import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface SetorDAO {

	public List<Setor> findAll();
	
	public List<Setor> findTodosAtivos();

	// TODO - remover
	public Setor getById(Long id);

}
