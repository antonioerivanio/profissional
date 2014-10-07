package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.AreaAcademica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface AreaAcademicaService {

	public int count(String descricao);
	public List<AreaAcademica> search(String descricao, int first, int rows);

	public void salvar(AreaAcademica entidade) throws SRHRuntimeException;
	public void excluir(AreaAcademica entidade);

	public List<AreaAcademica> findAll();
	
}
