package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AmbienteTrabalhoDAO;
import br.gov.ce.tce.srh.domain.AmbienteTrabalho;

@Service("ambienteTrabalhoService")
public class AmbienteTrabalhoService{

	@Autowired
	private AmbienteTrabalhoDAO dao;

	@Transactional
	public AmbienteTrabalho salvar(AmbienteTrabalho entidade) {
		// TODO Validações
		return dao.salvar(entidade);
	}

	@Transactional
	public void excluir(AmbienteTrabalho entidade) {
		dao.excluir(entidade);
	}	

	public AmbienteTrabalho getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<AmbienteTrabalho> search(String descricao, Integer first, Integer rows) {
		return dao.search(descricao, first, rows);
	}

}
