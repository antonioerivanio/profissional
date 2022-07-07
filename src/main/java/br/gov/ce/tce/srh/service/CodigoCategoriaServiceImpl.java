package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.CodigoCategoriaDAO;
import br.gov.ce.tce.srh.domain.CodigoCategoria;

/**
 * 
 * @author robstown
 *
 */
@Service("codigoCategoriaServiceImpl")
public class CodigoCategoriaServiceImpl implements CodigoCategoriaService {

	@Autowired
	private CodigoCategoriaDAO dao;


	@Override
	public CodigoCategoria getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public CodigoCategoria getByCodigo(Long codigo) {
		return dao.getByCodigo(codigo);
	}


	@Override
	public List<CodigoCategoria> findAll() {
		return dao.findAll();
	}


	@Override
	public List<CodigoCategoria> findByCodigo(Long codigo) {
		return dao.findByCodigo(codigo);
	}


	public void setDAO(CodigoCategoriaDAO dao) {this.dao = dao;}

}
