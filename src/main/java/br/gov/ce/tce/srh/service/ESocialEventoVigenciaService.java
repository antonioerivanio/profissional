package br.gov.ce.tce.srh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.ESocialEventoVigenciaDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;

@Service
public class ESocialEventoVigenciaService {
	
	@Autowired
	private ESocialEventoVigenciaDAO esocialEventoDao;

	@Transactional
	public ESocialEventoVigencia salvar(ESocialEventoVigencia entidade) {			
		if (entidade.getId() != null) {
			ESocialEventoVigencia entidadeNoBanco = this.getById(entidade.getId());
			
			if (entidadeNoBanco != null && !entidade.equals(entidadeNoBanco)) {
				entidade.setTransmitido(false);
			}			
		}				
		return esocialEventoDao.salvar(entidade);		
	}
	
	public ESocialEventoVigencia getById(Long id) {
		return esocialEventoDao.getById(id);
	}

}
