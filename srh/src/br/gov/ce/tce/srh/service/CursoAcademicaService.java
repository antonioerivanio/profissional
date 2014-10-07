package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CursoAcademica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface CursoAcademicaService {

	public int count(String descricao);
	public int count(Long area, String descricao);

	public List<CursoAcademica> search(String descricao, int first, int rows);
	public List<CursoAcademica> search(Long area, String descricao, int first, int rows);

	public void salvar(CursoAcademica entidade) throws SRHRuntimeException;
	public void excluir(CursoAcademica entidade);

	public List<CursoAcademica> findAll();

}
