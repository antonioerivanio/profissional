package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import br.gov.ce.tce.srh.domain.Ferias;
import br.gov.ce.tce.srh.domain.GTR;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface GTRService {

	public int count(Long idPessoal);
	public List<GTR> search(Long idPessoal, int first, int rows);

	public void salvar(GTR entidade) throws SRHRuntimeException;
	public void excluir(GTR entidade);

	public List<GTR> findByPessoal(Long idPessoal);

}
