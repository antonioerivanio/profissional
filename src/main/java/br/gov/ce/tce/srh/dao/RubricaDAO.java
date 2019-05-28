package br.gov.ce.tce.srh.dao;

import java.util.List;
import br.gov.ce.tce.srh.domain.Rubrica;

public interface RubricaDAO {

	public int count(String descricao);
	public List<Rubrica> search(String descricao, int first, int rows);

	public Rubrica salvar(Rubrica entidade);
	public void excluir(Rubrica entidade);

	public Rubrica getByCodigo(String codigo);
	public Rubrica getByDescricao(String descricao);

}
