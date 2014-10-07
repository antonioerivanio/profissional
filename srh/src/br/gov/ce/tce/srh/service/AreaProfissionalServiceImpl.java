package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AreaProfissionalDAO;
import br.gov.ce.tce.srh.domain.AreaProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("areaProfissionalService")
public class AreaProfissionalServiceImpl implements AreaProfissionalService {

	@Autowired
	private AreaProfissionalDAO dao;


	@Override
	@Transactional
	public void salvar(AreaProfissional entidade) throws SRHRuntimeException {

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
	public void excluir(AreaProfissional entidade) {
		dao.excluir(entidade);
	}

	
	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}


	@Override
	public List<AreaProfissional> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}


	@Override
	public List<AreaProfissional> findAll() {
		return dao.findAll();
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe area profissional cadastrada com a mesma DESCRICAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(AreaProfissional entidade) throws SRHRuntimeException {

		AreaProfissional entidadeJaExiste = dao.getByDescricao(entidade.getDescricao());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Área de Formação Profissional já cadastrado. Operação cancelada.");
		
	}


	public void setDAO(AreaProfissionalDAO areaProfissionalDAO) {this.dao = areaProfissionalDAO;}

}
