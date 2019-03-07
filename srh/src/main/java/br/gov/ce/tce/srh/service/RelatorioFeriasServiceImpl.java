package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.RelatorioFeriasDAO;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.RelatorioFerias;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

@Service("relatorioFeriasService")
public class RelatorioFeriasServiceImpl implements RelatorioFeriasService {

	@Autowired
	private RelatorioFeriasDAO dao;

	public void setDAO(RelatorioFeriasDAO dao) {this.dao = dao;}
	
	
	@Override
	public int getCountFindByParameter(Funcional funcional, Setor setor, List<String> tiposFerias, Date inicio, Date fim, Long anoReferencia, TipoOcupacao tipoOcupacao) {
		return dao.getCountFindByParameter(funcional, setor, tiposFerias, inicio, fim, anoReferencia, tipoOcupacao);
	}

	@Override
	public List<RelatorioFerias> findByParameter(Funcional funcional, Setor setor, List<String> tiposFerias, Date inicio, Date fim, Long anoReferencia, TipoOcupacao tipoOcupacao, int firstResult, int maxResults) {
		return dao.findByParameter(funcional, setor, tiposFerias, inicio, fim, anoReferencia, tipoOcupacao, firstResult, maxResults);		
	}

}
