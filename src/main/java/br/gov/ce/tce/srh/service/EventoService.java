package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.EventoDAO;
import br.gov.ce.tce.srh.domain.Evento;

/**
 * 
 * @author esmayk.alves@tce.ce.gov.br
 *
 */
@Service("eventoService")
public class EventoService {

	@Autowired
	private EventoDAO dao;
	
	@Transactional
	public Evento salvar(Evento entidade) {
		return dao.salvar(entidade);
	}
	
	public Evento getById(Long id) {
		return dao.getById(id);
	}
	
	public List<Evento> findAll() {
		return dao.findAll();
	}
	
	@Transactional
	public void excluir(Evento entidade) {
		dao.excluir(entidade);
	}
	
}
