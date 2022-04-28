package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.domain.TipoLicenca;

public interface LicencaDAO {

	public int count(Long idPessoa);
	public int count(Long idPessoa, Long tipoLicenca);

	public List<Licenca> search(Long idPessoa, int first, int rows);
	public List<Licenca> search(Long idPessoa, Long tipoLicenca, int first, int rows);
	public List<Licenca> search(String nome, int first, int rows);
	public List<Licenca> search(String nome, TipoLicenca tipoLicenca, int first, int rows);
	public List<Licenca> search(Funcional funcional, List<Integer> listaCodigo);
	
	public Licenca salvar(Licenca entidade);
	public void excluir(Licenca entidade);

	public Licenca getById(Long id);

	public List<Licenca> findByPessoa(Long idPessoa);
	public List<Licenca> findByPessoaLicencaEspecial(Long idPessoa);
	List<Licenca> findByPessoaLicencaExcluidaTempoServico(Long idPessoa);
}
