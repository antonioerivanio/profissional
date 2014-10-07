package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.TipoOcupacao;

public interface TipoOcupacaoService {

	public TipoOcupacao getById(Long id);

	public List<TipoOcupacao> findAll();

}
