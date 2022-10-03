package br.gov.ce.tce.srh.service;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.DependenteEsocialDAO;
import br.gov.ce.tce.srh.domain.AdmissaoVO;
import br.gov.ce.tce.srh.domain.AlteracaoCadastral;
import br.gov.ce.tce.srh.domain.DependenteEsocial;
import br.gov.ce.tce.srh.domain.DependenteEsocialVO;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.util.SRHUtils;

@Service("dependenteEsocialTCEService")
public class DependenteEsocialTCEService{

	@Autowired
	private DependenteEsocialDAO dao;
	
	@Autowired
	private AlteracaoCadastralEsocialService alteracaoCadastralEsocialService;
	
	@Autowired
	private AdmissaoEsocialService admissaoEsocialService;
		

	@Transactional
	public DependenteEsocial salvar(DependenteEsocial entidade) {		
		entidade = dao.salvar(entidade);
		return entidade;
	}		

	@Transactional
	public void excluir(DependenteEsocial entidade) {
		dao.excluir(entidade);
	}	

	public DependenteEsocial getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<DependenteEsocial> findByIdfuncional(Long idFuncional) {
		List<DependenteEsocial> depentendes = dao.findByIdfuncional(idFuncional);
		for (DependenteEsocial dependenteEsocial : depentendes) {
			dependenteEsocial.setId(null);
		}
		return depentendes;
	}
	public List<DependenteEsocialVO> findDependenteByIdfuncional(Long idFuncional) {
		return dao.findDependenteByIdfuncional(idFuncional);	
	}

	@Transactional
	public void salvar(List<DependenteEsocial> dependentesList) {
		for (DependenteEsocial dependenteEsocial : dependentesList) {
			salvar(dependenteEsocial);
		}
		
	}

	public List<DependenteEsocial> findDependenteEsocialByIdfuncional(Long idFuncional) {
		return dao.findDependenteEsocialByIdfuncional(idFuncional);
	}

	public void excluirAll(List<DependenteEsocial> dependentesList) {
		for (DependenteEsocial dependenteEsocial : dependentesList) {
			DependenteEsocial dependenteEsocialExcluir = dao.getById(dependenteEsocial.getId());
			dao.excluir(dependenteEsocialExcluir);
		}
		
	}
	
	public Boolean checkHouveAlteracaoByServidor(final Funcional servidorFuncional, final boolean possuiCargo) throws NoSuchAlgorithmException {		
		final AlteracaoCadastral entidade = alteracaoCadastralEsocialService.getEventoS2205ByServidor(servidorFuncional, possuiCargo);
		final AdmissaoVO admissaoSRH =  admissaoEsocialService.getEventoS2200ByServidor(servidorFuncional);
		final List<DependenteEsocial> dependentesList = findByIdfuncional(servidorFuncional.getId());
		//dependentesList.set(0, null);
		admissaoSRH.setDependentesList(dependentesList);

		final String admissaoHashString = SRHUtils.getStringMD5HashFromObject(admissaoSRH.toString().getBytes());

		//Admissao admissaoConector =  admissaoEsocialService.getEventoS2200ConectorByReferencia(admissaoSRH.getReferencia());
		final AdmissaoVO admissaoConector =  admissaoEsocialService.getEventoS2200ConectorByReferencia(admissaoSRH.getReferencia());				
		final List<DependenteEsocial> dependentesEsocialList  = findByIdfuncional(servidorFuncional.getId());
		admissaoConector.setDependentesList(dependentesEsocialList);
		admissaoConector.setCBOCargo("252206");

		final String admissaoConectorHashString = SRHUtils.getStringMD5HashFromObject(admissaoConector.toString().getBytes());

		if(!admissaoHashString.equals(admissaoConectorHashString)) {
			return Boolean.TRUE;
		}

		return Boolean.FALSE;
	}
}
