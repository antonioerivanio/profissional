package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RubricaDAO;
import br.gov.ce.tce.srh.domain.Rubrica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("rubricaService")
public class RubricaService {

	@Autowired
	private RubricaDAO dao;

	@Transactional
	public void salvar(Rubrica entidade) throws SRHRuntimeException {

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


	@Transactional
	public void excluir(Rubrica entidade) {
		dao.excluir(entidade);
	}


	public int count(String descricao) {
		return dao.count(descricao);
	}


	public List<Rubrica> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}
	
	public List<Rubrica> findAll() {
		return dao.findAll();
	}


	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe rubrica cadastrada com o mesmo CODIGO ou ORDEM ou DESCRICAO.
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(Rubrica entidade) throws SRHRuntimeException {

		// verificando codigo
		Rubrica entidadeJaExiste = dao.getByCodigo(entidade.getCodigo());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Código já cadastrado. Operação cancelada.");

		// verificando descricao
		entidadeJaExiste = dao.getByDescricao(entidade.getDescricao());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Rubrica já cadastrada. Operação cancelada.");
		
	}


	public void setDAO(RubricaDAO rubricaDAO) {this.dao = rubricaDAO;}

}
