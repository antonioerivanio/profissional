package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Especialidade;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface EspecialidadeService {

	public int count(String descricao);
	public List<Especialidade> search(String descricao, int first, int rows);

	public void salvar(Especialidade entidade) throws SRHRuntimeException;
	public void excluir(Especialidade entidade);

	public Especialidade getById(Long id);

	public List<Especialidade> findByDescricao(String descricao);
	public List<Especialidade> findAll();

}
