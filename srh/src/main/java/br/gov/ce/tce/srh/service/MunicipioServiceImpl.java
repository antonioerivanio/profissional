package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.MunicipioDAO;
import br.gov.ce.tce.srh.domain.Municipio;

/**
 * 
 * @author robstown
 *
 */
@Service("municipioServiceImpl")
public class MunicipioServiceImpl implements MunicipioService {

	@Autowired
	private MunicipioDAO dao;


	@Override
	public List<Municipio> findByUF(String uf) {
		return dao.findByUF(uf);
	}


	public void setDAO(MunicipioDAO dao) {this.dao = dao;}

}
