package br.gov.ce.tce.srh.service;

import java.util.List;

import br.gov.ce.tce.srh.sapjava.domain.Setor;
import br.gov.ce.tce.srh.to.Servidor;

public interface ServidorService {

	List<Servidor> consultarServidoresPorSetor(Setor setor, Integer vinculo, Boolean ativoPortal, int firstResult, int maxResults) throws Exception;

	int getCountServidoresPorSetor(Setor setor, Integer vinculo, Boolean ativoPortal);

}
