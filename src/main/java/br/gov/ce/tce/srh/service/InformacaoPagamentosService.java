package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.InformacaoPagamentosDAO;
import br.gov.ce.tce.srh.domain.InformacaoPagamentos;
import br.gov.ce.tce.srh.domain.Pagamentos;

@Service("informacaoPagamentosService")
public class InformacaoPagamentosService{

	@Autowired
	private InformacaoPagamentosDAO dao;		

	@Transactional
	public InformacaoPagamentos salvar(InformacaoPagamentos entidade) {		
		entidade = dao.salvar(entidade);
		return entidade;
	}		

	@Transactional
	public void excluir(InformacaoPagamentos entidade) {
		dao.excluir(entidade);
	}	

	public InformacaoPagamentos getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<InformacaoPagamentos> findInformacaoPagamentos(String mesReferencia, String anoReferencia, Pagamentos pagamentos, Long idFuncional) {
		List<InformacaoPagamentos> informacaoPagamentosList = dao.findInformacaoPagamentos(mesReferencia, anoReferencia, pagamentos.getId(), null, idFuncional);
		for (InformacaoPagamentos informacaoPagamentos : informacaoPagamentosList) {
			if(informacaoPagamentos.getId() < 0) {				
				informacaoPagamentos.setPagamentos(pagamentos);
				informacaoPagamentos.setId(null);
			}
		}
		return informacaoPagamentosList;
	}

	@Transactional
	public void salvar(List<InformacaoPagamentos> informacaoPagamentosList) {
		for (InformacaoPagamentos informacaoPagamentos : informacaoPagamentosList) {
			salvar(informacaoPagamentos);
		}		
	}

	public List<InformacaoPagamentos> findInformacaoPagamentosByIdfuncional(Long idFuncional) {
		return dao.findInformacaoPagamentosByIdfuncional(idFuncional);
	}	

}
