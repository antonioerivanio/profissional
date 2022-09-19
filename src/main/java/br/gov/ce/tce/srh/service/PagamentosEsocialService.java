package br.gov.ce.tce.srh.service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.PagamentosEsocialDAO;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.InformacaoPagamentos;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.domain.Pagamentos;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("pagamentosESocialService")
public class PagamentosEsocialService{

	@Autowired
	private PagamentosEsocialDAO dao;		
	@Autowired
	private EventoService eventoService;
	@Autowired
	private NotificacaoService notificacaoService;
	@Autowired
	private FuncionalService funcionalService;
	@Autowired
	private InformacaoPagamentosService informacaoPagamentosService;	
	
	
	@Transactional
	public void salvar(ArrayList<Pagamentos> PagamentosList) throws CloneNotSupportedException {
		for (Pagamentos pagamentos : PagamentosList) {	
			salvar(pagamentos);
		}				
	}
	
	public void salvaNotificacaoEsocial(Pagamentos Pagamentos) {
		// salvando notificação
				Evento evento = this.eventoService.getById(TipoEventoESocial.S1210.getCodigo());
				Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), Pagamentos.getReferencia());
				if (notificacao == null) {
					notificacao = new Notificacao();
					notificacao.setDescricao("Evento S1210 com pendência de envio.");
					notificacao.setData(new Date());
					notificacao.setTipo(TipoNotificacao.N);
					notificacao.setEvento(evento);
					notificacao.setReferencia(Pagamentos.getReferencia());
				} else {
					notificacao.setData(new Date());
				}

				this.notificacaoService.salvar(notificacao);
	}

	@Transactional
	public void excluir(Pagamentos entidade) {
		dao.excluir(entidade);
	}	

	public Pagamentos getById(Long id) {
		return dao.getById(id);
	}	
	
	public int count(String nome, String cpf,  String anoReferencia, String mesReferencia) {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		return dao.count(nome, cpf, periodoApuracao);
	}
	
	public List<Pagamentos> search(String nome, String cpf, String anoReferencia, String mesReferencia, Integer first, Integer rows) {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		return dao.search(nome, cpf, periodoApuracao, first, rows);
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
	
	public Pagamentos getEventoS1210(String mesReferencia, String anoReferencia, Funcional beneficioFuncional) throws CloneNotSupportedException {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		Pagamentos pagamentos = dao.getEventoS1210(mesReferencia, anoReferencia, periodoApuracao, beneficioFuncional);
		Pagamentos pagamentosClonado = pagamentos.clone();
		pagamentosClonado.setId(null);
		return pagamentosClonado;
	}

	@Transactional
	public void salvar(Pagamentos pagamentos) {
		pagamentos = dao.salvar(pagamentos);
		salvaNotificacaoEsocial(pagamentos);
	}

	public ArrayList<Pagamentos> geraPagamentosLote(String mesReferencia, String anoReferencia) throws CloneNotSupportedException {
		
			ArrayList<Pagamentos> pagamentosList = new ArrayList<Pagamentos>();
			
			//String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);		
			List<Funcional> servidorList = funcionalService.findServidorEvento1210(anoReferencia, mesReferencia);
			
			List<Funcional> servidorEnvioListControle = new ArrayList<Funcional>();
			
			int limiteGeracao = 20;
			if(limiteGeracao > servidorList.size()) {
				limiteGeracao = servidorList.size();
			}
			
			for (int i = 0; i < limiteGeracao; i++) {
				servidorEnvioListControle.add(servidorList.get(i));
			}
			
			for (Funcional servidor : servidorEnvioListControle) {
				Pagamentos pagamentos = getEventoS1210(mesReferencia, anoReferencia, servidor);
				//Pagamentos pagamentosClonado = pagamentos.clone();
				//pagamentosClonado.setId(null);
				
				if( pagamentos != null) {
					if(mesReferencia.equals("13")) {
						pagamentos.setIndApuracao(new Byte("2"));
					}
	
					List<InformacaoPagamentos> informacaoPagamentosList = informacaoPagamentosService.findInformacaoPagamentos(mesReferencia, anoReferencia, pagamentos, servidor.getId());

					pagamentos.setPerApur(getPeriodoApuracaoPorDtPagamento(informacaoPagamentosList.get(0).getDtPgto()));
					pagamentos.setInformacaoPagamentos(informacaoPagamentosList);									
					pagamentosList.add(pagamentos);
				}			
				
			}
			return pagamentosList;
	}
	
	private String getPeriodoApuracaoPorDtPagamento(Date dtPgto) {
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM");
        String periodoApuracao = dateFormat.format(dtPgto);
		return periodoApuracao;
	}

}
