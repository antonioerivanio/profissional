package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.AbonoPermanencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface AbonoPermanenciaService {

	public void salvar (AbonoPermanencia entidade, boolean alterar) throws SRHRuntimeException;
	
	public void excluir(AbonoPermanencia entidade);
	
	public List<AbonoPermanencia> findAll();
	
	public AbonoPermanencia getById (Long id);
	
	public AbonoPermanencia getByPessoalId (Long pessoalId);

	public List<AbonoPermanencia> search(String cpf, int first, int rows);	
	
}
