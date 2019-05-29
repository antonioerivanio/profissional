package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CarreiraDAO;
import br.gov.ce.tce.srh.domain.Carreira;

@Service("carreiraService")
public class CarreiraService{

	@Autowired
	private CarreiraDAO dao;

	@Transactional
	public Carreira salvar(Carreira entidade) {
		// TODO Validações
		return dao.salvar(entidade);
	}

	@Transactional
	private void excluir(Carreira entidade) {
		dao.excluir(entidade);
	}
	
	@Transactional
	public void encerrarVigencia(Carreira entidade) {
		
		entidade.setInicioExclusao(entidade.getInicioValidade());
		entidade.setFimExclusao(entidade.getFimValidade());		
		
		dao.salvar(entidade);
	}

	public Carreira getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<Carreira> search(String descricao, Integer first, Integer rows) {
		return dao.search(descricao, first, rows);
	}

}
