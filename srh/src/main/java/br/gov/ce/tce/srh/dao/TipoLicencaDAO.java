package br.gov.ce.tce.srh.dao;

import java.util.List;
import br.gov.ce.tce.srh.domain.TipoLicenca;

public interface TipoLicencaDAO {

	public int count(String descricao);
	public List<TipoLicenca> search(String descricao, int first, int rows);

	public TipoLicenca salvar(TipoLicenca entidade);
	public void excluir(TipoLicenca entidade);

	public TipoLicenca getById(Long id);
	public TipoLicenca getByDescricao(String descricao);

	public List<TipoLicenca> findAll();

}
