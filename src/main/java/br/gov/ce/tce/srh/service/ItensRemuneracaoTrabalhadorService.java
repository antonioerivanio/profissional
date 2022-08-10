package br.gov.ce.tce.srh.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.DadosPagtoPrestadorDAO;
import br.gov.ce.tce.srh.dao.ItensRemuneracaoTrabalhadorDAO;
import br.gov.ce.tce.srh.domain.DadosPagtoPrestador;
import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
import br.gov.ce.tce.srh.domain.ItensRemuneracaoTrabalhador;

@Service("itensRemuneracaoTrabalhadorService")
public class ItensRemuneracaoTrabalhadorService{

	@Autowired
	private ItensRemuneracaoTrabalhadorDAO dao;
	
	@Autowired
	private DadosPagtoPrestadorDAO dadosPagtoPrestadorDAO;
		

	@Transactional
	public ItensRemuneracaoTrabalhador salvar(ItensRemuneracaoTrabalhador entidade) {		
		entidade = dao.salvar(entidade);
		return entidade;
	}		

	@Transactional
	public void excluir(ItensRemuneracaoTrabalhador entidade) {
		dao.excluir(entidade);
	}	

	public ItensRemuneracaoTrabalhador getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<ItensRemuneracaoTrabalhador> findByDemonstrativosDeValores(List<DemonstrativosDeValores> demonstrativosDeValoresList) {
		List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorReturnList = new  ArrayList<ItensRemuneracaoTrabalhador>();
		String matricula = "";
		if(demonstrativosDeValoresList != null && !demonstrativosDeValoresList.isEmpty()) {
			for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {
				if(demonstrativosDeValores.getFlInfoRemunPerAnteriores().equals(1)) {
					matricula = demonstrativosDeValores.getInfoRemuneracaoPeriodoAnteriores().getMatricula().substring(1);
				}
				else {
					matricula = demonstrativosDeValores.getInfoRemuneracaoPeriodoApuracao().getMatricula().substring(1);
				}
				
				List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList = dao.findByDemonstrativosDeValores(demonstrativosDeValores, matricula);
				if(itensRemuneracaoTrabalhadorList != null && !itensRemuneracaoTrabalhadorList.isEmpty()) {	
					for (ItensRemuneracaoTrabalhador itensRemuneracaoTrabalhador : itensRemuneracaoTrabalhadorList) {
						itensRemuneracaoTrabalhador.setId(null);
						if(demonstrativosDeValores.getFlInfoRemunPerAnteriores().equals(1)) {
							itensRemuneracaoTrabalhador.setInfoRemuneracaoPeriodoAnteriores(demonstrativosDeValores.getInfoPerAnt());
						}
						else {
							itensRemuneracaoTrabalhador.setInfoRemuneracaoPeriodoApuracao(demonstrativosDeValores.getInfoPerApur());
						}
						
					}
					itensRemuneracaoTrabalhadorReturnList.addAll(itensRemuneracaoTrabalhadorList);	
					demonstrativosDeValores.setItensRemuneracaoTrabalhadorList(itensRemuneracaoTrabalhadorList);
					if(demonstrativosDeValores.getFlInfoRemunPerAnteriores().equals(1)) {
						demonstrativosDeValores.getInfoPerAnt().setItensRemun(itensRemuneracaoTrabalhadorList);
					}
					else {
						demonstrativosDeValores.getInfoPerApur().setItensRemun(itensRemuneracaoTrabalhadorList);
					}
					
				}
				
			}
			
		}
		return itensRemuneracaoTrabalhadorReturnList;
	}

	@Transactional
	public void salvar(List<ItensRemuneracaoTrabalhador> dependentesList) {
		for (ItensRemuneracaoTrabalhador ItensRemuneracaoTrabalhador : dependentesList) {
			salvar(ItensRemuneracaoTrabalhador);
		}
		
	}

	public List<ItensRemuneracaoTrabalhador> findItensRemuneracaoTrabalhadorByIdfuncional(Long idFuncional) {
		return dao.findItensRemuneracaoTrabalhadorByIdfuncional(idFuncional);
	}

	public List<ItensRemuneracaoTrabalhador> findByDemonstrativosDeValoresRPA( String mesReferencia, String anoReferencia, 
			List<DemonstrativosDeValores> demonstrativosDeValoresList) {
		
		List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorReturnList = new  ArrayList<ItensRemuneracaoTrabalhador>();
		String competencia = anoReferencia+mesReferencia;
		if(demonstrativosDeValoresList != null && !demonstrativosDeValoresList.isEmpty()) {
			for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {
				
				Long idPagtoPrestador = new Long(demonstrativosDeValores.getIdeDmDev().replace("RPA_", ""));
				DadosPagtoPrestador dadosPagtoPrestador = dadosPagtoPrestadorDAO.findDadosPagtoPrestador(competencia, idPagtoPrestador);
				List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList = getItensRemuneracaoTrabalhador(dadosPagtoPrestador);
				if(itensRemuneracaoTrabalhadorList != null && !itensRemuneracaoTrabalhadorList.isEmpty()) {	
					for (ItensRemuneracaoTrabalhador itensRemuneracaoTrabalhador : itensRemuneracaoTrabalhadorList) {
						itensRemuneracaoTrabalhador.setId(null);
						if(demonstrativosDeValores.getFlInfoRemunPerAnteriores().equals(1)) {
							itensRemuneracaoTrabalhador.setInfoRemuneracaoPeriodoAnteriores(demonstrativosDeValores.getInfoPerAnt());
						}
						else {
							itensRemuneracaoTrabalhador.setInfoRemuneracaoPeriodoApuracao(demonstrativosDeValores.getInfoPerApur());
						}
						
					}
					itensRemuneracaoTrabalhadorReturnList.addAll(itensRemuneracaoTrabalhadorList);	
					demonstrativosDeValores.setItensRemuneracaoTrabalhadorList(itensRemuneracaoTrabalhadorList);
					if(demonstrativosDeValores.getFlInfoRemunPerAnteriores().equals(1)) {
						demonstrativosDeValores.getInfoPerAnt().setItensRemun(itensRemuneracaoTrabalhadorList);
					}
					else {
						demonstrativosDeValores.getInfoPerApur().setItensRemun(itensRemuneracaoTrabalhadorList);
					}
					
				}
				
			}
			
		}
		return itensRemuneracaoTrabalhadorReturnList;
	}

	private List<ItensRemuneracaoTrabalhador> getItensRemuneracaoTrabalhador(DadosPagtoPrestador dadosPagtoPrestador) {
		List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorRPAList = new  ArrayList<ItensRemuneracaoTrabalhador>();
		if(dadosPagtoPrestador.getVlrBruto().compareTo(new BigDecimal(0)) > 0){
			ItensRemuneracaoTrabalhador vlrBruto = new ItensRemuneracaoTrabalhador();
			vlrBruto.setId(null);
			vlrBruto.setCodRubr("252");
			vlrBruto.setIdeTabRubr("001");
			vlrBruto.setIndApurIr(new Byte("1"));
			vlrBruto.setVlrRubr(dadosPagtoPrestador.getVlrBruto());
			itensRemuneracaoTrabalhadorRPAList.add(vlrBruto);
		}
		
		if(dadosPagtoPrestador.getVlrInssOrigem().compareTo(new BigDecimal(0)) > 0){
			ItensRemuneracaoTrabalhador vlrInssOrigem = new ItensRemuneracaoTrabalhador();
			vlrInssOrigem.setId(null);
			vlrInssOrigem.setCodRubr("612");
			vlrInssOrigem.setIdeTabRubr("001");
			vlrInssOrigem.setIndApurIr(new Byte("1"));
			vlrInssOrigem.setVlrRubr(dadosPagtoPrestador.getVlrInssOrigem());
			itensRemuneracaoTrabalhadorRPAList.add(vlrInssOrigem);
					
		}
		
		if(dadosPagtoPrestador.getVlrIss().compareTo(new BigDecimal(0)) > 0){
			ItensRemuneracaoTrabalhador vlrIss = new ItensRemuneracaoTrabalhador();
			vlrIss.setId(null);
			vlrIss.setCodRubr("640");
			vlrIss.setIdeTabRubr("001");
			vlrIss.setIndApurIr(new Byte("1"));
			vlrIss.setVlrRubr(dadosPagtoPrestador.getVlrIss());
			itensRemuneracaoTrabalhadorRPAList.add(vlrIss);
		}
		
		if(dadosPagtoPrestador.getVlrInss().compareTo(new BigDecimal(0)) > 0){
			ItensRemuneracaoTrabalhador vlrInss = new ItensRemuneracaoTrabalhador();
			vlrInss.setId(null);
			vlrInss.setCodRubr("252");
			vlrInss.setIdeTabRubr("001");
			vlrInss.setIndApurIr(new Byte("1"));
			vlrInss.setVlrRubr(dadosPagtoPrestador.getVlrInss());
			itensRemuneracaoTrabalhadorRPAList.add(vlrInss);
		}
		return itensRemuneracaoTrabalhadorRPAList;
	}

	

}
