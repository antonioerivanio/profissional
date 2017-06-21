package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.RelatorioFeriasDAO;
import br.gov.ce.tce.srh.domain.RelatorioFerias;
import br.gov.ce.tce.srh.domain.TipoOcupacao;
import br.gov.ce.tce.srh.sapjava.domain.Setor;

@Service("relatorioFeriasService")
public class RelatorioFeriasServiceImpl implements RelatorioFeriasService {

	@Autowired
	private RelatorioFeriasDAO dao;

	public void setDAO(RelatorioFeriasDAO dao) {this.dao = dao;}
	
	
	@Override
	public int getCountFindByParameter(Setor setor, List<String> tiposFerias, Date inicio, Date fim, TipoOcupacao tipoOcupacao) {
		return dao.getCountFindByParameter(setor, tiposFerias, inicio, fim, tipoOcupacao);
	}

	@Override
	public List<RelatorioFerias> findByParameter(Setor setor, List<String> tiposFerias, Date inicio, Date fim, TipoOcupacao tipoOcupacao, int firstResult, int maxResults) {
		return dao.findByParameter(setor, tiposFerias, inicio, fim, tipoOcupacao, firstResult, maxResults);		
	}

}
