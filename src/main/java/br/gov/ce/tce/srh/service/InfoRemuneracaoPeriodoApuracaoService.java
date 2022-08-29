package br.gov.ce.tce.srh.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.InfoRemuneracaoPeriodoApuracaoDAO;
import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
import br.gov.ce.tce.srh.domain.InfoRemuneracaoPeriodoApuracao;

@Service("infoRemuneracaoPeriodoApuracaoService")
public class InfoRemuneracaoPeriodoApuracaoService{

	@Autowired
	private InfoRemuneracaoPeriodoApuracaoDAO dao;
		

	@Transactional
	public InfoRemuneracaoPeriodoApuracao salvar(InfoRemuneracaoPeriodoApuracao entidade) {		
		entidade = dao.salvar(entidade);
		return entidade;
	}		

	@Transactional
	public void excluir(InfoRemuneracaoPeriodoApuracao entidade) {
		dao.excluir(entidade);
	}	

	public InfoRemuneracaoPeriodoApuracao getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<InfoRemuneracaoPeriodoApuracao> findInfoRemuneracaoPeriodoApuracao(String mesReferencia, String anoReferencia, List<DemonstrativosDeValores> demonstrativosDeValoresList, Long idFuncional) {
		List<InfoRemuneracaoPeriodoApuracao> infoRemuneracaoPeriodoApuracaoListReturn = new ArrayList<InfoRemuneracaoPeriodoApuracao>();
		
		if(demonstrativosDeValoresList != null && !demonstrativosDeValoresList.isEmpty()) {
			for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {			
				if(demonstrativosDeValores.getFlInfoRemunPerAnteriores().equals(0)) {
					List<InfoRemuneracaoPeriodoApuracao> infoRemuneracaoPeriodoApuracaoList = dao.findInfoRemuneracaoPeriodoApuracao(mesReferencia, anoReferencia, demonstrativosDeValores, idFuncional);
					demonstrativosDeValores.setInfoPerApur(infoRemuneracaoPeriodoApuracaoList.get(0));
					for (InfoRemuneracaoPeriodoApuracao infoRemuneracaoPeriodoApuracao : infoRemuneracaoPeriodoApuracaoList) {
						if(infoRemuneracaoPeriodoApuracao.getId() < 0) {
							infoRemuneracaoPeriodoApuracao.setId(null);
							infoRemuneracaoPeriodoApuracao.setDemonstrativosDeValores(demonstrativosDeValores);							
						}
					}
					infoRemuneracaoPeriodoApuracaoListReturn.addAll(infoRemuneracaoPeriodoApuracaoList);
					if(infoRemuneracaoPeriodoApuracaoList != null && !infoRemuneracaoPeriodoApuracaoList.isEmpty()) {
						demonstrativosDeValores.setInfoRemuneracaoPeriodoApuracao(infoRemuneracaoPeriodoApuracaoList.get(0));
					}
				}
				
			}
			
		}
		
		
		return infoRemuneracaoPeriodoApuracaoListReturn;
	}

	@Transactional
	public void salvar(List<InfoRemuneracaoPeriodoApuracao> dependentesList) {
		for (InfoRemuneracaoPeriodoApuracao InfoRemuneracaoPeriodoApuracao : dependentesList) {
			salvar(InfoRemuneracaoPeriodoApuracao);
		}
		
	}

	public List<InfoRemuneracaoPeriodoApuracao> findInfoRemuneracaoPeriodoApuracaoByIdfuncional(Long idFuncional) {
		return dao.findInfoRemuneracaoPeriodoApuracaoByIdfuncional(idFuncional);
	}

	public List<InfoRemuneracaoPeriodoApuracao> findInfoRemuneracaoPeriodoApuracaoRPA( List<DemonstrativosDeValores> demonstrativosDeValoresList, Long idPrestador) {
		List<InfoRemuneracaoPeriodoApuracao> infoRemuneracaoPeriodoApuracaoListReturn = new ArrayList<InfoRemuneracaoPeriodoApuracao>();
		
		if(demonstrativosDeValoresList != null && !demonstrativosDeValoresList.isEmpty()) {
			for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {			
				if(demonstrativosDeValores.getFlInfoRemunPerAnteriores().equals(0)) {
					InfoRemuneracaoPeriodoApuracao infoRemuneracaoPeriodoApuracao = dao.findInfoRemuneracaoPeriodoApuracao(demonstrativosDeValores);
					demonstrativosDeValores.setInfoPerApur(infoRemuneracaoPeriodoApuracao);
					demonstrativosDeValores.setInfoRemuneracaoPeriodoApuracao(infoRemuneracaoPeriodoApuracao); 
					infoRemuneracaoPeriodoApuracao.setDemonstrativosDeValores(demonstrativosDeValores);	
					infoRemuneracaoPeriodoApuracaoListReturn.add(infoRemuneracaoPeriodoApuracao);
				}
				
			}
					
					
			
		}
		
		
		return infoRemuneracaoPeriodoApuracaoListReturn;
	}

private InfoRemuneracaoPeriodoApuracao geraInfoRemuneracaoPeriodoApuracaoRPA(
			DemonstrativosDeValores demonstrativosDeValores) {
		
		InfoRemuneracaoPeriodoApuracao infoRemuneracaoPeriodoApuracao = new InfoRemuneracaoPeriodoApuracao();
		//infoRemuneracaoPeriodoApuracao.setId(null);
		infoRemuneracaoPeriodoApuracao.setDemonstrativosDeValores(demonstrativosDeValores);
		infoRemuneracaoPeriodoApuracao.setCodLotacao("LOTACAO-BASICA");
		infoRemuneracaoPeriodoApuracao.setNrInsc("09499757000146");
		infoRemuneracaoPeriodoApuracao.setTpInsc(new Byte("1"));
		infoRemuneracaoPeriodoApuracao.setGrauEX(new Byte("1"));
		
		return infoRemuneracaoPeriodoApuracao;
	}

	

}
