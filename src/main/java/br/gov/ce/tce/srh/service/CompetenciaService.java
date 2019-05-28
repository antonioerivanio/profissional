package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface CompetenciaService {

	public void salvar(Competencia entidade) throws SRHRuntimeException;
	public void excluir(Competencia entidade) throws SRHRuntimeException;

	public int count(String descricao);
	public List<Competencia> search(String descricao, int first, int rows);

	public Competencia getById(Long id);
	public List<Competencia> findAll();
	List<Competencia> search(String tipo);
	public List<Competencia> findByTipo(Long tipo);

}
