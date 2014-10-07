package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.TipoFeriasDAO;
import br.gov.ce.tce.srh.domain.TipoFerias;

/**
 * 
 * @author joel.barbosa
 *
 */
@Service("tipoFeriasService")
public class TipoFeriasServiceImpl implements TipoFeriasService {

	@Autowired
	private TipoFeriasDAO dao;


	@Override
	public List<TipoFerias> findAll() {
		return dao.findAll();
	}


	public void setDAO(TipoFeriasDAO dao){this.dao = dao;}

}
