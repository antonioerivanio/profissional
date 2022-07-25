package br.gov.ce.tce.srh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.ItensRemuneracaoTrabalhadorDAO;
import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
import br.gov.ce.tce.srh.domain.ItensRemuneracaoTrabalhador;

@Service("itensRemuneracaoTrabalhadorService")
public class ItensRemuneracaoTrabalhadorService{

	@Autowired
	private ItensRemuneracaoTrabalhadorDAO dao;
		

	@Transactional
	public ItensRemuneracaoTrabalhador salvar(ItensRemuneracaoTrabalhador entidade) {		
		entidade = dao.salvar(entidade);
		return entidade;
	}		

	@Transactional
	public void excluir(ItensRemuneracaoTrabalhador entidade) {
		dao.excluir(entidade);
	}	

	public ItensRemuneracaoTrabalhador getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<ItensRemuneracaoTrabalhador> findByDemonstrativosDeValores(List<DemonstrativosDeValores> demonstrativosDeValoresList) {
		List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorReturnList = new  ArrayList<ItensRemuneracaoTrabalhador>();
		String matricula = "";
		if(demonstrativosDeValoresList != null && !demonstrativosDeValoresList.isEmpty()) {
			for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {
				if(demonstrativosDeValores.getFlInfoRemunPerAnteriores().equals(1)) {
					matricula = demonstrativosDeValores.getInfoRemuneracaoPeriodoAnteriores().getMatricula().substring(1);
				}
				else {
					matricula = demonstrativosDeValores.getInfoRemuneracaoPeriodoApuracao().getMatricula().substring(1);
				}
				
				List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList = dao.findByDemonstrativosDeValores(demonstrativosDeValores, matricula);
				if(itensRemuneracaoTrabalhadorList != null && !itensRemuneracaoTrabalhadorList.isEmpty()) {			
					itensRemuneracaoTrabalhadorReturnList.addAll(itensRemuneracaoTrabalhadorList);	
					demonstrativosDeValores.setItensRemuneracaoTrabalhadorList(itensRemuneracaoTrabalhadorList);
				}
				
			}
			for (ItensRemuneracaoTrabalhador itensRemuneracaoTrabalhador : itensRemuneracaoTrabalhadorReturnList) {
				itensRemuneracaoTrabalhador.setId(null);
			}
		}
		return itensRemuneracaoTrabalhadorReturnList;
	}

	@Transactional
	public void salvar(List<ItensRemuneracaoTrabalhador> dependentesList) {
		for (ItensRemuneracaoTrabalhador ItensRemuneracaoTrabalhador : dependentesList) {
			salvar(ItensRemuneracaoTrabalhador);
		}
		
	}

	public List<ItensRemuneracaoTrabalhador> findItensRemuneracaoTrabalhadorByIdfuncional(Long idFuncional) {
		return dao.findItensRemuneracaoTrabalhadorByIdfuncional(idFuncional);
	}

	

}
