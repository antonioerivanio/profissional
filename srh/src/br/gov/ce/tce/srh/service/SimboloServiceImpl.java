package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.SimboloDAO;
import br.gov.ce.tce.srh.domain.Simbolo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("simboloService")
public class SimboloServiceImpl implements SimboloService {

	@Autowired
	private SimboloDAO dao;


	@Override
	@Transactional
	public void salvar(Simbolo entidade) throws SRHRuntimeException {

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
	public void excluir(Simbolo entidade) {
		dao.excluir(entidade);
	}


	@Override
	public void excluirAll(Long ocupacao) {
		dao.excluirAll(ocupacao);
	}


	@Override
	public List<Simbolo> findByOcupacao(Long ocupacao) {
		return dao.findByOcupacao(ocupacao);
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe simbolo cadastrada com o mesmo SIMBOLO e OCUPACAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(Simbolo entidade) throws SRHRuntimeException {

		Simbolo entidadeJaExiste = dao.getByOcupacaoSimbolo( entidade.getOcupacao().getId(), entidade.getSimbolo() );
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Simbolo já cadastrado. Operação cancelada.");

	}


	public void setDAO(SimboloDAO dao) {this.dao = dao;}

}
