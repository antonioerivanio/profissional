package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.FolhaDAO;
import br.gov.ce.tce.srh.domain.Folha;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("folhaService")
public class FolhaServiceImpl implements FolhaService {

	@Autowired
	private FolhaDAO dao;


	@Override
	@Transactional
	public void salvar(Folha entidade) throws SRHRuntimeException {

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
	public void excluir(Folha entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}


	@Override
	public List<Folha> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}


	@Override
	public List<Folha> findByAtivo(Boolean ativo) {
		return dao.findByAtivo(ativo);
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe folha cadastrada com a mesma CODIGO ou DESCRICAO.
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(Folha entidade) throws SRHRuntimeException {

		// verifciando codigo
		Folha entidadeJaExiste = dao.getByCodigo(entidade.getCodigo());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Código já cadastrado. Operação cancelada.");

		// verificando descricao
		entidadeJaExiste = dao.getByDescricao(entidade.getDescricao());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Tipo de Folha já cadastrado. Operação cancelada.");
		
	}


	public void setDAO(FolhaDAO folhaDAO) {this.dao = folhaDAO;}

}
