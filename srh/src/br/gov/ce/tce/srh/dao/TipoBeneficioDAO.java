package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.TipoBeneficio;

public interface TipoBeneficioDAO {

	public int count(String descricao);
	public List<TipoBeneficio> search(String descricao, int first, int rows);

	public TipoBeneficio salvar(TipoBeneficio entidade);
	public void excluir(TipoBeneficio entidade);

	public TipoBeneficio getByDescricao(String descricao);

}
