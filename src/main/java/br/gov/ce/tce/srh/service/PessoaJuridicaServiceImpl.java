package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.PessoaJuridicaDAO;
import br.gov.ce.tce.srh.domain.PessoaJuridica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

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
	public int count(String cnpj) {
		return dao.count(cnpj);
	}
	
	
	@Override
	public List<PessoaJuridica> search(String cnpj, int first, int rows) {
		return dao.search(cnpj, first, rows);
	}


	@Override
	public List<PessoaJuridica> findAll() {
		return dao.findAll();
	}
	
		
	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica se existe na base o CNPJ ou a razão social.
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
		entidadeJaExiste = dao.getByrazaoSocial(entidade.getRazaoSocial());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Razão Social já cadastrada. Operação cancelada.");
	}
	
	
	public void setDao(PessoaJuridicaDAO pessoaJuridicaDao) {this.dao = pessoaJuridicaDao;}	
}
