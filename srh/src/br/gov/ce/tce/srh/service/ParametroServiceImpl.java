package br.gov.ce.tce.srh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.ParametroDAO;
import br.gov.ce.tce.srh.domain.Parametro;

/**
 * 
 * @author robstown
 *
 */
@Service("parametroService")
public class ParametroServiceImpl implements ParametroService {

	@Autowired
	private ParametroDAO dao;


	@Override
	public Parametro getByNome(String nome) {
		return dao.getByNome(nome);
	}


	public void setDAO(ParametroDAO parametroDAO) {this.dao = parametroDAO;}

}
