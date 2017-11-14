package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.CarreiraPessoal;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface CarreiraPessoalService {

	public int count(Long pessoal);
	public List<CarreiraPessoal> search(Long pessoal, int first, int rows);

	public CarreiraPessoal salvar(CarreiraPessoal entidade) throws SRHRuntimeException;
	public void excluir(CarreiraPessoal entidade) throws SRHRuntimeException;

	public List<CarreiraPessoal> findByPessoal(Long idPessoa);

}
