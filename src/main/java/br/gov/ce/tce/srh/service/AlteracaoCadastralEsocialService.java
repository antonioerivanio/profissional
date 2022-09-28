package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AlteracaoCadastralEsocialDAO;
import br.gov.ce.tce.srh.domain.AlteracaoCadastral;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("alteracaoCadastralEsocialService")
public class AlteracaoCadastralEsocialService{

	@Autowired
	private AlteracaoCadastralEsocialDAO dao;
		
	@Autowired
	private EventoService eventoService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Transactional
	public AlteracaoCadastral salvar(AlteracaoCadastral entidade) {		
		
		entidade = dao.salvar(entidade);

		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S2205.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), entidade.getReferencia());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evento S2205 com pendência de envio.");
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
	public void excluir(AlteracaoCadastral entidade) {
		dao.excluir(entidade);
	}	

	public AlteracaoCadastral getByIdFuncional(Long idFuncional) {
		return dao.getByIdFuncional(idFuncional);
	}	
	
	public AlteracaoCadastral getById(Long id) {
		return dao.getById(id);
	}	
	
	public int count(String nome, String cpf) {
		return dao.count(nome, cpf);
	}
	
	public List<AlteracaoCadastral> search(String nome, String cpf, Integer first, Integer rows) {
		return dao.search(nome, cpf, first, rows);
	}

	public AlteracaoCadastral getEventoS2205ByServidor(Funcional servidorFuncional, boolean possuiCargo) {	
		return dao.getEventoS2205ByServidor(servidorFuncional, possuiCargo);
	}

	public String findReciboEventoS2205(String referencia) {
		return dao.findReciboEventoS2205(referencia);
	}

}