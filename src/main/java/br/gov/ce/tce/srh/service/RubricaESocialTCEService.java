package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RubricaESocialTCEDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.domain.RubricaESocialTCE;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("rubricaESocialTCEService")
public class RubricaESocialTCEService{

	@Autowired
	private RubricaESocialTCEDAO dao;
	
	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;
	
	@Autowired
	private EventoService eventoService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Transactional
	public RubricaESocialTCE salvar(RubricaESocialTCE entidade) {		
		
		validaCamposObrigatorios(entidade);
		
		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCodigo());
		vigencia.setTipoEvento(TipoEventoESocial.S1010);
		esocialEventoVigenciaService.salvar(vigencia);
		
		entidade = dao.salvar(entidade);

		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S1010.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipo(evento.getId());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evendo S1010 com pendência de envio.");
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
	
	private void validaCamposObrigatorios(RubricaESocialTCE entidade) {		
		if (entidade.getCodigo().toUpperCase().indexOf("ESOCIAL") == 0) {
			throw new SRHRuntimeException("O código não pode ter eSocial nos sete primeiros caracteres.");
		}
		
	}

	@Transactional
	public void excluir(RubricaESocialTCE entidade) {
		dao.excluir(entidade);
	}	

	public RubricaESocialTCE getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<RubricaESocialTCE> search(String descricao, Integer first, Integer rows) {
		return dao.search(descricao, first, rows);
	}

}
