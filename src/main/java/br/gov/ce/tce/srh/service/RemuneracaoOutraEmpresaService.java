package br.gov.ce.tce.srh.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RemuneracaoOutraEmpresaDAO;
import br.gov.ce.tce.srh.domain.RemuneracaoOutraEmpresa;
import br.gov.ce.tce.srh.domain.RemuneracaoTrabalhador;

@Service("remuneracaoOutraEmpresaService")
public class RemuneracaoOutraEmpresaService{

	@Autowired
	private RemuneracaoOutraEmpresaDAO dao;		

	@Transactional
	public RemuneracaoOutraEmpresa salvar(RemuneracaoOutraEmpresa entidade) {		
		entidade = dao.salvar(entidade);
		return entidade;
	}		

	@Transactional
	public void excluir(RemuneracaoOutraEmpresa entidade) {
		dao.excluir(entidade);
	}	

	public RemuneracaoOutraEmpresa getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<RemuneracaoOutraEmpresa> findRemuneracaoOutraEmpresa(String mesReferencia, String anoReferencia, RemuneracaoTrabalhador remuneracaoTrabalhador, Long idFuncional) {
		List<RemuneracaoOutraEmpresa> remuneracaoOutraEmpresaList = dao.findRemuneracaoOutraEmpresa(mesReferencia, anoReferencia, remuneracaoTrabalhador.getId(), idFuncional);
		for (RemuneracaoOutraEmpresa remuneracaoOutraEmpresa : remuneracaoOutraEmpresaList) {
			if(remuneracaoOutraEmpresa.getId() < 0) {
				remuneracaoOutraEmpresa.setId(null);
				remuneracaoOutraEmpresa.setRemuneracaoTrabalhador(remuneracaoTrabalhador);
			}
		}
		return remuneracaoOutraEmpresaList;
	}

	@Transactional
	public void salvar(List<RemuneracaoOutraEmpresa> dependentesList) {
		for (RemuneracaoOutraEmpresa RemuneracaoOutraEmpresa : dependentesList) {
			salvar(RemuneracaoOutraEmpresa);
		}		
	}

	public List<RemuneracaoOutraEmpresa> findRemuneracaoOutraEmpresaByIdfuncional(Long idFuncional) {
		return dao.findRemuneracaoOutraEmpresaByIdfuncional(idFuncional);
	}

	public List<RemuneracaoOutraEmpresa> findRemuneracaoOutraEmpresaRPA(String mesReferencia, String anoReferencia,
			RemuneracaoTrabalhador remuneracaoTrabalhador, Long idPrestador) {
		
		List<RemuneracaoOutraEmpresa> remuneracaoOutraEmpresaList = dao.findRemuneracaoOutraEmpresaRPA(mesReferencia, anoReferencia, remuneracaoTrabalhador.getId(), idPrestador);
		for (RemuneracaoOutraEmpresa remuneracaoOutraEmpresa : remuneracaoOutraEmpresaList) {
			if(remuneracaoOutraEmpresa.getId() < 0) {
				remuneracaoOutraEmpresa.setId(null);
				remuneracaoOutraEmpresa.setRemuneracaoTrabalhador(remuneracaoTrabalhador);
			}
		}
		return remuneracaoOutraEmpresaList;
	}

	public BigDecimal getINSS(String anoMes) {		
		return dao.getINSS(anoMes);
	}

	

}
