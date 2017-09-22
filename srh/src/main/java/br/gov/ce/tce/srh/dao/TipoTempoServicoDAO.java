package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.TipoTempoServico;

public interface TipoTempoServicoDAO {
	
	public TipoTempoServico getById(Long id);

	public List<TipoTempoServico> findAll();

}
