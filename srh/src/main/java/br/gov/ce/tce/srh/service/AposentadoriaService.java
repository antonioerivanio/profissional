/**
 * 
 */
package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Aposentadoria;

public interface AposentadoriaService {

	public Aposentadoria salvar(Aposentadoria entidade);
	public void excluir(Aposentadoria entidade);
	
	public int count();
	public List<Aposentadoria> search(Long idPessoal, int first, int rows);
	
}
