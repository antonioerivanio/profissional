package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.TipoTempoServico;

public interface TipoTempoServicoService {
	
	public TipoTempoServico getById(Long id);

	public List<TipoTempoServico> findAll();

}
