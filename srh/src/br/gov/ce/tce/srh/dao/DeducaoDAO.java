/**
 * 
 */
package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Deducao;

public interface DeducaoDAO {
	
	public int count(Long idPessoal);
	public List<Deducao> search(Long idPessoal, int first, int rows);
	
	public Deducao salvar(Deducao entidade);
	public void excluir(Deducao entidade);

	public List<Deducao> findByPessoal(Long idPessoal);
	
}
