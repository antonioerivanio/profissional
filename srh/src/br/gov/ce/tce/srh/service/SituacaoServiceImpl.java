package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.SituacaoDAO;
import br.gov.ce.tce.srh.domain.Situacao;

/**
 * 
 * @author robstown
 *
 */
@Service("situacaoServiceImpl")
public class SituacaoServiceImpl implements SituacaoService {

	@Autowired
	private SituacaoDAO dao;


	@Override
	public List<Situacao> findAll() {
		return dao.findAll();
	}

	public void setDAO(SituacaoDAO dao) {this.dao = dao;}

}
