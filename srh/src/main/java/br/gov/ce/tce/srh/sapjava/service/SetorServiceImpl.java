package br.gov.ce.tce.srh.sapjava.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.sapjava.dao.SetorDAO;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

@Service("setorService")
public class SetorServiceImpl implements SetorService {

	@Autowired
	private SetorDAO dao;


	@Override
	public List<Setor> findAll() {
		return dao.findAll();
	}


	public void setSetorDAO(SetorDAO setorDAO) {
		this.dao = setorDAO;
	}


	// TODO - remover
	@Override
	public Setor getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public List<Setor> findTodosAtivos() {
		return dao.findTodosAtivos();
	}

}
