package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Competencia;

public interface CompetenciaDAO {

	public int count(String descricao);
	public List<Competencia> search(String descricao, int first, int rows);

	public Competencia salvar(Competencia entidade);
	public void excluir(Competencia entidade);

	public Competencia getById(Long id);
	public Competencia getByDescricao(String descricao);

	public List<Competencia> findAll();
	List<Competencia> search(String tipo);
	
	public List<Competencia> findByTipo(Long tipo);
	
}