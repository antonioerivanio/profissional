package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface CursoProfissionalService {

	public int count(String descricao);
	public int count(Long id, String descricao);

	public List<CursoProfissional> search(String descricao, int first, int rows);
	public List<CursoProfissional> search(Long area, String descricao, int first, int rows);

	public void salvar(CursoProfissional entidade) throws SRHRuntimeException;
	public void excluir(CursoProfissional entidade);

	public CursoProfissional getById(Long id);

	public List<CursoProfissional> findByArea(Long area);
	public List<CursoProfissional> findAll();

}
