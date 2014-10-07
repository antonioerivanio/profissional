package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.UfDAO;
import br.gov.ce.tce.srh.domain.Uf;

/**
 * 
 * @author robstown
 *
 */
@Service("ufService")
public class UfServiceImpl implements UfService {

	@Autowired
	private UfDAO dao;


	@Override
	public List<Uf> findAll() {
		return dao.findAll();
	}


	public void setDAO(UfDAO ufDAO) {this.dao = ufDAO;}

}
