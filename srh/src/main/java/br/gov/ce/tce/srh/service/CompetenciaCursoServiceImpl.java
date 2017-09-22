package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CompetenciaCursoDAO;
import br.gov.ce.tce.srh.domain.CompetenciaCurso;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("competenciaCursoService")
public class CompetenciaCursoServiceImpl implements CompetenciaCursoService {

	@Autowired
	private CompetenciaCursoDAO dao;


	@Override
	@Transactional
	public void salvar(CompetenciaCurso entidade) throws SRHRuntimeException {
		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(Long curso) {
		dao.excluir(curso);
	}


	@Override
	public CompetenciaCurso getByPessoalCompetencia(Long pessoal, Long competencia) {
		return dao.getByPessoalCompetencia(pessoal, competencia);
	}


	@Override
	public List<CompetenciaCurso> findByCurso(Long curso) {
		return dao.findByCurso(curso);
	}


	public void setDAO(CompetenciaCursoDAO dao) {this.dao = dao;}

}
