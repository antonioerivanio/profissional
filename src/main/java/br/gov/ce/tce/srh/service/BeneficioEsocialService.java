package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.BeneficioEsocialDAO;
import br.gov.ce.tce.srh.domain.Beneficio;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("beneficioEsocialService")
public class BeneficioEsocialService{

	@Autowired
	private BeneficioEsocialDAO dao;
		
	@Autowired
	private EventoService eventoService;

	@Autowired
	private NotificacaoService notificacaoService;



	@Transactional
	public Beneficio salvar(Beneficio entidade) {		
				
		entidade = dao.salvar(entidade);

		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S2410.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), entidade.getReferencia());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evento S2410 com pendência de envio.");
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
	public void excluir(Beneficio entidade) {
		dao.excluir(entidade);
	}	

	public Beneficio getById(Long id) {
		return dao.getById(id);
	}	
	
	public int count(String nome, String cpf) {
		return dao.count(nome, cpf);
	}
	
	public List<Beneficio> search(String nome, String cpf, Integer first, Integer rows) {
		return dao.search(nome, cpf, first, rows);
	}

	public Beneficio getEventoS2410ByServidor(Funcional servidorFuncional) {
		Beneficio beneficio = dao.getEventoS2410ByServidor(servidorFuncional);
		//beneficio.setNrBeneficio(getNumeroBenecio(beneficio));
		return beneficio;
	}

	private String getNumeroBenecio(Beneficio beneficio) {
		StringBuffer numero = new StringBuffer();		
		numero.append("TCE");
		numero.append("0"+dao.getMaxId());
		numero.append(beneficio.getTpBeneficio());
		numero.append(beneficio.getCpfBenef());
		return numero.toString();
	}

}
