package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.EstadoCivil;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface EstadoCivilService {

	public int count(String descricao);
	public List<EstadoCivil> search(String descricao, int first, int rows);

	public void salvar(EstadoCivil entidade) throws SRHRuntimeException;
	public void excluir(EstadoCivil entidade);

	public List<EstadoCivil> findAll();

}
