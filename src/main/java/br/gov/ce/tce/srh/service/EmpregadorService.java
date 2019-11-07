package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.EmpregadorDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.Empregador;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("empregadorService")
public class EmpregadorService {

	@Autowired
	private EmpregadorDAO dao;

	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;
	
	@Autowired
	private EventoService eventoService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Transactional
	public Empregador salvar(Empregador entidade) {

		// TODO Validações

		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCnpj());
		vigencia.setTipoEvento(TipoEventoESocial.S1000);
		esocialEventoVigenciaService.salvar(vigencia);

		entidade = dao.salvar(entidade);

		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S1000.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipo(evento.getId());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evendo S1000 com pendência de envio.");
			notificacao.setData(new Date());
			notificacao.setTipo(TipoNotificacao.N);
			notificacao.setEvento(evento);
			notificacao.setReferencia(entidade.getReferenciaESocial());
		} else {
			notificacao.setData(new Date());
		}

		this.notificacaoService.salvar(notificacao);

		return entidade;
	}

	@Transactional
	public void excluir(Empregador entidade) {
		dao.excluir(entidade);
	}

	public Empregador findById(Long id) {
		return dao.findById(id);
	}

	public List<Empregador> findAll() {
		return dao.findAll();
	}

}
