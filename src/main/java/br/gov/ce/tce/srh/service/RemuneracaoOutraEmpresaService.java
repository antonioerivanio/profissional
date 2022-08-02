package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RemuneracaoOutraEmpresaDAO;
import br.gov.ce.tce.srh.domain.RemuneracaoOutraEmpresa;

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
	
	public List<RemuneracaoOutraEmpresa> findRemuneracaoOutraEmpresa(String mesReferencia, String anoReferencia, Long idRemuneracaoTrabalhador, Long idFuncional) {
		List<RemuneracaoOutraEmpresa> RemuneracaoOutraEmpresaList = dao.findRemuneracaoOutraEmpresa(mesReferencia, anoReferencia, idRemuneracaoTrabalhador, idFuncional);
		for (RemuneracaoOutraEmpresa RemuneracaoOutraEmpresa : RemuneracaoOutraEmpresaList) {
			if(RemuneracaoOutraEmpresa.getId() < 0) {
				RemuneracaoOutraEmpresa.setId(null);
			}
		}
		return RemuneracaoOutraEmpresaList;
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

	

}
