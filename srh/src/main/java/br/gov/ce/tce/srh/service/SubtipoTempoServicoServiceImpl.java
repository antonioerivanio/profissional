package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.SubtipoTempoServicoDAO;
import br.gov.ce.tce.srh.domain.SubtipoTempoServico;

@Service("subtipoTempoServicoService")
public class SubtipoTempoServicoServiceImpl implements SubtipoTempoServicoService{

	@Autowired
	private SubtipoTempoServicoDAO dao;
	
	@Override
	public SubtipoTempoServico getById(Long id) {
		return dao.getById(id);
	}

	@Override
	public List<SubtipoTempoServico> findByTipoTempoServico(Long idTipoTempoServico) {
		return dao.findByTipoTempoServico(idTipoTempoServico);
	}

	@Override
	public List<SubtipoTempoServico> findAll() {
		return dao.findAll();
	}

}
