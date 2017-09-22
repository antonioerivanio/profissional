package br.gov.ce.tce.srh.dao;

import java.util.Date;
import java.util.List;

import br.gov.ce.tce.srh.domain.RelatorioFerias;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

public interface RelatorioFeriasDAO {
	public abstract int getCountFindByParameter(Setor setor, List<String> tiposFerias, Date inicio, Date fim, TipoOcupacao tipoOcupacao);
	public abstract List<RelatorioFerias> findByParameter(Setor setor, List<String> tiposFerias, Date inicio, Date fim, TipoOcupacao tipoOcupacao, int firstResult, int maxResults);
}