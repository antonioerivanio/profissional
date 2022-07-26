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
		List<InfoRemuneracaoPeriodoApuracao> InfoRemuneracaoPeriodoApuracaoListReturn = new ArrayList<InfoRemuneracaoPeriodoApuracao>();
		
		if(demonstrativosDeValoresList != null && !demonstrativosDeValoresList.isEmpty()) {
			for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {			
				if(demonstrativosDeValores.getFlInfoRemunPerAnteriores().equals(0)) {
					List<InfoRemuneracaoPeriodoApuracao> InfoRemuneracaoPeriodoApuracaoList = dao.findInfoRemuneracaoPeriodoApuracao(mesReferencia, anoReferencia, demonstrativosDeValores, idFuncional);
					InfoRemuneracaoPeriodoApuracaoListReturn.addAll(InfoRemuneracaoPeriodoApuracaoList);
					if(InfoRemuneracaoPeriodoApuracaoList != null && !InfoRemuneracaoPeriodoApuracaoList.isEmpty()) {
						demonstrativosDeValores.setInfoRemuneracaoPeriodoApuracao(InfoRemuneracaoPeriodoApuracaoList.get(0));
					}
				}
				
			}
			for (InfoRemuneracaoPeriodoApuracao InfoRemuneracaoPeriodoApuracao : InfoRemuneracaoPeriodoApuracaoListReturn) {
				if(InfoRemuneracaoPeriodoApuracao.getId() < 0) {
					InfoRemuneracaoPeriodoApuracao.setId(null);
				}
			}
		}
		
		return InfoRemuneracaoPeriodoApuracaoListReturn;
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

	

}
