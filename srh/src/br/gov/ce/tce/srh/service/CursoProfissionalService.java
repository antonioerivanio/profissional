package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface CursoProfissionalService {

	public int count(String descricao, Long idArea, Date inicio, Date fim );

	public List<CursoProfissional> search(String descricao, Long idArea, Date inicio, Date fim, int first, int rows);

	public void salvar(CursoProfissional entidade) throws SRHRuntimeException;
	public void excluir(CursoProfissional entidade);

	public CursoProfissional getById(Long id);

	public List<CursoProfissional> findByArea(Long area);
	public List<CursoProfissional> findAll();

}
