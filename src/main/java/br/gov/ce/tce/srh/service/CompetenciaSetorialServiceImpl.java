package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CompetenciaSetorialDAO;
import br.gov.ce.tce.srh.domain.CompetenciaSetorial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

@Service("competenciaSetorialServiceImpl")
public class CompetenciaSetorialServiceImpl implements CompetenciaSetorialService {

	@Autowired
	private CompetenciaSetorialDAO dao;
	
	public void setDAO(CompetenciaSetorialDAO dao) {this.dao = dao;}
	
	@Override
	public int count(Setor setor, Long tipo) {
		return dao.count(setor, tipo);
	}
	
	@Override
	public int count(Setor setor) {
		return dao.count(setor);
	}

	@Override
	public List<CompetenciaSetorial> search(Setor setor, Long tipo, int first,
			int rows) {
		return dao.search(setor, tipo, first, rows);
	}
	
	@Override
	public List<CompetenciaSetorial> search(Setor setor, int first,
			int rows) {
		return dao.search(setor, first, rows);
	}

	@Override
	@Transactional
	public CompetenciaSetorial salvar(CompetenciaSetorial entidade) throws SRHRuntimeException {
		validaCampos(entidade);
		return dao.salvar(entidade);
	}

	@Override
	@Transactional
	public void excluir(CompetenciaSetorial entidade) {
		dao.excluir(entidade);
	}

	@Override
	public CompetenciaSetorial getById(Long id) {
		return dao.getById(id);
	}

	@Override
	public List<CompetenciaSetorial> findAll() {
		return dao.findAll();
	}
	
	private void validaCampos(CompetenciaSetorial entidade) throws SRHRuntimeException{

		if(entidade.getInicio() == null){
			throw new SRHRuntimeException("A Data Inicial é obrigatória.");
		}

		if(entidade.getInicio() != null && entidade.getFim() != null){
			if(entidade.getInicio().after(entidade.getFim())){
				throw new SRHRuntimeException("A Data Fim deve ser maior que a Data Inicio.");
			}
		}
		
		if(entidade.getSetor()==null||entidade.getSetor().getId()==null){
			throw new SRHRuntimeException("O setor é obrigatório.");
		}
		
		if(entidade.getCompetencia()==null||entidade.getCompetencia().getId()==null){
			throw new SRHRuntimeException("A competência é obrigatória.");
		}		
		
	}

}
