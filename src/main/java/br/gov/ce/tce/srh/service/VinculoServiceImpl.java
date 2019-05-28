package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.VinculoDAO;
import br.gov.ce.tce.srh.domain.Vinculo;

/**
 * 
 * @author robstown
 *
 */
@Service("vinculoServiceImpl")
public class VinculoServiceImpl implements VinculoService {

	@Autowired
	private VinculoDAO dao;


	@Override
	public Vinculo getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public List<Vinculo> findAll() {
		return dao.findAll();
	}


	public void setDao(VinculoDAO vinculoDAO) {this.dao = vinculoDAO;}

}
