package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.CompetenciaSetorial;
import br.gov.ce.tce.srh.domain.sapjava.Setor;

public interface CompetenciaSetorialDAO {
	
	public int count(Setor setor, Long tipo);
	public int count(Setor setor);
	public List<CompetenciaSetorial> search(Setor setor, Long tipo, int first, int rows);
	public List<CompetenciaSetorial> search(Setor setor, int first, int rows);
	
	public CompetenciaSetorial salvar(CompetenciaSetorial entidade);
	public void excluir(CompetenciaSetorial entidade);

	public CompetenciaSetorial getById(Long id);	

	public List<CompetenciaSetorial> findAll();

}
