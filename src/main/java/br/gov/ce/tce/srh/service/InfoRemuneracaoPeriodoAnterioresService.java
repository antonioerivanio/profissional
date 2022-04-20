package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.InfoRemuneracaoPeriodoAnterioresDAO;
import br.gov.ce.tce.srh.domain.InfoRemuneracaoPeriodoAnteriores;

@Service("infoRemuneracaoPeriodoAnterioresService")
public class InfoRemuneracaoPeriodoAnterioresService{

	@Autowired
	private InfoRemuneracaoPeriodoAnterioresDAO dao;
		

	@Transactional
	public InfoRemuneracaoPeriodoAnteriores salvar(InfoRemuneracaoPeriodoAnteriores entidade) {		
		entidade = dao.salvar(entidade);
		return entidade;
	}		

	@Transactional
	public void excluir(InfoRemuneracaoPeriodoAnteriores entidade) {
		dao.excluir(entidade);
	}	

	public InfoRemuneracaoPeriodoAnteriores getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<InfoRemuneracaoPeriodoAnteriores> findByIdfuncional(Long idFuncional) {
		List<InfoRemuneracaoPeriodoAnteriores> depentendes = dao.findByIdfuncional(idFuncional);
		for (InfoRemuneracaoPeriodoAnteriores InfoRemuneracaoPeriodoAnteriores : depentendes) {
			InfoRemuneracaoPeriodoAnteriores.setId(null);
		}
		return depentendes;
	}

	@Transactional
	public void salvar(List<InfoRemuneracaoPeriodoAnteriores> dependentesList) {
		for (InfoRemuneracaoPeriodoAnteriores InfoRemuneracaoPeriodoAnteriores : dependentesList) {
			salvar(InfoRemuneracaoPeriodoAnteriores);
		}
		
	}

	public List<InfoRemuneracaoPeriodoAnteriores> findInfoRemuneracaoPeriodoAnterioresByIdfuncional(Long idFuncional) {
		return dao.findInfoRemuneracaoPeriodoAnterioresByIdfuncional(idFuncional);
	}

	

}
