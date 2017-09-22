package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AreaAcademicaDAO;
import br.gov.ce.tce.srh.domain.AreaAcademica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("areaFormacaoService")
public class AreaAcademicaServiceImpl implements AreaAcademicaService {

	@Autowired
	private AreaAcademicaDAO dao;


	@Override
	@Transactional
	public void salvar(AreaAcademica entidade) throws SRHRuntimeException {

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
	public void excluir(AreaAcademica entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}

	@Override
	public List<AreaAcademica> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}


	@Override
	public List<AreaAcademica> findAll() {
		return dao.findAll();
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe area academica cadastrada com a mesma DESCRICAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(AreaAcademica entidade) throws SRHRuntimeException {

		AreaAcademica entidadeJaExiste = dao.getByDescricao(entidade.getDescricao());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Área de Formação Acadêmica já cadastrada. Operação cancelada.");

	}


	public void setDAO(AreaAcademicaDAO areaFormacaoDAO) {this.dao = areaFormacaoDAO;}

}
