package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Dependente;

public interface DependenteDAO {

	public Dependente salvar (Dependente entidade);
	
	public void excluir(Dependente entidade);
	
	public List<Dependente> findAll();
	
	public Dependente getById (Long id);
	
	public int count(Long idPessoal);

	public List<Dependente> search(Long idPessoal, int first, int rows);

	public Dependente findByResponsavelAndDependente(Long idResponsavel, Long idDependente);
	
	public List<Dependente> findByResponsavel(Long idResponsavel);
}