/**
 * 
 */
package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Ferias;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface FeriasService {

	public int count(Long idPessoal);
	public List<Ferias> search(Long idPessoal, int first, int rows);

	public void salvar(Ferias entidade) throws SRHRuntimeException;
	public void excluir(Ferias entidade);

	public List<Ferias> findByPessoal(Long idPessoal);
	public List<Ferias> findByPessoalTipo(Long idPessoal, Long tipo);
	
	public Ferias findMaisRecenteByPessoal(Long idPessoal);
	
}
