package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.RepresentacaoCargo;

public interface RepresentacaoCargoDAO {

	public int count(String nomenclatura);
	public List<RepresentacaoCargo> search(String nomenclatura, int first, int rows);

	public RepresentacaoCargo salvar(RepresentacaoCargo entidade);
	public void excluir(RepresentacaoCargo entidade);

	public RepresentacaoCargo getByNomenclaturaSimbolo(String nomenclatura, String simbolo);

	public List<RepresentacaoCargo> findAll();

}
