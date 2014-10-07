package br.gov.ce.tce.srh.service.sapjava;

import java.util.List;

import br.gov.ce.tce.srh.domain.sapjava.Setor;

public interface SetorService {

	public List<Setor> findAll();

	public List<Setor> findTodosAtivos();
	
	// TODO - remover
	public Setor getById(Long id);

}
