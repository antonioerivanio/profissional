package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.RepresentacaoValor;

public interface RepresentacaoValorDAO {

	public int count(Long cargo);
	public List<RepresentacaoValor> search(Long cargo, int first, int rows);

	public RepresentacaoValor salvar(RepresentacaoValor entidade);
	public void excluir(RepresentacaoValor entidade);

}
