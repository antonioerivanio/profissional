package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.PessoaJuridicaDAO;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.SRHUtils;

/**
 * Referente a tabela: TB_PESSOAJURIDICA
 * 
 * @since : Dez 08, 2021, 09:59:00 AM
 * @author : marcelo.viana@tce.ce.gov.br
 *
 */

@Service("pessoaJuridicaService")
public class PessoaJuridicaServiceImpl implements PessoaJuridicaService {

	@Autowired
	private PessoaJuridicaDAO dao;
	
	

	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica se existe na base o CNPJ, razão social ou o nome de fantasia.
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(PessoaJuridica entidade) throws SRHRuntimeException {
		
		// verificando cnpj
		PessoaJuridica entidadeJaExiste = dao.getBycnpj(entidade.getCnpj());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("CNPJ já cadastrado. Operação cancelada.");
		
		// verificando razão social
	/*	entidadeJaExiste = dao.getByrazaoSocial(entidade.getRazaoSocial());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Razão Social já cadastrada. Operação cancelada.");*/
		
		// verificando nome de fantasia
		entidadeJaExiste = dao.getBynomeFantasia(entidade.getNomeFantasia());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Nome Fantasia já cadastrada. Operação cancelada.");
	}
	

	
	@Override
	@Transactional
	public void salvar(PessoaJuridica entidade) throws SRHRuntimeException {
		/*
		 * Regra: 
		 *
		 * Nao deixar cadastrar entidade ja existente.
		 * 
		 */
		verificandoSeEntidadeExiste(entidade);
		
		
		// persistindo
		dao.salvar(entidade);
		
	}

	
	@Override
	@Transactional
	public void excluir(PessoaJuridica entidade) {
		dao.excluir(entidade);
	}
	
	
	@Override
	public int count(String cnpj, String razaoSocial, String nomeFantasia) {
		return dao.count(SRHUtils.removerMascara(cnpj), razaoSocial, nomeFantasia);
	}
	
	
	@Override
	public List<PessoaJuridica> search(String cnpj, String razaoSocial, String nomeFantasia, int first, int rows) {
		return dao.search(SRHUtils.removerMascara(cnpj), razaoSocial, nomeFantasia, first, rows);
	}

	@Override
	public List<PessoaJuridica> findAll() {
		return dao.findAll();
	}
	
		
	
	public void setDao(PessoaJuridicaDAO pessoaJuridicaDao) {this.dao = pessoaJuridicaDao;}



	@Override
	public PessoaJuridica findById(Long id) {
		return dao.findById(id);
	}	
}
