package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.RepresentacaoSetor;

public interface RepresentacaoSetorDAO {

	public int count(Long cargo);
	public List<RepresentacaoSetor> search(Long cargo, int first, int rows);

	public RepresentacaoSetor salvar(RepresentacaoSetor entidade);
	public void excluir(RepresentacaoSetor entidade);

	public RepresentacaoSetor getByCargoSetor(Long cargo, Long setor);

	public List<RepresentacaoSetor> findBySetorAtivo(Long setor, boolean ativo);

}
