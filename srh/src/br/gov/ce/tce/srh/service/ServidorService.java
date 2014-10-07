package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Servidor;
import br.gov.ce.tce.srh.domain.sapjava.Setor;

public interface ServidorService {

	List<Servidor> consultarServidoresPorSetor(Setor setor, int firstResult, int maxResults) throws Exception;

	int getCountServidoresPorSetor(Setor setor);

}
