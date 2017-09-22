package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.PessoalCategoriaDAO;
import br.gov.ce.tce.srh.domain.PessoalCategoria;

/**
 * 
 * @author robstown
 *
 */
@Service("pessoalCategoriaServiceImpl")
public class PessoalCategoriaServiceImpl implements PessoalCategoriaService {

	@Autowired
	private PessoalCategoriaDAO dao;


	@Override
	public List<PessoalCategoria> findAll() {
		return dao.findAll();
	}


	public void setDAO(PessoalCategoriaDAO dao) {this.dao = dao;}

}
