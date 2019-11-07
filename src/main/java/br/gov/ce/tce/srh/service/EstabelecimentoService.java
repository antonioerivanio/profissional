package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.EstabelecimentoDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.Estabelecimento;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("estabelecimentoService")
public class EstabelecimentoService {

	@Autowired
	private EstabelecimentoDAO dao;

	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;

	@Autowired
	private EventoService eventoService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Transactional
	public Estabelecimento salvar(Estabelecimento entidade) {

		validar(entidade);

		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getNumeroInscricao());
		vigencia.setTipoEvento(TipoEventoESocial.S1005);
		esocialEventoVigenciaService.salvar(vigencia);

		entidade = dao.salvar(entidade);

		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S1005.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipo(evento.getId());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evendo S1005 com pendência de envio.");
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

	private void validar(Estabelecimento entidade) {

		if (entidade.getFap() < 0.5 || entidade.getFap() > 2) {
			throw new SRHRuntimeException(
					"O FAP deve ser um número maior ou igual a 0,5000 e menor ou igual a 2,0000.");
		}

	}

	@Transactional
	public void excluir(Estabelecimento entidade) {
		dao.excluir(entidade);
	}

	public Estabelecimento findById(Long id) {
		return dao.findById(id);
	}

	public List<Estabelecimento> findAll() {
		return dao.findAll();
	}

}
