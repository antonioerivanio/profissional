package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.FuncionalCedido;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface FuncionalCedidoService {

	
	/**
	 * Validar os dados antes de salvar
	 * @autor erivanio.cruz
	 * @param entidade
	 * @return TRUE|FALSE
	 */
	public boolean isOk(FuncionalCedido entidade);
	
	public FuncionalCedido salvar(FuncionalCedido entidade) throws SRHRuntimeException;
	
	public FuncionalCedido getById(Long id);

	public FuncionalCedido getByCodigo(Long codigo);

	public List<FuncionalCedido> findAll();

	public List<FuncionalCedido> findByCodigo(Long codigo);

}
