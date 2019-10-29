package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CarreiraDAO;
import br.gov.ce.tce.srh.domain.Carreira;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;

@Service("carreiraService")
public class CarreiraService{

	@Autowired
	private CarreiraDAO dao;
	
	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;

	@Transactional
	public Carreira salvar(Carreira entidade) {		
		
		// TODO Validações		
		
		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCodigo());
		vigencia.setTipoEvento(TipoEventoESocial.S1035);
		esocialEventoVigenciaService.salvar(vigencia);
		
		return dao.salvar(entidade);
	}

	@Transactional
	public void excluir(Carreira entidade) {
		dao.excluir(entidade);
	}

	public Carreira getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<Carreira> search(String descricao, Integer first, Integer rows) {
		return dao.search(descricao, first, rows);
	}

}
