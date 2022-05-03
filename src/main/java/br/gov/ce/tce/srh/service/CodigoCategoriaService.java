package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CodigoCategoria;

public interface CodigoCategoriaService {

	public CodigoCategoria getById(Long id);
	public CodigoCategoria getByCodigo(Long codigo);

	public List<CodigoCategoria> findAll();	
	public List<CodigoCategoria> findByCodigo(Long codigo);

}
