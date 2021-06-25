package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.FolgaDAO;
import br.gov.ce.tce.srh.domain.Folga;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("folgaService")
public class FolgaService {

	@Autowired
	private FolgaDAO dao;
	
	@Transactional
	public Folga salvar(Folga entidade) {
		if(entidade.getId() == null) {
			entidade.setSaldoFinal(entidade.getSaldoInicial());
		}
		
		validarDados(entidade);
		
		return dao.salvar(entidade);
	}
	
	private void validarDados(Folga entidade) {
		
		if(entidade.getPessoal() == null) {
			throw new SRHRuntimeException("A pessoa é obrigatória.");			
		}
		
		if(entidade.getTipoFolga() == null) {
			throw new SRHRuntimeException("O tipo de folga é obrigatório.");			
		}
		
		if(entidade.getSaldoInicial() == null || entidade.getSaldoInicial() == 0) {
			throw new SRHRuntimeException("O saldo inicial é obrigatório.");
		}
		
		if(entidade.getInicio() == null) {
			throw new SRHRuntimeException("A data inicial é obrigatória.");
		}
		
		if(entidade.getDescricao() == null || entidade.getDescricao().trim().equals("")) {
			throw new SRHRuntimeException("A descrição é obrigatória.");
		}
		
	}

	public int count(Long idPessoal) {
		return dao.count(idPessoal);
	}
	
	public List<Folga> search(Long idPessoal, Integer first, Integer rows) {
		return dao.search(idPessoal, first, rows);
	}
	
	@Transactional
	public void excluir(Folga entidade) {
		Folga folga = dao.findById(entidade.getId());
		
		if(folga != null) { 
			if (folga.getDebitos() == null || folga.getDebitos().size() == 0) {
				dao.excluir(entidade);
			} else {
				throw new SRHRuntimeException("Exclusão não permitida. Débitos já foram lançados para esta folga.");
			}
		} 		
	}
	
	public Double saldoTotal(Long idPessoal) {
		return dao.saldoTotal(idPessoal);
	}
	
	public Folga findById(Long id) {
		return dao.findById(id);
	}
	
}
