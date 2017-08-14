package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CursoProfissionalDAO;
import br.gov.ce.tce.srh.domain.CursoProfissional;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("cursoProfissionalService")
public class CursoProfissionalServiceImpl implements CursoProfissionalService {

	@Autowired
	private CursoProfissionalDAO dao;


	@Override
	@Transactional
	public void salvar(CursoProfissional entidade) throws SRHRuntimeException {

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
	public void excluir(CursoProfissional entidade) {
		dao.excluir(entidade);
	}

	@Override
	public int count(String descricao, Long idArea, Date inicio, Date fim) {
		return dao.count(descricao, idArea, inicio, fim);
	}


	@Override
	public List<CursoProfissional> search(String descricao, Long idArea, Date inicio, Date fim, int first, int rows) {
		return dao.search(descricao, idArea, inicio, fim, first, rows);
	}


	@Override
	public CursoProfissional getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public List<CursoProfissional> findByArea(Long area) {
		return dao.findByArea(area);
	}


	@Override
	public List<CursoProfissional> findAll() {
		return dao.findAll();
	}



	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe curso profissional cadastrada com a mesma DESCRICAO e AREA e INSTITUICAO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	private void verificandoSeEntidadeExiste(CursoProfissional entidade) throws SRHRuntimeException {

		CursoProfissional entidadeJaExiste = dao.getByCursoAreaInstituicao( entidade.getDescricao(), entidade.getArea().getId(), entidade.getInstituicao().getId() );
		if (entidadeJaExiste != null && !entidadeJaExiste.getId().equals(entidade.getId()))
			throw new SRHRuntimeException("Curso de Formação Profissional já cadastrado. Operação cancelada.");
		
	}


	public void setDAO(CursoProfissionalDAO cursoProfissionalDAO) { this.dao = cursoProfissionalDAO; }

}
