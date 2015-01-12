package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RubricaDAO;
import br.gov.ce.tce.srh.domain.Rubrica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("rubricaService")
public class RubricaServiceImpl implements RubricaService {

	@Autowired
	private RubricaDAO dao;


	@Override
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


	@Override
	@Transactional
	public void excluir(Rubrica entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}


	@Override
	public List<Rubrica> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
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
