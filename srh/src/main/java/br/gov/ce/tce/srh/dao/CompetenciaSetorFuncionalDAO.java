package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.CategoriaFuncional;
import br.gov.ce.tce.srh.domain.CompetenciaSetorFuncional;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface CompetenciaSetorFuncionalDAO {

	public int count(Setor setor, String tipo, CategoriaFuncional categoriaFuncional);
	public List<CompetenciaSetorFuncional> search(Setor setor, String tipo, CategoriaFuncional categoriaFuncional, int first, int rows);

	public CompetenciaSetorFuncional salvar(CompetenciaSetorFuncional entidade);
	public void excluir(CompetenciaSetorFuncional entidade);

	
}