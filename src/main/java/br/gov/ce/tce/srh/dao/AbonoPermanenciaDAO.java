package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.AbonoPermanencia;

public interface AbonoPermanenciaDAO {

	public AbonoPermanencia salvar (AbonoPermanencia entidade);
	
	public void excluir(AbonoPermanencia entidade);
	
	public List<AbonoPermanencia> findAll();
	
	public AbonoPermanencia getById (Long id);
	
	public AbonoPermanencia getByPessoalId (Long pessoalId);

	public List<AbonoPermanencia> search(String cpf, int first, int rows);
	
}