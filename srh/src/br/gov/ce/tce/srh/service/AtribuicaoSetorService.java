package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.AtribuicaoSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface AtribuicaoSetorService {
	
	public int count(Setor setor, int opcaoAtiva);
	public List<AtribuicaoSetor> search(Setor setor, int opcaoAtiva, int first, int rows);
	
	public void salvar(AtribuicaoSetor entidade) throws SRHRuntimeException;
	
	public void excluir(AtribuicaoSetor entidade);

	public List<AtribuicaoSetor> findAll();

	public AtribuicaoSetor findById(Long id);

	public List<AtribuicaoSetor> findBySetor(Setor setor);

}
