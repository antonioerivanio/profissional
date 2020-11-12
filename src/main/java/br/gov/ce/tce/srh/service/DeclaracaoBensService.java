package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.DeclaracaoBensDAO;
import br.gov.ce.tce.srh.domain.DeclaracaoBens;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;
import br.gov.ce.tce.srh.util.SRHUtils;

@Service
public class DeclaracaoBensService {
	
	@Autowired
	private DeclaracaoBensDAO declaracaoBensDAO;
	
	@Transactional
	public void salvarDeclaracoes(List<DeclaracaoBens> declaracaoBensList, boolean verificaObrigatoriedade) {
		
		boolean possuiDeclaracaoObrigatoria = false;
		
		for(DeclaracaoBens declaracao: declaracaoBensList) {
			
			if(declaracao.getExercicio() == null || !SRHUtils.anoExercicioValido(declaracao.getExercicio())) {
				throw new SRHRuntimeException("Declaração " + declaracao.getNomeArquivo() + " com exercício inválido");
			}			
			
			if(declaracao.getAnoCalendario() == null || !SRHUtils.anoExercicioValido(declaracao.getAnoCalendario())) {
				throw new SRHRuntimeException("Declaração " + declaracao.getNomeArquivo() + " com ano calendário inválido");
			}
			
			if(declaracao.getAnoCalendario().equals(SRHUtils.getAnoCorrente() - 1)) {
				possuiDeclaracaoObrigatoria = true;
			}		
			
			this.declaracaoBensDAO.salvar(declaracao);
		}  
		
		if(verificaObrigatoriedade && !possuiDeclaracaoObrigatoria) {
			throw new SRHRuntimeException("É obrigatório informar uma declaração de bens e valores com ano calendário " + (SRHUtils.getAnoCorrente() - 1));
		}
	}	
	
	@Transactional
	public void excluir(DeclaracaoBens declaracao) {
		this.declaracaoBensDAO.excluir(declaracao);
	}

	public List<DeclaracaoBens> getByPessoalId(Long id) {
		return this.declaracaoBensDAO.getByPessoalId(id);
	}

}
