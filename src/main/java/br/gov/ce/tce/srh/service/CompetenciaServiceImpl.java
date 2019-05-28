package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CompetenciaDAO;
import br.gov.ce.tce.srh.domain.Competencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("competenciaService")
public class CompetenciaServiceImpl implements CompetenciaService {

	@Autowired
	private CompetenciaDAO dao;


	@Override
	@Transactional
	public void salvar(Competencia entidade) throws SRHRuntimeException {

		// validando dados obrigatorios
		if( entidade.getDescricao() == null || entidade.getDescricao().equals("") )
			throw new SRHRuntimeException("A descrição é obrigatória.");

		if( entidade.getTipo() == null || entidade.getTipo() == 0l )
			throw new SRHRuntimeException("O tipo é obrigatório.");

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
	public void excluir(Competencia entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}


	@Override
	public List<Competencia> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}


	@Override
	public List<Competencia> search(String tipo) {
		return dao.search(tipo);
	}

	@Override
	public Competencia getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public List<Competencia> findAll() {
		return dao.findAll();
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe competencia cadastrada com a mesma DESCRICAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(Competencia entidade) throws SRHRuntimeException {
		
		Competencia entidadeJaExiste = dao.getByDescricao( entidade.getDescricao() );
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Competência já cadastrada. Operação cancelada.");

	}


	public void setDAO(CompetenciaDAO competenciaDAO) {this.dao = competenciaDAO;}


	@Override
	public List<Competencia> findByTipo(Long tipo) {
		return dao.findByTipo(tipo);
	}

}
