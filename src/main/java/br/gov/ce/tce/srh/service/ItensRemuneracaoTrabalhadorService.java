package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.ItensRemuneracaoTrabalhadorDAO;
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
	
	public List<ItensRemuneracaoTrabalhador> findByIdfuncional(Long idFuncional) {
		List<ItensRemuneracaoTrabalhador> depentendes = dao.findByIdfuncional(idFuncional);
		for (ItensRemuneracaoTrabalhador ItensRemuneracaoTrabalhador : depentendes) {
			ItensRemuneracaoTrabalhador.setId(null);
		}
		return depentendes;
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