/**
 * 
 */
package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Averbacao;

public interface AverbacaoDAO {

	public int count(Long idPessoal);
	public List<Averbacao> search(Long idPessoal, int first, int rows);

	public Averbacao salvar(Averbacao entidade);
	public void excluir(Averbacao entidade);

	public List<Averbacao> findByPessoal(Long idPessoal);
	
}
