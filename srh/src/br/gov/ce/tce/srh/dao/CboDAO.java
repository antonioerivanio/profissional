package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Cbo;

public interface CboDAO {

	public Cbo getById(Long id);
	public Cbo getByCodigo(String codigo);

	public List<Cbo> findAll();
	public List<Cbo> findByNivel(Long nivel);
	public List<Cbo> findByNivelCodigo(Long nivel, String codigo);

}
