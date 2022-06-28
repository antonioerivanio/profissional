package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.enums.TipodeEmpresa;

/**
 * Referente a tabela: TB_PESSOAJURIDICA
 * 
 * @since  : Dez 08, 2021, 09:15:00 AM
 * @author : marcelo.viana@tce.ce.gov.br
 *
 */
public interface PessoaJuridicaDAO {
	
	public int count(String cnpj, String razaoSocial, String nomeFantasia);
	public List<PessoaJuridica> search(String cnpj, String razaoSocial, String nomeFantasia, int first, int rows);
			
	public PessoaJuridica salvar(PessoaJuridica entidade);
	public void excluir(PessoaJuridica entidade);

	public PessoaJuridica getBycnpj(String cnpj);
	public PessoaJuridica getByrazaoSocial(String razaoSocial);
	public PessoaJuridica getBynomeFantasia(String nomeFantasia);

	public List<PessoaJuridica> findAll();
	public List<PessoaJuridica> findAllByTipo(TipodeEmpresa tipodeEmpresa);
	public PessoaJuridica findById(Long id);
	

}

