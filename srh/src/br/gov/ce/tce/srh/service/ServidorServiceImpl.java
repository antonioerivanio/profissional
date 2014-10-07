package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.ServidorDAO;
import br.gov.ce.tce.srh.domain.Servidor;
import br.gov.ce.tce.srh.domain.sapjava.Setor;

@Service("servidorService")
public class ServidorServiceImpl implements ServidorService {

	@Autowired
	private ServidorDAO dao;
	
	@Transactional
	@Override
	public List<Servidor> consultarServidoresPorSetor(Setor setor, int firstResult, int maxResults) throws Exception {
		return dao.consultarServidoresPorSetor(setor, firstResult, maxResults);
	}

	@Transactional
	@Override
	public int getCountServidoresPorSetor(Setor setor) {
		return dao.getCountServidoresPorSetor(setor);
	}

}
