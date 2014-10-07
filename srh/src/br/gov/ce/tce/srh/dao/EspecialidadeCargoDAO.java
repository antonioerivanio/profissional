package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.EspecialidadeCargo;

public interface EspecialidadeCargoDAO {

	public EspecialidadeCargo salvar(EspecialidadeCargo entidade);

	public void excluir(EspecialidadeCargo entidade);
	public void excluirAll(Long idOcupacao);

	public List<EspecialidadeCargo> findByOcupacao(Long ocupacao);
	
	public EspecialidadeCargo getById(Long id);

}
