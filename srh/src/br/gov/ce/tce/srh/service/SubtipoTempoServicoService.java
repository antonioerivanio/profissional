package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.SubtipoTempoServico;

public interface SubtipoTempoServicoService {
	
	public SubtipoTempoServico getById(Long id);
	
	public List<SubtipoTempoServico> findByTipoTempoServico(Long idTipoTempoServico);

	public List<SubtipoTempoServico> findAll();

}
