package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.EstagiarioESocialDAO;
import br.gov.ce.tce.srh.dao.EstatutarioESocialDAO;
import br.gov.ce.tce.srh.domain.EstagiarioESocial;
import br.gov.ce.tce.srh.domain.EstatutarioESocial;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("estatutarioESocialService")
public class EstatutarioESocialService {

	@Autowired
	private EstatutarioESocialDAO estatutarioESocialDAO;

	@Autowired
	private EventoService eventoService;
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	public int count(String nome, String cpf) {		
		return estatutarioESocialDAO.count(nome, cpf);
	}

	@Transactional
	public void excluir(EstatutarioESocial entidade) {
		estatutarioESocialDAO.excluir(entidade);
		
	}

	public List<EstatutarioESocial> search(String nome, String cpf, int first, int rows) {
		return estatutarioESocialDAO.search(nome, cpf, first, rows);
	}

	public EstatutarioESocial salvar(EstatutarioESocial entidade) {
		
		entidade = estatutarioESocialDAO.salvar(entidade);

		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S2300.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), entidade.getReferencia());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evento S2300 com pendência de envio.");
			notificacao.setData(new Date());
			notificacao.setTipo(TipoNotificacao.N);
			notificacao.setEvento(evento);
			notificacao.setReferencia(entidade.getReferencia());
		} else {
			notificacao.setData(new Date());
		}

		this.notificacaoService.salvar(notificacao);

		return entidade;
		
	}

	public EstatutarioESocial  getEventoS2300ByEstatutario(Funcional estatutarioESocial) {
		return estatutarioESocialDAO.getEventoS2300ByEstatutario(estatutarioESocial);
	}
	
}
