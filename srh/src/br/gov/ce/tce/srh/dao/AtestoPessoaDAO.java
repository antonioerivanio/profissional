/**
 * 
 */
package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.AtestoPessoa;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author joel.barbosa
 *
 */
public interface AtestoPessoaDAO {
	
	public int count(Long pessoa);
	public List<AtestoPessoa> search(Long pessoa, int first, int rows);

	public AtestoPessoa salvar(AtestoPessoa entidade) throws SRHRuntimeException;
	public void excluir(AtestoPessoa entidade);

	public List<AtestoPessoa> findByPessoaCompetencia(Long idPessoa, Long idCompetencia);

	public AtestoPessoa getByPessoalCompetencia(Long pessoal, Long competencia);
	
}
