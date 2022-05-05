package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.DemonstrativosDeValoresDAO;
import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;

@Service("demonstrativosDeValoresService")
public class DemonstrativosDeValoresService{

	@Autowired
	private DemonstrativosDeValoresDAO dao;		

	@Transactional
	public DemonstrativosDeValores salvar(DemonstrativosDeValores entidade) {		
		entidade = dao.salvar(entidade);
		return entidade;
	}		

	@Transactional
	public void excluir(DemonstrativosDeValores entidade) {
		dao.excluir(entidade);
	}	

	public DemonstrativosDeValores getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<DemonstrativosDeValores> findByIdfuncional(Long idFuncional) {
		List<DemonstrativosDeValores> demonstrativosDeValoresList = dao.findByIdfuncional(idFuncional);
		for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {
			demonstrativosDeValores.setId(null);
		}
		return demonstrativosDeValoresList;
	}

	@Transactional
	public void salvar(List<DemonstrativosDeValores> dependentesList) {
		for (DemonstrativosDeValores demonstrativosDeValores : dependentesList) {
			salvar(demonstrativosDeValores);
		}		
	}

	public List<DemonstrativosDeValores> findDemonstrativosDeValoresByIdfuncional(Long idFuncional) {
		return dao.findDemonstrativosDeValoresByIdfuncional(idFuncional);
	}

	

}
