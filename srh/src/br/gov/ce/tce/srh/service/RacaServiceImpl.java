package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RacaDAO;
import br.gov.ce.tce.srh.domain.Raca;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("racaService")
public class RacaServiceImpl implements RacaService {

	@Autowired
	private RacaDAO dao;


	@Override
	@Transactional
	public void salvar(Raca entidade) throws SRHRuntimeException {

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
	public void excluir(Raca entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}


	@Override
	public List<Raca> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}


	@Override
	public List<Raca> findAll() {
		return dao.findAll();
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe tipo de biotipo cadastrado com a mesma DESCRICAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(Raca entidade) throws SRHRuntimeException {

		Raca entidadeJaExiste = dao.getByDescricao(entidade.getDescricao());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Tipo de Biotipo Humano já cadastrado. Operação cancelada.");
		
	}


	public void setDAO(RacaDAO racaDAO) { this.dao = racaDAO; }

}
