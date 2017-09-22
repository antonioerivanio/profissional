/**
 * 
 */
package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Ferias;

/**
 * 
 * @author joel.barbosa
 *
 */
public interface FeriasDAO {
	
	public int count(Long idPessoal);
	public List<Ferias> search(Long idPessoal, int first, int rows);
	
	public Ferias salvar(Ferias entidade);
	public void excluir(Ferias entidade);

	public List<Ferias> findByPessoal(Long idPessoal);
	public List<Ferias> findByPessoalTipo(Long idPessoal, Long tipo);
	
	public List<Ferias> findByPessoalPeriodoReferencia(Long idPessoal, Long periodo, Long anoReferencia, Long tipo);
	public List<Ferias> findByPessoalPeriodoReferencia(Long idPessoal, Long periodo, Long anoReferencia);
	public Ferias findMaisRecenteByPessoal(Long idPessoal);

}
