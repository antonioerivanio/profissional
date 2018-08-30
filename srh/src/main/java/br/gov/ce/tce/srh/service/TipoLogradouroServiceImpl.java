package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.TipoLogradouroDAO;
import br.gov.ce.tce.srh.domain.TipoLogradouro;

@Service("tipoLogradouroService")
public class TipoLogradouroServiceImpl implements TipoLogradouroService {

	@Autowired
	private TipoLogradouroDAO dao;


	@Override
	public List<TipoLogradouro> findAll() {
		return dao.findAll();
	}


	public void setDAO(TipoLogradouroDAO tipoLogradouroDAO) {this.dao = tipoLogradouroDAO;}

}
