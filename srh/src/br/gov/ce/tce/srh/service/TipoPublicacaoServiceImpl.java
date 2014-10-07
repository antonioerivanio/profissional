package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.TipoPublicacaoDAO;
import br.gov.ce.tce.srh.domain.TipoPublicacao;

/**
 * 
 * @author robstown
 *
 */
@Service("tipoPublicacaoService")
public class TipoPublicacaoServiceImpl implements TipoPublicacaoService {

	@Autowired
	private TipoPublicacaoDAO dao;


	@Override
	public List<TipoPublicacao> findAll() {
		return dao.findAll();
	}


	public void setDAO(TipoPublicacaoDAO dao) {this.dao = dao;}
	
}
