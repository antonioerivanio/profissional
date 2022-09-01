package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.BeneficioEsocialDAO;
import br.gov.ce.tce.srh.dao.FechamentoEventoEsocialDAO;
import br.gov.ce.tce.srh.domain.Beneficio;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.FechamentoEventoEsocial;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("beneficioEsocialService")
public class FechamentoEventoEsocialService{

	@Autowired
	private FechamentoEventoEsocialDAO dao;
		
	@Autowired
	private EventoService eventoService;

	@Autowired
	private NotificacaoService notificacaoService;



	@Transactional
	public FechamentoEventoEsocial salvar(FechamentoEventoEsocial entidade) {		
				
		entidade = dao.salvar(entidade);

		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S1299.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), entidade.getReferencia());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evento S1299 com pendência de envio.");
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

	@Transactional
	public void excluir(FechamentoEventoEsocial entidade) {
		dao.excluir(entidade);
	}	

	public FechamentoEventoEsocial getById(Long id) {
		return dao.getById(id);
	}	
	
	public int count(String nome, String cpf) {
		return dao.count(nome, cpf);
	}
	
	public List<FechamentoEventoEsocial> search(String nome, String cpf, Integer first, Integer rows) {
		return dao.search(nome, cpf, first, rows);
	}

	public FechamentoEventoEsocial getEventoS1299ByServidor(Funcional servidorFuncional) {
		FechamentoEventoEsocial fechamentoEventoEsocial = dao.getEventoS1299ByServidor(servidorFuncional);
		return fechamentoEventoEsocial;
	}


}
