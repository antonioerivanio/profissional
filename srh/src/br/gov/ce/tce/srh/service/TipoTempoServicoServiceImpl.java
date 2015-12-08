package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.ce.tce.srh.dao.TipoTempoServicoDAO;
import br.gov.ce.tce.srh.domain.TipoTempoServico;

@Service("tipoTempoServicoService")
public class TipoTempoServicoServiceImpl implements TipoTempoServicoService{

	@Autowired
	private TipoTempoServicoDAO dao;
	
	
	@Override
	public TipoTempoServico getById(Long id) {
		return dao.getById(id);
	}

	@Override
	public List<TipoTempoServico> findAll() {
		return dao.findAll();
	}

}
