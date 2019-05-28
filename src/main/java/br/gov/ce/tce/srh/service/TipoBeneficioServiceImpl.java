package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.TipoBeneficioDAO;
import br.gov.ce.tce.srh.domain.TipoBeneficio;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("tipoBeneficioService")
public class TipoBeneficioServiceImpl implements TipoBeneficioService {

	@Autowired
	private TipoBeneficioDAO dao;
	
	@Override
	@Transactional
	public void salvar(TipoBeneficio entidade) throws SRHRuntimeException {

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
	public void excluir(TipoBeneficio entidade) {
		dao.excluir(entidade);
	}	
	
	@Override
	public List<TipoBeneficio> findAll() {
		return dao.findAll();
	}
	
	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}

	@Override
	public List<TipoBeneficio> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}


	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe tipo de beneficio cadastrada com a mesma DESCRICAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(TipoBeneficio entidade) throws SRHRuntimeException {

		TipoBeneficio entidadeJaExiste = dao.getByDescricao(entidade.getDescricao());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Tipo de Benefício já cadastrado. Operação cancelada.");
		
	}

	public void setDAO(TipoBeneficioDAO tipoBeneficioDAO) {this.dao = tipoBeneficioDAO;}	

}
