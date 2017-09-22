package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.EstadoCivilDAO;
import br.gov.ce.tce.srh.domain.EstadoCivil;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("estadoCivilService")
public class EstadoCivilServiceImpl implements EstadoCivilService {

	@Autowired
	private EstadoCivilDAO dao;


	@Override
	@Transactional
	public void salvar(EstadoCivil entidade) throws SRHRuntimeException {

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
	public void excluir(EstadoCivil entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}


	@Override
	public List<EstadoCivil> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}


	@Override
	public List<EstadoCivil> findAll() {
		return dao.findAll();
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe estado civil cadastrada com a mesma DESCRICAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(EstadoCivil entidade) throws SRHRuntimeException {

		EstadoCivil entidadeJaExiste = dao.getByDescricao(entidade.getDescricao());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Estado Civil já cadastrado. Operação cancelada.");
		
	}


	public void setDAO(EstadoCivilDAO estadoCivilDAO) { this.dao = estadoCivilDAO; }

}
