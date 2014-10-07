package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CompetenciaGraduacaoDAO;
import br.gov.ce.tce.srh.domain.CompetenciaGraduacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("competenciaGraduacaoService")
public class CompetenciaGraduacaoServiceImpl implements CompetenciaGraduacaoService {

	@Autowired
	private CompetenciaGraduacaoDAO dao;


	@Override
	@Transactional
	public void salvar(CompetenciaGraduacao entidade) throws SRHRuntimeException {
		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(Long pessoa, Long curso) {
		dao.excluir(pessoa, curso);
	}
	
	@Override
	@Transactional
	public void excluir(Long pessoa, Long curso, Long competencia) {
		dao.excluir(pessoa, curso, competencia);
	}


	@Override
	public CompetenciaGraduacao getByPessoalCompetencia(Long pessoal, Long competencia) {
		return dao.getByPessoalCompetencia(pessoal, competencia);
	}


	@Override
	public List<CompetenciaGraduacao> findByPessoaCurso(Long pessoa, Long curso) {
		return dao.findByPessoaCurso(pessoa, curso);
	}


	public void setDAO(CompetenciaGraduacaoDAO dao) {this.dao = dao;}

}
