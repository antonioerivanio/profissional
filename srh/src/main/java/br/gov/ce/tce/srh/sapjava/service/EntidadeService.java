package br.gov.ce.tce.srh.sapjava.service;

import java.util.List;

import br.gov.ce.tce.srh.sapjava.domain.Entidade;


public interface EntidadeService {
	
	public List<Entidade> findAll();
	public List<Entidade> listaOrgaoOrigemComRestricaoTipoEsfera();
	
}
