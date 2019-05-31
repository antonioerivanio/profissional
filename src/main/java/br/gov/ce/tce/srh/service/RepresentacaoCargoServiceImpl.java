package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RepresentacaoCargoDAO;
import br.gov.ce.tce.srh.domain.RepresentacaoCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("representacaoCargoService")
public class RepresentacaoCargoServiceImpl implements RepresentacaoCargoService {

	@Autowired
	private RepresentacaoCargoDAO dao;


	@Override
	@Transactional
	public void salvar(RepresentacaoCargo entidade) throws SRHRuntimeException {

		/*
		 * Regra: 
		 *
		 * Nao deixar cadastrar entidade ja existente.
		 * 
		 */
//		verificandoSeEntidadeExiste(entidade);

		// persistindo
		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(RepresentacaoCargo entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String nomenclatura) {
		return dao.count(nomenclatura);
	}
	
	@Override
	public List<RepresentacaoCargo> search(String nomenclatura, int first, int rows) {
		return dao.search(nomenclatura, first, rows);
	}


	@Override
	public List<RepresentacaoCargo> findAll() {
		return dao.findAll();
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe cargo comissionado cadastrado com a mesma NOMENCLATURA e SIMBOLO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(RepresentacaoCargo entidade) throws SRHRuntimeException {

		RepresentacaoCargo entidadeJaExiste = dao.getByNomenclaturaSimbolo( entidade.getNomenclatura(), entidade.getSimbolo() );
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Nomenclatura e Simbolo já cadastrada. Operação cancelada.");
		
	}


	public void setDAO(RepresentacaoCargoDAO dao) {this.dao = dao;}

}
