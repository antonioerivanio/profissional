package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.RelatorioFeriasDAO;
import br.gov.ce.tce.srh.domain.RelatorioFerias;
import br.gov.ce.tce.srh.domain.sapjava.Setor;
import br.gov.ce.tce.srh.util.SRHUtils;

@Service("relatorioFeriasService")
public class RelatorioFeriasServiceImpl implements RelatorioFeriasService {

	@Autowired
	private RelatorioFeriasDAO dao;

	public void setDAO(RelatorioFeriasDAO dao) {this.dao = dao;}
	
	
	@Override
	public int getCountFindByParameter(Setor setor,
			List<String> tiposFerias, Date inicio, Date fim) {
		return dao.getCountFindByParameter(setor, tiposFerias, inicio, fim);
	}

	@Override
	public List<RelatorioFerias> findByParameter(Setor setor,
			List<String> tiposFerias, Date inicio, Date fim,
			int firstResult, int maxResults) {
		List<RelatorioFerias> relatorioFerias = dao.findByParameter(setor, tiposFerias, inicio, fim, firstResult, maxResults);
		
		for (RelatorioFerias rf: relatorioFerias) {
			rf.setDias(SRHUtils.dataDiff(rf.getInicio(), rf.getFim()));
		}
		
		return relatorioFerias;
	}

}
