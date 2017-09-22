package br.gov.ce.tce.srh.sapjava.service;

import java.util.List;

import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface SetorService {

	public List<Setor> findAll();

	public List<Setor> findTodosAtivos();
	
	// TODO - remover
	public Setor getById(Long id);

}
