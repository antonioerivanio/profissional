package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CursoAcademicaDAO;
import br.gov.ce.tce.srh.domain.CursoAcademica;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("cursoAcademicaService")
public class CursoAcademicaServiceImpl implements CursoAcademicaService {

	@Autowired
	private CursoAcademicaDAO dao;


	@Override
	@Transactional
	public void salvar(CursoAcademica entidade) throws SRHRuntimeException {

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
	public void excluir(CursoAcademica entidade) {
		dao.excluir(entidade);
	}


	@Override
	public int count(String orderBy) {
		return dao.count(orderBy);
	}


	@Override
	public int count(Long area, String descricao) {
		return dao.count(area, descricao);
	}


	@Override
	public List<CursoAcademica> search(String orderBy, int first, int rows) {
		return dao.search(orderBy, first, rows);
	}


	@Override
	public List<CursoAcademica> search(Long area, String descricao, int first, int rows) {
		return dao.search(area, descricao, first, rows);
	}


	@Override
	public List<CursoAcademica> findAll() {
		return dao.findAll();
	}


	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe curso academico cadastrado com a mesma DESCRICAO e AREA
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(CursoAcademica entidade) throws SRHRuntimeException {

		CursoAcademica entidadeJaExiste = dao.getByAreaDescricao(entidade.getArea().getId(), entidade.getDescricao());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Curso de Formação Acadêmica já cadastrado. Operação cancelada.");
		
	}


	public void setDAO(CursoAcademicaDAO cursoAcademicaDAO) {this.dao = cursoAcademicaDAO;}

}
