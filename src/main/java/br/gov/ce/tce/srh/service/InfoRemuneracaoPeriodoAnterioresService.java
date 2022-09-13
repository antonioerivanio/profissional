package br.gov.ce.tce.srh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.InfoRemuneracaoPeriodoAnterioresDAO;
import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
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
	
	public List<InfoRemuneracaoPeriodoAnteriores> findInfoRemuneracaoPeriodoAnteriores(String mesReferencia, String anoReferencia, List<DemonstrativosDeValores> demonstrativosDeValoresList, Long idFuncional, boolean isEstagiario) {
		List<InfoRemuneracaoPeriodoAnteriores> infoRemuneracaoPeriodoAnterioresListReturn = new ArrayList<InfoRemuneracaoPeriodoAnteriores>();
		
		if(demonstrativosDeValoresList != null && !demonstrativosDeValoresList.isEmpty()) {
			for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {			
				if(demonstrativosDeValores.getFlInfoRemunPerAnteriores().equals(1)) {
					List<InfoRemuneracaoPeriodoAnteriores> infoRemuneracaoPeriodoAnterioresList = dao.findInfoRemuneracaoPeriodoAnteriores(mesReferencia, anoReferencia, demonstrativosDeValores, idFuncional, isEstagiario);
					demonstrativosDeValores.setInfoPerAnt(infoRemuneracaoPeriodoAnterioresList.get(0));
					for (InfoRemuneracaoPeriodoAnteriores infoRemuneracaoPeriodoAnteriores : infoRemuneracaoPeriodoAnterioresList) {
						if(infoRemuneracaoPeriodoAnteriores.getId() < 0) {
							infoRemuneracaoPeriodoAnteriores.setId(null);
							infoRemuneracaoPeriodoAnteriores.setDemonstrativosDeValores(demonstrativosDeValores);							
						}
					}
					infoRemuneracaoPeriodoAnterioresListReturn.addAll(infoRemuneracaoPeriodoAnterioresList);
					if(infoRemuneracaoPeriodoAnterioresList != null && !infoRemuneracaoPeriodoAnterioresList.isEmpty()) {
						demonstrativosDeValores.setInfoRemuneracaoPeriodoAnteriores(infoRemuneracaoPeriodoAnterioresList.get(0));
						
					}
				}
				
			}
			
		}
		
		return infoRemuneracaoPeriodoAnterioresListReturn;
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
