package br.gov.ce.tce.srh.dao;

import java.util.List;
import br.gov.ce.tce.srh.domain.AfastamentoESocial;
import br.gov.ce.tce.srh.domain.FuncionalCedido;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface FuncionalCedidoDAO {

	public int count(String matricula);
	public List<FuncionalCedido> search(String matricula, int first, int rows);
	
	public FuncionalCedido salvar(FuncionalCedido entidade) throws SRHRuntimeException;	
	public FuncionalCedido getById(Long id);	
	public FuncionalCedido getByCodigo(Long codigo);
	public List<FuncionalCedido> findAll();
	public List<FuncionalCedido> findByCodigo(Long codigo);	
	public void excluir(FuncionalCedido entidade); 

}
