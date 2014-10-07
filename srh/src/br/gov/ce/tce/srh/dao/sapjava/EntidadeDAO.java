package br.gov.ce.tce.srh.dao.sapjava;

import java.util.List;

import br.gov.ce.tce.srh.domain.sapjava.Entidade;

public interface EntidadeDAO {

	public List<Entidade> findAll();
	public List<Entidade> listaOrgaoOrigemComRestricaoTipoEsfera();

}
