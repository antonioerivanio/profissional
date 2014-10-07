package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.domain.LicencaEspecial;
import br.gov.ce.tce.srh.domain.TipoLicenca;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface LicencaService {

	public int count(Long idPessoa);
	public int count(Long idPessoa, Long tipoLicenca);
	public int count(String nome, TipoLicenca tipoLicenca);

	public List<Licenca> search(Long idPessoa, int first, int rows);
	public List<Licenca> search(Long idPessoa, Long tipoLicenca, int first, int rows);
	public List<Licenca> search(String nome, int first, int rows);
	public List<Licenca> search(String nome, TipoLicenca tipoLicenca, int first, int rows);

	public void salvar(Licenca entidade) throws SRHRuntimeException;
	public void excluir(Licenca entidade) throws SRHRuntimeException;
	public void finalizar(Licenca entidade) throws SRHRuntimeException;
	
	public Licenca getById(Long id);

	public List<Licenca> findByPessoa(Long idPessoa);
	public List<Licenca> findByPessoaLicencaEspecial(Long idPessoa);
	List<Licenca> findByPessoaLicencasExcluirTempoServico(Long idPessoa);

}
