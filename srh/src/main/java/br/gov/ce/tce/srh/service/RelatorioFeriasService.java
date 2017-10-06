package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import br.gov.ce.tce.srh.domain.RelatorioFerias;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface RelatorioFeriasService {
	public abstract int getCountFindByParameter(Setor setor, List<String> tiposFerias, Date inicio, Date fim, Long anoReferencia, TipoOcupacao tipoOcupacao);
	public abstract List<RelatorioFerias> findByParameter(Setor setor, List<String> tiposFerias, Date inicio, Date fim, Long anoReferencia, TipoOcupacao tipoOcupacao, int firstResult, int maxResults);	
}
