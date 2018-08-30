package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.PaisDAO;
import br.gov.ce.tce.srh.domain.Pais;

@Service("paisService")
public class PaisServiceImpl implements PaisService {

	@Autowired
	private PaisDAO dao;


	@Override
	public List<Pais> findAll() {
		return dao.findAll();
	}


	public void setDAO(PaisDAO paisDAO) {this.dao = paisDAO;}

}
