package br.gov.ce.tce.srh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.ESocialEventoDAO;
import br.gov.ce.tce.srh.domain.ESocialEvento;

@Service
public class ESocialEventoService {
	
	@Autowired
	private ESocialEventoDAO esocialEventoDao;

	
	public ESocialEvento salvar(ESocialEvento entidade) {
		if(entidade != null && entidade.getInicioValidade() != null)
			return esocialEventoDao.salvar(entidade);
		
		return null;
	}
	
	public ESocialEvento getById(Long id) {
		return esocialEventoDao.getById(id);
	}

}
