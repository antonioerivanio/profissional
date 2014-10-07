package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Municipio;

public interface MunicipioDAO {

	public List<Municipio> findByUF(String uf);

}
