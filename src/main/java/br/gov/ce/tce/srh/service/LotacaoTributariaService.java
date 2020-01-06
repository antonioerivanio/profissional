package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.LotacaoTributariaDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.LotacaoTributaria;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoLotacaoTributaria;
import br.gov.ce.tce.srh.enums.TipoNotificacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("lotacaoTributariaService")
public class LotacaoTributariaService {

	@Autowired
	private LotacaoTributariaDAO dao;

	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;

	@Autowired
	private EventoService eventoService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Transactional
	public LotacaoTributaria salvar(LotacaoTributaria entidade) {

		// TODO Validações

		validaCamposObrigatorios(entidade);

		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCodigo());
		vigencia.setTipoEvento(TipoEventoESocial.S1020);
		esocialEventoVigenciaService.salvar(vigencia);

		entidade = dao.salvar(entidade);

		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S1020.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), entidade.getReferenciaESocial());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evento S1020 com pendência de envio.");
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

	private void validaCamposObrigatorios(LotacaoTributaria entidade) {
		if (entidade.getCodigo().toUpperCase().indexOf("ESOCIAL") == 0) {
			throw new SRHRuntimeException("O código não pode ter eSocial nos sete primeiros caracteres.");
		}
		if (entidade.getTipoInscricao() != null && entidade.getTipoLotacao() == TipoLotacaoTributaria.TLT01) {
			throw new SRHRuntimeException("O campo tipo de inscrição só é necessário quando o tipo de lotação for "
					+ TipoLotacaoTributaria.TLT04.getSimplificado());
		}

	}

	@Transactional
	public void excluir(LotacaoTributaria entidade) {
		dao.excluir(entidade);
	}

	public LotacaoTributaria findById(Long id) {
		return dao.findById(id);
	}

	public List<LotacaoTributaria> findAll() {
		return dao.findAll();
	}

}
