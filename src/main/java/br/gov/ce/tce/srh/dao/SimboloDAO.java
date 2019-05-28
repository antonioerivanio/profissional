package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Simbolo;

public interface SimboloDAO {

	public Simbolo salvar(Simbolo entidade);

	public void excluir(Simbolo entidade);
	public void excluirAll(Long ocupacao);

	public Simbolo getByOcupacaoSimbolo(Long ocupacao, String simbolo);

	public List<Simbolo> findByOcupacao(Long ocupacao);

}
