package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * Referente a tabela: TB_PESSOAJURIDICA
 * 
 * @since : Dez 08, 2021, 09:55:00 AM
 * @author : marcelo.viana@tce.ce.gov.br
 *
 */

public interface PessoaJuridicaService {
	
	public int count(String cnpj);
	public List<PessoaJuridica> search(String cnpj, int first, int rows);
	
	public void salvar(PessoaJuridica entidade) throws SRHRuntimeException;
	public void excluir(PessoaJuridica entidade);
	
	public List<PessoaJuridica> findAll();
}
