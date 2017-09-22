/**
 * 
 */
package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Averbacao;

public interface AverbacaoService {

	public int count(Long idPessoal);
	public List<Averbacao> search(Long idPessoal, int first, int rows);

	public void salvar(Averbacao entidade, boolean periodoValido);
	public void excluir(Averbacao entidade);

	public List<Averbacao> findByPessoal(Long idPessoal);

}
