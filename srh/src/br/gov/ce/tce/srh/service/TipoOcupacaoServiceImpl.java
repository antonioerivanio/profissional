package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.TipoOcupacaoDAO;
import br.gov.ce.tce.srh.domain.TipoOcupacao;

/**
 * 
 * @author robstown
 *
 */
@Service("tipoOcupacaoService")
public class TipoOcupacaoServiceImpl implements TipoOcupacaoService {

	@Autowired
	private TipoOcupacaoDAO dao;


	@Override
	public TipoOcupacao getById(Long id) {
		return dao.getById(id);
	}


	@Override
	public List<TipoOcupacao> findAll() {
		return dao.findAll();
	}


	public void setDAO(TipoOcupacaoDAO tipoOcupacaoDAO) {this.dao = tipoOcupacaoDAO;}

}
