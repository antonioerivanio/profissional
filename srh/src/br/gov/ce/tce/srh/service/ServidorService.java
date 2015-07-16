package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.domain.Servidor;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface ServidorService {

	List<Servidor> consultarServidoresPorSetor(Setor setor, Integer vinculo, int firstResult, int maxResults) throws Exception;

	int getCountServidoresPorSetor(Setor setor, Integer vinculo);

}
