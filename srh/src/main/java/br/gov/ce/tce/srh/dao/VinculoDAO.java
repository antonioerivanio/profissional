package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Vinculo;

public interface VinculoDAO {

	public Vinculo getById(Long id);

	public List<Vinculo> findAll();

}
