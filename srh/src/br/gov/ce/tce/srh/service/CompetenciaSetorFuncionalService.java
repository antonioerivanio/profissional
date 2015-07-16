package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CategoriaFuncional;
import br.gov.ce.tce.srh.domain.CompetenciaSetorFuncional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface CompetenciaSetorFuncionalService {

	public void salvar(CompetenciaSetorFuncional entidade, Setor setor) throws SRHRuntimeException;
	public void excluir(CompetenciaSetorFuncional entidade) throws SRHRuntimeException;

	public int count(Setor setor, String tipo, CategoriaFuncional categoriaFuncional);
	public List<CompetenciaSetorFuncional> search(Setor setor, String tipo, CategoriaFuncional categoriaFuncional, int first, int rows);

}
