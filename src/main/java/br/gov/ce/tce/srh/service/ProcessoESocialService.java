package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.ProcessoESocialDAO;
import br.gov.ce.tce.srh.domain.ProcessoESocial;

@Service("processoESocialService")
public class ProcessoESocialService{

	@Autowired
	private ProcessoESocialDAO dao;

	@Transactional
	public ProcessoESocial salvar(ProcessoESocial entidade) {
		// TODO Validações
		return dao.salvar(entidade);
	}

	@Transactional
	public void excluir(ProcessoESocial entidade) {
		dao.excluir(entidade);
	}	

	public ProcessoESocial getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<ProcessoESocial> search(String numero, Integer first, Integer rows) {
		return dao.search(numero, first, rows);
	}

}
