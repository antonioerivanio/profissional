package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.ProcessoESocialDAO;
import br.gov.ce.tce.srh.dao.ProcessoESocialSuspensaoDAO;
import br.gov.ce.tce.srh.domain.ProcessoESocial;
import br.gov.ce.tce.srh.domain.ProcessoESocialSuspensao;

@Service("processoESocialService")
public class ProcessoESocialService{

	@Autowired
	private ProcessoESocialDAO dao;
	
	@Autowired
	private ProcessoESocialSuspensaoDAO suspensaoDao;

	@Transactional
	public void salvar(ProcessoESocial entidade) {
		dao.salvar(entidade);
		salvarSuspensoes(entidade);
	}
	
	private void salvarSuspensoes(ProcessoESocial entidade) {
		List<ProcessoESocialSuspensao> suspensoes = entidade.getSuspensoes();
		if (suspensoes != null && suspensoes.size() > 0) {
			for(ProcessoESocialSuspensao suspensao: suspensoes) {
				suspensao.setProcesso(entidade.getId());
				if (suspensao.isExcluida()) {
					suspensaoDao.excluir(suspensao);
				} else {
					suspensaoDao.salvar(suspensao);
				}					
			}
		}
	}
	
	@Transactional
	public void salvarSuspensao(ProcessoESocialSuspensao suspensao) {
		suspensaoDao.salvar(suspensao);		
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
	
	public List<ProcessoESocialSuspensao> getSuspensoes(Long idProcesso){
		return suspensaoDao.getSuspensoes(idProcesso);
	}	

}
