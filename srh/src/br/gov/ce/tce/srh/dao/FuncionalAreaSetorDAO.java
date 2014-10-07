package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.FuncionalAreaSetor;

public interface FuncionalAreaSetorDAO {

	public int count(Long funcional);
	public List<FuncionalAreaSetor> search(Long funcional, int first, int rows);

	public FuncionalAreaSetor salvar(FuncionalAreaSetor entidade);
	public void excluir(FuncionalAreaSetor entidade);

	public List<FuncionalAreaSetor> findByFuncional(Long funcional);
	public List<FuncionalAreaSetor> findByFuncionalComSetorAtual(Long funcional);
	public List<FuncionalAreaSetor> findyByAreaSetor(Long areaSetor);

}
