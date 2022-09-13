package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.DemonstrativosDeValoresDAO;
import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
import br.gov.ce.tce.srh.domain.RemuneracaoBeneficio;
import br.gov.ce.tce.srh.domain.RemuneracaoServidor;
import br.gov.ce.tce.srh.domain.RemuneracaoTrabalhador;

@Service("demonstrativosDeValoresService")
public class DemonstrativosDeValoresService{

	@Autowired
	private DemonstrativosDeValoresDAO dao;		

	@Transactional
	public DemonstrativosDeValores salvar(DemonstrativosDeValores entidade) {		
		entidade = dao.salvar(entidade);
		return entidade;
	}		

	@Transactional
	public void excluir(DemonstrativosDeValores entidade) {
		dao.excluir(entidade);
	}	

	public DemonstrativosDeValores getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<DemonstrativosDeValores> findDemonstrativosDeValores(String mesReferencia, String anoReferencia, RemuneracaoTrabalhador remuneracaoTrabalhador, Long idFuncional) {
		List<DemonstrativosDeValores> demonstrativosDeValoresList = dao.findDemonstrativosDeValores(mesReferencia, anoReferencia, remuneracaoTrabalhador.getId(), null, idFuncional);
		for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {
			if(demonstrativosDeValores.getId() < 0) {				
				demonstrativosDeValores.setRemuneracaoTrabalhador(remuneracaoTrabalhador);
				demonstrativosDeValores.setId(null);
			}
		}
		return demonstrativosDeValoresList;
	}

	@Transactional
	public void salvar(List<DemonstrativosDeValores> dependentesList) {
		for (DemonstrativosDeValores demonstrativosDeValores : dependentesList) {
			salvar(demonstrativosDeValores);
		}		
	}

	public List<DemonstrativosDeValores> findDemonstrativosDeValoresByIdfuncional(Long idFuncional) {
		return dao.findDemonstrativosDeValoresByIdfuncional(idFuncional);
	}

	public List<DemonstrativosDeValores> findDemonstrativosDeValoresRPA(String mesReferencia, String anoReferencia, RemuneracaoTrabalhador remuneracaoTrabalhador, Long idPrestador) {
		
		List<DemonstrativosDeValores> demonstrativosDeValoresList = dao.findDemonstrativosDeValoresRPA(mesReferencia, anoReferencia, remuneracaoTrabalhador.getId(), idPrestador);
		for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {
			if(demonstrativosDeValores.getId() < 0) {
				demonstrativosDeValores.setRemuneracaoTrabalhador(remuneracaoTrabalhador);
				demonstrativosDeValores.setId(null);
			}
		}
		return demonstrativosDeValoresList;
	}

	public List<DemonstrativosDeValores> findDemonstrativosDeValoresServidor(String mesReferencia, String anoReferencia,
			RemuneracaoServidor remuneracaoServidor, Long idFuncional) {
		List<DemonstrativosDeValores> demonstrativosDeValoresList = dao.findDemonstrativosDeValores(mesReferencia, anoReferencia, null, remuneracaoServidor.getId(), idFuncional);
		for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {
			if(demonstrativosDeValores.getId() < 0) {
				demonstrativosDeValores.setRemuneracaoServidor(remuneracaoServidor);
				demonstrativosDeValores.setId(null);
			}
		}
		return demonstrativosDeValoresList;
	}

	public List<DemonstrativosDeValores> findDemonstrativosDeValoresBeneficio(String mesReferencia,
			String anoReferencia, RemuneracaoBeneficio remuneracaoBeneficio, Long idFuncional) {
		List<DemonstrativosDeValores> demonstrativosDeValoresList = dao.findDemonstrativosDeValoresBeneficio(mesReferencia, anoReferencia, remuneracaoBeneficio.getId(), idFuncional);
		for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {
			if(demonstrativosDeValores.getId() < 0) {
				demonstrativosDeValores.setRemuneracaoBeneficio(remuneracaoBeneficio);
				demonstrativosDeValores.setId(null);
			}
		}
		return demonstrativosDeValoresList;
	}

	

}
