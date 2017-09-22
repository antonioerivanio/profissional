package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Especialidade;
import br.gov.ce.tce.srh.domain.OrientacaoCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface OrientacaoCargoService {

	public int count(String descricao);
	public List<OrientacaoCargo> search(String descricao, int first, int rows);
	
	public void salvar(OrientacaoCargo entidade) throws SRHRuntimeException;

	public void excluir(OrientacaoCargo entidade);
	
	public List<Especialidade> findAll();
	
	public List<OrientacaoCargo> findByEspecialidade(Long especialidade);

}