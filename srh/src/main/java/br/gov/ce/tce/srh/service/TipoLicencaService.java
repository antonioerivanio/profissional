package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.TipoLicenca;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface TipoLicencaService {

	public int count(String descricao);
	public List<TipoLicenca> search(String descricao, int first, int rows);

	public void salvar(TipoLicenca entidade) throws SRHRuntimeException;
	public void excluir(TipoLicenca entidade);

	public TipoLicenca getById(Long id);

	public List<TipoLicenca> findAll();
	
}
