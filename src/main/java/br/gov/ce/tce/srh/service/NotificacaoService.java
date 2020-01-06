package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.NotificacaoDAO;
import br.gov.ce.tce.srh.domain.Notificacao;

/**
 * 
 * @author esmayk.alves@tce.ce.gov.br
 *
 */
@Service("notificacaoService")
public class NotificacaoService {

	@Autowired
	private NotificacaoDAO dao;
	
	@Transactional
	public Notificacao salvar(Notificacao entidade) {
		return dao.salvar(entidade);
	}
	
	public Notificacao getById(Long id) {
		return dao.getById(id);
	}
	
	public Notificacao findByEventoIdAndTipoAndReferencia(long idEvento, String referencia) {
		return dao.findByEventoIdAndTipoAndReferencia(idEvento, referencia);
	}
	
	public List<Notificacao> findAll() {
		return dao.findAll();
	}
	
	@Transactional
	public void excluir(Notificacao entidade) {
		dao.excluir(entidade);
	}
}
