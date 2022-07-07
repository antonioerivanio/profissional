package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.DependenteEsocialDAO;
import br.gov.ce.tce.srh.domain.DependenteEsocial;

@Service("dependenteEsocialTCEService")
public class DependenteEsocialTCEService{

	@Autowired
	private DependenteEsocialDAO dao;
		

	@Transactional
	public DependenteEsocial salvar(DependenteEsocial entidade) {		
		entidade = dao.salvar(entidade);
		return entidade;
	}		

	@Transactional
	public void excluir(DependenteEsocial entidade) {
		dao.excluir(entidade);
	}	

	public DependenteEsocial getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<DependenteEsocial> findByIdfuncional(Long idFuncional) {
		List<DependenteEsocial> depentendes = dao.findByIdfuncional(idFuncional);
		for (DependenteEsocial dependenteEsocial : depentendes) {
			dependenteEsocial.setId(null);
		}
		return depentendes;
	}

	@Transactional
	public void salvar(List<DependenteEsocial> dependentesList) {
		for (DependenteEsocial dependenteEsocial : dependentesList) {
			salvar(dependenteEsocial);
		}
		
	}

	public List<DependenteEsocial> findDependenteEsocialByIdfuncional(Long idFuncional) {
		return dao.findDependenteEsocialByIdfuncional(idFuncional);
	}

	public void excluirAll(List<DependenteEsocial> dependentesList) {
		for (DependenteEsocial dependenteEsocial : dependentesList) {			
			dao.excluir(dependenteEsocial);
		}
		
	}

	

}
