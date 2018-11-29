package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Dependente;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface DependenteService {

	public void salvar(Dependente entidade, boolean alterar) throws SRHRuntimeException;
	
	public void excluir(Dependente entidade);
	
	public List<Dependente> findAll();
	
	public Dependente getById (Long id);
	
	public int count(Long idPessoal);

	public List<Dependente> search(Long idPessoal, int first, int rows);
	
	public List<Dependente> find(Dependente dependente);
	
	public List<Dependente> listaParaDarBaixa(List<Dependente> dependentes);
	
	public List<Dependente> findByResponsavel(Long idResponsavel);
	
}
