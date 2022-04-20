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

	@Autowired
	private RubricaService rubricaService;

	@Autowired
	private RubricaESocialTabelaService rubricaESocialTabelaService;


	@Transactional
	public RubricaESocialTCE salvar(RubricaESocialTCE entidade) {		
		
		validaCamposObrigatorios(entidade);
		
		String codigoRubrica = rubricaService.findById(entidade.getRubrica().getId()).getCodigo();
		String codigoTabela = rubricaESocialTabelaService.getById(entidade.getTabela().getId()).getCodigo();

		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(codigoRubrica + "-" + codigoTabela);
		vigencia.setTipoEvento(TipoEventoESocial.S1010);
		esocialEventoVigenciaService.salvar(vigencia);
		
		entidade = dao.salvar(entidade);

		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S1010.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), entidade.getReferenciaESocial());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evento S1010 com pendência de envio.");
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
		if (entidade.getIncideTeto() && Integer.parseInt(entidade.getRubrica().getCodigo()) > 599 ) {
			throw new SRHRuntimeException("Para código de rubrica maior que 599 não incide no teto.");
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
