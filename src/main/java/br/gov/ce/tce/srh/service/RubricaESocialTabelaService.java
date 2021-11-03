package br.gov.ce.tce.srh.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.RubricaESocialTabelaDAO;
import br.gov.ce.tce.srh.domain.RubricaESocialTabela;

@Service("rubricaESocialTabelaService")
public class RubricaESocialTabelaService {
    @Autowired
	private RubricaESocialTabelaDAO dao;

    public RubricaESocialTabela getById(Long id) {
		return dao.getById(id);
	}
}
