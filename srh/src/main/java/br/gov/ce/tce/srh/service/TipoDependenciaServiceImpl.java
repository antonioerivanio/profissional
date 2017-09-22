package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.TipoDependenciaDAO;
import br.gov.ce.tce.srh.domain.TipoDependencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("tipoDependenciaService")
public class TipoDependenciaServiceImpl implements TipoDependenciaService {

	@Autowired
	private TipoDependenciaDAO dao;


	@Override
	@Transactional
	public void salvar(TipoDependencia entidade) throws SRHRuntimeException {

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
	public void excluir(TipoDependencia entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}


	@Override
	public List<TipoDependencia> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}
	
	
	@Override
	public List<TipoDependencia> findAll() {
		return dao.findAll();
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe tipo dependente cadastrado com a mesma DESCRICAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(TipoDependencia entidade) throws SRHRuntimeException {

		TipoDependencia entidadeJaExiste = dao.getByDescricao( entidade.getDescricao() );
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Tipo de Dependência já cadastrado. Operação cancelada.");

	}


	public void setDAO(TipoDependenciaDAO tipoDependenciaDAO) {this.dao = tipoDependenciaDAO;}
	

}
