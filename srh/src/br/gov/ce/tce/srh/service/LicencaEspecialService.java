package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.LicencaEspecial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

public interface LicencaEspecialService {

	public int count(Long pessoal);
	public List<LicencaEspecial> search(Long pessoal, int first, int rows);

	public LicencaEspecial salvar(LicencaEspecial entidade) throws SRHRuntimeException;
	public void excluir(LicencaEspecial entidade) throws SRHRuntimeException;

	public LicencaEspecial getById(Long id);

	public List<LicencaEspecial> findByPessoal(Long Pessoal);
	public List<LicencaEspecial> findByPessoalComSaldo(Long pessoal);
	public List<LicencaEspecial> findByPessoalContaEmDobro(Long pessoal);	

	public Long calcularQtdeDias(Long anoInicial, Long anoFinal) throws SRHRuntimeException;
	public void ajustarSaldo(Long idLicencaEspecial, String acao, int qtdDias)	throws SRHRuntimeException;	

}
