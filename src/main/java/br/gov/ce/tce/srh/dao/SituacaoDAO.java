package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Situacao;

public interface SituacaoDAO {

	public Situacao getById(Long id);
	
	public List<Situacao> findAll();

}
