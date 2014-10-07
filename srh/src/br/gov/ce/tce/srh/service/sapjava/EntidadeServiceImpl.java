package br.gov.ce.tce.srh.service.sapjava;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.sapjava.EntidadeDAO;
import br.gov.ce.tce.srh.domain.sapjava.Entidade;
@Service("entidadeServiceImpl")
public class EntidadeServiceImpl implements EntidadeService {
	
	@Autowired
	private EntidadeDAO dao;

	@Override
	public List<Entidade> findAll() {
		return dao.findAll();
	}
	
	@Override
	public List<Entidade> listaOrgaoOrigemComRestricaoTipoEsfera() {
		return dao.listaOrgaoOrigemComRestricaoTipoEsfera();
	}

	public void setDao(EntidadeDAO dao) {
		this.dao = dao;
	}
	
	

}
