package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.CursoProfissional;

public interface CursoProfissionalDAO {

	public int count(String descricao);
	public int count(Long area, String descricao);

	public List<CursoProfissional> search(String descricao, int first, int rows);
	public List<CursoProfissional> search(Long area, String descricao, int first, int rows);

	public CursoProfissional salvar(CursoProfissional entidade);
	public void excluir(CursoProfissional entidade);

	public CursoProfissional getById(Long id);
	public CursoProfissional getByCursoAreaInstituicao(String curso, Long area, Long instituicao);

	public List<CursoProfissional> findByArea(Long area);
	public List<CursoProfissional> findAll();

}
