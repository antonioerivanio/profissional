package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AfastamentoESocialDAO;
import br.gov.ce.tce.srh.domain.AfastamentoESocial;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Licenca;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("afastamentoESocialService")
public class AfastamentoESocialService {
	@Autowired
	private AfastamentoESocialDAO afastamentoESocialDAO;
	
	@Autowired
	private EventoService eventoService;
	
	@Autowired
	private NotificacaoService notificacaoService;
	
	@Autowired
	private LicencaService licencaService;
	
	public int count(String nome, String cpf) {
		return afastamentoESocialDAO.count(nome, cpf);
	}
	
	@Transactional
	public void excluir(AfastamentoESocial entidade) {
		afastamentoESocialDAO.excluir(entidade);
	}
	

	public AfastamentoESocial getById(Long id) {
		return afastamentoESocialDAO.getById(id);
	}	
	
	public List<AfastamentoESocial> search(String nome, String cpf, int first, int rows){
		return afastamentoESocialDAO.search(nome, cpf, first, rows);
	}
	
	public AfastamentoESocial salvar(AfastamentoESocial entidade) {
		entidade = afastamentoESocialDAO.salvar(entidade);
		
		//salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S2230.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), entidade.getReferencia());
		if(notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evento S2230 com pendência de envio.");
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
	
	public AfastamentoESocial getEvento2230ByServidor(Funcional servidorFuncional,Licenca licenca) {
		return afastamentoESocialDAO.getEvento2230ByServidor(servidorFuncional, licenca);
	}
	
	/**
	 * @author erivanio.cruz
	 * @since 27/04/2022
	 * @param servidorFuncional
	 * @param possuiCargo
	 * @return Set<AfastamentoESocial>
	 */
	public List<Licenca> getLicencaList(Funcional servidorFuncional, List<Integer> listaCondigo) {
		return licencaService.search(servidorFuncional, listaCondigo);
	}
}


