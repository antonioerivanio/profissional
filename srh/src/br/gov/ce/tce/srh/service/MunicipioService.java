package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Municipio;

public interface MunicipioService {

	public List<Municipio> findByUF(String uf);

}
