package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Ocupacao;

public interface OcupacaoDAO {

	public int count(String nomenclatura);
	public int count(String nomenclatura, Long situacao);
	public List<Ocupacao> search(String nomenclatura, int first, int rows);
	public List<Ocupacao> search(String nomenclatura, Long situacao, int first, int rows);

	public Ocupacao salvar(Ocupacao entidade);
	public void excluir(Ocupacao entidade);

	public Ocupacao getById(Long id);
	public Ocupacao getByNomenclatura(String nomenclatura);

	public List<Ocupacao> findByTipoOcupacao(Long tipoOcupacao);
	public List<Ocupacao> findAll();
	List<Ocupacao> findByPessoa(Long idPessoal);

}
