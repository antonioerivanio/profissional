package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.CarreiraPessoal;

public interface CarreiraPessoalDAO {

	public int count(Long pessoal);
	public List<CarreiraPessoal> search(Long pessoal, int first, int rows);

	public CarreiraPessoal salvar(CarreiraPessoal entidade);
	public void excluir(CarreiraPessoal entidade);

	public List<CarreiraPessoal> findByPessoal(Long idPessoa);

}
