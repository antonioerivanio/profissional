package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.TipoDocumentoDAO;
import br.gov.ce.tce.srh.domain.TipoDocumento;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("tipoDocumentoService")
public class TipoDocumentoServiceImpl implements TipoDocumentoService {

	@Autowired
	private TipoDocumentoDAO dao;


	@Override
	@Transactional
	public void salvar(TipoDocumento entidade) throws SRHRuntimeException {

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
	public void excluir(TipoDocumento entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}


	@Override
	public List<TipoDocumento> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}


	@Override
	public List<TipoDocumento> findByEsfera(Long esfera) {
		return dao.findByEsfera(esfera);
	}

	
	@Override
	public List<TipoDocumento> findByDocfuncional(boolean docFuncional) {
		return dao.findByDocFuncional(docFuncional);
	}


	@Override
	public List<TipoDocumento> findAll() {
		return dao.findAll();
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe tipo de documento cadastrado com a mesma DESCRICAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(TipoDocumento entidade) throws SRHRuntimeException {

		TipoDocumento entidadeJaExiste = dao.getByDescricao( entidade.getDescricao() );
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Tipo de Documento já cadastrado. Operação cancelada.");
		
	}


	public void setDAO(TipoDocumentoDAO tipoDocumentoDAO) {this.dao = tipoDocumentoDAO;}

}
