package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.CodigoCategoria;

public interface CodigoCategoriaDAO {

	public CodigoCategoria getById(Long id);
	public CodigoCategoria getByCodigo(Long codigo);

	public List<CodigoCategoria> findAll();
	public List<CodigoCategoria> findByCodigo(Long codigo);

}
