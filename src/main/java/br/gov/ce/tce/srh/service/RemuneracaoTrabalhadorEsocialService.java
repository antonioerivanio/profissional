package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.DemonstrativosDeValoresDAO;
import br.gov.ce.tce.srh.dao.InfoRemuneracaoPeriodoAnterioresDAO;
import br.gov.ce.tce.srh.dao.ItensRemuneracaoTrabalhadorDAO;
import br.gov.ce.tce.srh.dao.RemuneracaoTrabalhadorEsocialDAO;
import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.InfoRemuneracaoPeriodoAnteriores;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.domain.RemuneracaoTrabalhador;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("remuneracaoTrabalhadorESocialService")
public class RemuneracaoTrabalhadorEsocialService{

	@Autowired
	private RemuneracaoTrabalhadorEsocialDAO dao;
	
	@Autowired
	private DemonstrativosDeValoresDAO demonstrativosDeValoresDAO;
	
	@Autowired
	private InfoRemuneracaoPeriodoAnterioresDAO infoRemuneracaoPeriodoAnterioresDAO;
	
	@Autowired
	private ItensRemuneracaoTrabalhadorDAO itensRemuneracaoTrabalhadorDAO;
		
	@Autowired
	private EventoService eventoService;

	@Autowired
	private NotificacaoService notificacaoService;
	
	@Transactional
	public void salvar(String mesReferencia, String anoReferencia, Funcional servidorFuncional) {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		List<RemuneracaoTrabalhador> remuneracaoTrabalhadorList = dao.getEventoS1200ByServidor(mesReferencia, anoReferencia, periodoApuracao, servidorFuncional);
		
		for (RemuneracaoTrabalhador remuneracaoTrabalhador : remuneracaoTrabalhadorList) {
			salvar(remuneracaoTrabalhador, mesReferencia, anoReferencia);
		}
	}

	@Transactional
	public RemuneracaoTrabalhador salvar(RemuneracaoTrabalhador entidade,String mesReferencia, String anoReferencia) {		
				
		entidade = dao.salvar(entidade);
		List<DemonstrativosDeValores> demonstrativosDeValoresList = demonstrativosDeValoresDAO.findDemonstrativosDeValores(mesReferencia, anoReferencia, entidade.getId(), entidade.getFuncional().getId());
		for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {
			demonstrativosDeValores = demonstrativosDeValoresDAO.salvar(demonstrativosDeValores);
			if(demonstrativosDeValores.getFlInfoRemunPerAnteriores().equals(1)) {
				List<InfoRemuneracaoPeriodoAnteriores> infoRemuneracaoPeriodoAnterioresList = infoRemuneracaoPeriodoAnterioresDAO.findInfoRemuneracaoPeriodoAnteriores(demonstrativosDeValores.getIdeDmDev());
			}
		}
		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S1200.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), entidade.getReferencia());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evento S1200 com pendência de envio.");
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
	public void excluir(RemuneracaoTrabalhador entidade) {
		dao.excluir(entidade);
	}	

	public RemuneracaoTrabalhador getById(Long id) {
		return dao.getById(id);
	}	
	
	public int count(String nome, String cpf) {
		return dao.count(nome, cpf);
	}
	
	public List<RemuneracaoTrabalhador> search(String nome, String cpf, Integer first, Integer rows) {
		return dao.search(nome, cpf, first, rows);
	}
	
	private String getPeriodoApuracaoStr(String mesReferencia, String anoReferencia) {
		String periodoApuracaoStr = "";
		
		if(mesReferencia.equals("13")) {
			periodoApuracaoStr = anoReferencia;
		}
		else {
			periodoApuracaoStr = anoReferencia + "-" + mesReferencia;
		}
		return periodoApuracaoStr;
	}

}
