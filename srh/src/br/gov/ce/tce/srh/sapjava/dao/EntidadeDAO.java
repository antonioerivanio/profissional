package br.gov.ce.tce.srh.sapjava.dao;

import java.util.List;

import br.gov.ce.tce.srh.sapjava.domain.Entidade;

public interface EntidadeDAO {

	public List<Entidade> findAll();
	public List<Entidade> listaOrgaoOrigemComRestricaoTipoEsfera();

}
