package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.AtribuicaoSetor;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface AtribuicaoSetorDAO {
	
	public int count(Setor setor, int opcaoAtiva);
	public List<AtribuicaoSetor> search(Setor setor, int opcaoAtiva, int first, int rows);
	
	public AtribuicaoSetor salvar(AtribuicaoSetor entidade);
	public void excluir(AtribuicaoSetor entidade);

	public AtribuicaoSetor findById(Long id);

	public List<AtribuicaoSetor> findAll();
	
	public List<AtribuicaoSetor> findBySetor(Setor setor);

}
