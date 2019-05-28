package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.CboDAO;
import br.gov.ce.tce.srh.domain.Cbo;

/**
 * 
 * @author robstown
 *
 */
@Service("cboServiceImpl")
public class CboServiceImpl implements CboService {

	@Autowired
	private CboDAO dao;


	@Override
	public Cbo getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public Cbo getByCodigo(String codigo) {
		return dao.getByCodigo(codigo);
	}


	@Override
	public List<Cbo> findAll() {
		return dao.findAll();
	}


	@Override
	public List<Cbo> findByNivel(Long nivel) {
		return dao.findByNivel(nivel);
	}


	@Override
	public List<Cbo> findByNivelCodigo(Long nivel, String codigo) {
		return dao.findByNivelCodigo(nivel, codigo);
	}


	public void setDAO(CboDAO dao) {this.dao = dao;}

}
