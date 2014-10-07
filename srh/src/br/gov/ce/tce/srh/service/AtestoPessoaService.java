/**
 * 
 */
package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.AtestoPessoa;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface AtestoPessoaService {

	public int count(Long pessoa);
	public List<AtestoPessoa> search(Long pessoa, int first, int rows);

	public void salvar(AtestoPessoa entidade) throws SRHRuntimeException;
	public void excluir(AtestoPessoa entidade);

	public AtestoPessoa getByPessoalCompetencia(Long pessoal, Long competencia);
	
}
