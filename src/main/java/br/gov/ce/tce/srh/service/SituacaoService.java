package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.SituacaoDAO;
import br.gov.ce.tce.srh.domain.Situacao;

@Service("situacaoService")
public class SituacaoService {

	@Autowired
	private SituacaoDAO dao;

	public Situacao findById(Long id) {
		return dao.getById(id);
	}

	public List<Situacao> findAll() {
		return dao.findAll();
	}

	public void setDAO(SituacaoDAO dao) {this.dao = dao;}

}
