package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.MotivoDependenciaDAO;
import br.gov.ce.tce.srh.domain.MotivoDependencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("motivoDependenciaService")
public class MotivoDependenciaServiceImpl implements MotivoDependenciaService {

	@Autowired
	private MotivoDependenciaDAO dao;


	@Override
	@Transactional
	public void salvar(MotivoDependencia entidade) throws SRHRuntimeException {

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
	public void excluir(MotivoDependencia entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}


	@Override
	public List<MotivoDependencia> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}


	@Override
	public List<MotivoDependencia> findByTipo(Long tipo) {
		return dao.findByTipo(tipo);
	}
	

	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe motivo dependencia cadastrada com a mesma DESCRICAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(MotivoDependencia entidade) throws SRHRuntimeException {

		MotivoDependencia entidadeJaExiste = dao.getByDescricao( entidade.getDescricao() );
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Motivo Dependência já cadastrado. Operação cancelada.");

	}


	public void setDAO(MotivoDependenciaDAO motivoDependenciaDAO) {this.dao = motivoDependenciaDAO;}
	

}
