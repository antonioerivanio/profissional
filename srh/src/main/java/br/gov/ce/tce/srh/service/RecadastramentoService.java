package br.gov.ce.tce.srh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.RecadastramentoDAO;
import br.gov.ce.tce.srh.domain.Recadastramento;

@Service
public class RecadastramentoService {

	@Autowired
	private RecadastramentoDAO dao;

	
	public Recadastramento findById(Long id) {
		return dao.findById(id);
	}
	
	public Recadastramento findMaisRecente() {
		return dao.findMaisRecente();
	}

	public void setDAO(RecadastramentoDAO recadastramentoDAO) {this.dao = recadastramentoDAO;}	

}
