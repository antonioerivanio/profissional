package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Publicacao;

public interface PublicacaoDAO {

	public int count(Long tipoDocumento);
	public List<Publicacao> search(Long tipoDocumento, int first, int rows);

	public Publicacao salvar(Publicacao entidade);
	public void excluir(Publicacao entidade);

	public Publicacao getByEmenta(String ementa);

}
