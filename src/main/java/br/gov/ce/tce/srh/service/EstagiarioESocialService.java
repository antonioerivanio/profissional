package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.EstagiarioESocialDAO;
import br.gov.ce.tce.srh.domain.EstagiarioESocial;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("estagiarioESocialService")
public class EstagiarioESocialService {

	@Autowired
	private EstagiarioESocialDAO estagiarioESocialDAO;

	@Autowired
	private EventoService eventoService;
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	public int count(String nome, String cpf) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Transactional
	public void excluir(EstagiarioESocial entidade) {
		estagiarioESocialDAO.excluir(entidade);
		
	}

	public List<EstagiarioESocial> search(String nome, String cpf, int first, int rows) {
		// TODO Auto-generated method stub
		return null;
	}

	public EstagiarioESocial salvar(EstagiarioESocial entidade) {
		
		
		entidade = estagiarioESocialDAO.salvar(entidade);

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

	public EstagiarioESocial getEventoS2300ByEstagiario(Funcional estagiarioFuncional) {
		return estagiarioESocialDAO.getEventoS2300ByEstagiario(estagiarioFuncional);
	}
	
}
