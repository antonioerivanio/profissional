package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.MunicipioDAO;
import br.gov.ce.tce.srh.domain.Municipio;


@Service
public class MunicipioService {

	@Autowired
	private MunicipioDAO dao;


	public List<Municipio> findByUF(String uf) {
		return dao.findByUF(uf);
	}
	
	public Municipio findByCodigoIBGE(String codigoIBGE) {
		return dao.findByCodigoIBGE(codigoIBGE);
	}


	public void setDAO(MunicipioDAO dao) {this.dao = dao;}

}
