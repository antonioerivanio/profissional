package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Especialidade;
import br.gov.ce.tce.srh.domain.OrientacaoCargo;

public interface OrientacaoCargoDAO {

	public int count(String descricao);
	
	public List<OrientacaoCargo> search(String descricao, int first, int rows);
	
	public OrientacaoCargo salvar(OrientacaoCargo entidade);

	public void excluir(OrientacaoCargo entidade);
	
	public List<Especialidade> findAll();
	
	public List<OrientacaoCargo> findByEspecialidade(Long especialidade);

}
