package br.gov.ce.tce.srh.dao;

import java.util.List;

import br.gov.ce.tce.srh.domain.Servidor;
import br.gov.ce.tce.srh.domain.sapjava.Setor;

public interface ServidorDAO {

	List<Servidor> consultarServidoresPorSetor(Setor setor, Integer vinculo, int firstResult, int maxResults) throws Exception;

	int getCountServidoresPorSetor(Setor setor, Integer vinculo);

}
