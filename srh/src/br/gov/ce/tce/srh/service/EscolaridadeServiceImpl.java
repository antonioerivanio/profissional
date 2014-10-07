package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.EscolaridadeDAO;
import br.gov.ce.tce.srh.domain.Escolaridade;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("escolaridadeService")
public class EscolaridadeServiceImpl implements EscolaridadeService {

	@Autowired
	private EscolaridadeDAO dao;


	@Override
	@Transactional
	public void salvar(Escolaridade entidade) throws SRHRuntimeException {

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
	public void excluir(Escolaridade entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}


	@Override
	public List<Escolaridade> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}


	@Override
	public List<Escolaridade> findAll() {
		return dao.findAll();
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe escolaridade cadastrada com a mesma DESCRICAO ou ORDEM.
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(Escolaridade entidade) throws SRHRuntimeException {

		// verificando descricao
		Escolaridade entidadeJaExiste = dao.getByDescricao(entidade.getDescricao());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Escolaridade já cadastrada. Operação cancelada.");

		// verificando ordem
		entidadeJaExiste = dao.getByOrdem(entidade.getOrdem());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Ordem já cadastrada. Operação cancelada.");
		
	}


	public void setDAO(EscolaridadeDAO escolaridadeDAO) {this.dao = escolaridadeDAO;}

}
