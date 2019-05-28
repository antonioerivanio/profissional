package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Acrescimo;

public interface AcrescimoDAO {
	
	public int count(Long idPessoal);
	public List<Acrescimo> search(Long idPessoal, int first, int rows);
	
	public Acrescimo salvar(Acrescimo entidade);
	public void excluir(Acrescimo entidade);

	public List<Acrescimo> findByPessoal(Long idPessoal);
	
}
