package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CarreiraDAO;
import br.gov.ce.tce.srh.domain.Carreira;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("carreiraService")
public class CarreiraService {

	@Autowired
	private CarreiraDAO dao;

	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;

	@Autowired
	private EventoService eventoService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Transactional
	public Carreira salvar(Carreira entidade) {

		// TODO Validações

		validaCamposObrigatorios(entidade);

		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCodigo());
		vigencia.setTipoEvento(TipoEventoESocial.S1035);
		esocialEventoVigenciaService.salvar(vigencia);

		entidade = dao.salvar(entidade);

		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S1035.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), entidade.getReferenciaESocial());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evento S1035 com pendência de envio.");
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

	private void validaCamposObrigatorios(Carreira entidade) {
		// TODO Auto-generated method stub
		if (entidade.getCodigo().toUpperCase().indexOf("ESOCIAL") == 0) {
			throw new SRHRuntimeException("O código não pode ter eSocial nos sete primeiros caracteres.");
		}

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
