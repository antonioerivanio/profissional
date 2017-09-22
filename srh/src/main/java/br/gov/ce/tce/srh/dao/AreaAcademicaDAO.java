package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.AreaAcademica;

public interface AreaAcademicaDAO {

	public int count(String descricao);
	public List<AreaAcademica> search(String descricao, int first, int rows);

	public AreaAcademica salvar(AreaAcademica entidade);
	public void excluir(AreaAcademica entidade);

	public AreaAcademica getByDescricao(String descricao);

	public List<AreaAcademica> findAll();

}
