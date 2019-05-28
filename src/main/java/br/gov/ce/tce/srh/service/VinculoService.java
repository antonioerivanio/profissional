package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Vinculo;

public interface VinculoService {

	public Vinculo getById(Long id);

	public List<Vinculo> findAll();

}
