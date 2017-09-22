package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.TipoBeneficio;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface TipoBeneficioService {

	public int count(String descricao);
	public List<TipoBeneficio> search(String descricao, int first, int rows);

	public void salvar(TipoBeneficio entidade) throws SRHRuntimeException;
	public void excluir(TipoBeneficio entidade);

}
