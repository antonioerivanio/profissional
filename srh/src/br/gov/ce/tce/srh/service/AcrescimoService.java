package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Acrescimo;

public interface AcrescimoService {

	public int count(Long idPessoal);
	public List<Acrescimo> search(Long idPessoal, int first, int rows);

	public void salvar(Acrescimo entidade);
	public void excluir(Acrescimo entidade);

	public List<Acrescimo> findByPessoal(Long idPessoal);

}
