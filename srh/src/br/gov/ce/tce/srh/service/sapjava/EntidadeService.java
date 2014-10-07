package br.gov.ce.tce.srh.service.sapjava;

import java.util.List;

import br.gov.ce.tce.srh.domain.sapjava.Entidade;


public interface EntidadeService {
	
	public List<Entidade> findAll();
	public List<Entidade> listaOrgaoOrigemComRestricaoTipoEsfera();
	
}
