package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.LicencaEspecial;

public interface LicencaEspecialDAO {

	public int count(Long pessoal);
	public List<LicencaEspecial> search(Long pessoal, int first, int rows);

	public LicencaEspecial salvar(LicencaEspecial entidade);
	public void ajustarSaldo(Long idLicencaEspecial, Long saldodias);
	public void excluir(LicencaEspecial entidade);

	public LicencaEspecial getById(Long id);

	public List<LicencaEspecial> findByPessoal(Long pessoal);
	public List<LicencaEspecial> findByPessoalComSaldo(Long pessoal);	
	public List<LicencaEspecial> findByPessoalContaEmDobro(Long pessoal);

}
