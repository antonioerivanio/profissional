package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.TipoLicencaDAO;
import br.gov.ce.tce.srh.domain.TipoLicenca;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("tipoLicencaService")
public class TipoLicencaServiceImpl implements TipoLicencaService {

	@Autowired
	private TipoLicencaDAO dao;


	@Override
	@Transactional
	public void salvar(TipoLicenca entidade) throws SRHRuntimeException {

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
	public void excluir(TipoLicenca entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}


	@Override
	public List<TipoLicenca> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}


	@Override
	public TipoLicenca getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public List<TipoLicenca> findAll() {
		return dao.findAll();
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe tipo de licenca cadastrada com a mesma DESCRICAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(TipoLicenca entidade) throws SRHRuntimeException {

		TipoLicenca entidadeJaExiste = dao.getByDescricao(entidade.getDescricao());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Tipo de Licença já cadastrado. Operação cancelada.");
		
	}


	public void setDAO(TipoLicencaDAO tipoLicencaDAO) {this.dao = tipoLicencaDAO;}

}
