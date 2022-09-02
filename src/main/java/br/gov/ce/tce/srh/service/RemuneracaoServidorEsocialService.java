package br.gov.ce.tce.srh.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RemuneracaoServidorEsocialDAO;
import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.InfoRemuneracaoPeriodoAnteriores;
import br.gov.ce.tce.srh.domain.InfoRemuneracaoPeriodoApuracao;
import br.gov.ce.tce.srh.domain.ItensRemuneracaoTrabalhador;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.domain.RemuneracaoOutraEmpresa;
import br.gov.ce.tce.srh.domain.RemuneracaoServidor;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("remuneracaoServidorESocialService")
public class RemuneracaoServidorEsocialService{

	@Autowired
	private RemuneracaoServidorEsocialDAO dao;		
	@Autowired
	private EventoService eventoService;
	@Autowired
	private NotificacaoService notificacaoService;
	@Autowired
	private FuncionalService funcionalService;
	@Autowired
	private DemonstrativosDeValoresService demonstrativosDeValoresService;	
	@Autowired
	private InfoRemuneracaoPeriodoAnterioresService infoRemuneracaoPeriodoAnterioresService;
	@Autowired
	private InfoRemuneracaoPeriodoApuracaoService infoRemuneracaoPeriodoApuracaoService;
	@Autowired
	private ItensRemuneracaoTrabalhadorService itensRemuneracaoTrabalhadorService;	
	@Autowired
	private RemuneracaoOutraEmpresaService remuneracaoOutraEmpresaService;
	
	@Transactional
	public void salvar(String mesReferencia, String anoReferencia) throws CloneNotSupportedException {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);		
		List<Funcional> servidorEnvioList = funcionalService.findServidoresEvento1200(anoReferencia, mesReferencia);
		
		for (Funcional servidorFuncional : servidorEnvioList) {
			RemuneracaoServidor remuneracaoServidor = dao.getEventoS1202(mesReferencia, anoReferencia, periodoApuracao, servidorFuncional );
			
			if( remuneracaoServidor != null) {
				if(mesReferencia.equals("13")) {
					remuneracaoServidor.setIndApuracao(new Byte("2"));
				}
				
				List<DemonstrativosDeValores> demonstrativosDeValoresList = demonstrativosDeValoresService.findDemonstrativosDeValoresServidor(mesReferencia, anoReferencia, remuneracaoServidor, servidorFuncional.getId());
				List<InfoRemuneracaoPeriodoAnteriores> infoRemuneracaoPeriodoAnterioresList = infoRemuneracaoPeriodoAnterioresService.findInfoRemuneracaoPeriodoAnteriores(mesReferencia, anoReferencia, demonstrativosDeValoresList, servidorFuncional.getId());
				List<InfoRemuneracaoPeriodoApuracao> infoRemuneracaoPeriodoApuracaoList = infoRemuneracaoPeriodoApuracaoService.findInfoRemuneracaoPeriodoApuracao(mesReferencia, anoReferencia, demonstrativosDeValoresList, servidorFuncional.getId());
				List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList = itensRemuneracaoTrabalhadorService.findByDemonstrativosDeValores(demonstrativosDeValoresList);
				
				remuneracaoServidor.setDmDev(demonstrativosDeValoresList);
				
				salvar(remuneracaoServidor);
			}			
			
		}
		
	}

	private Byte getIndMVRemunOutrEmpr(List<RemuneracaoOutraEmpresa> remunOutrEmpr) {
		BigDecimal totalValoresOutrEmpr = BigDecimal.ZERO;
		for (RemuneracaoOutraEmpresa remuneracaoOutraEmpresa : remunOutrEmpr) {	
			totalValoresOutrEmpr = totalValoresOutrEmpr.add(remuneracaoOutraEmpresa.getVlrRemunOE());
		}
		
		if(totalValoresOutrEmpr.compareTo(new BigDecimal(10)) > 0) {
			return new Byte("3");
		}
		else {
			return new Byte("2");
		}
		
	}
	
	public void salvaNotificacaoEsocial(RemuneracaoServidor remuneracaoServidor) {
		// salvando notificação
				Evento evento = this.eventoService.getById(TipoEventoESocial.S1202.getCodigo());
				Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), remuneracaoServidor.getReferencia());
				if (notificacao == null) {
					notificacao = new Notificacao();
					notificacao.setDescricao("Evento S1200 com pendência de envio.");
					notificacao.setData(new Date());
					notificacao.setTipo(TipoNotificacao.N);
					notificacao.setEvento(evento);
					notificacao.setReferencia(remuneracaoServidor.getReferencia());
				} else {
					notificacao.setData(new Date());
				}

				this.notificacaoService.salvar(notificacao);
	}

	@Transactional
	public void excluir(RemuneracaoServidor entidade) {
		dao.excluir(entidade);
	}	

	public RemuneracaoServidor getById(Long id) {
		return dao.getById(id);
	}	
	
	public int count(String nome, String cpf,  String anoReferencia, String mesReferencia, boolean isRGPA) {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		return dao.count(nome, cpf, periodoApuracao, isRGPA);
	}
	
	public List<RemuneracaoServidor> search(String nome, String cpf, String anoReferencia, String mesReferencia, boolean isRGPA, Integer first, Integer rows) {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		return dao.search(nome, cpf, periodoApuracao, isRGPA, first, rows);
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
	
	public RemuneracaoServidor getEventoS1202(String mesReferencia, String anoReferencia, Funcional servidorFuncional) throws CloneNotSupportedException {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		RemuneracaoServidor remuneracaoServidor = dao.getEventoS1202(mesReferencia, anoReferencia, periodoApuracao, servidorFuncional);
		RemuneracaoServidor remuneracaoServidorClonado = remuneracaoServidor.clone();
		remuneracaoServidorClonado.setId(null);
		return remuneracaoServidorClonado;
	}

	@Transactional
	public void salvar(RemuneracaoServidor remuneracaoServidor) {
		remuneracaoServidor = dao.salvar(remuneracaoServidor);
		salvaNotificacaoEsocial(remuneracaoServidor);
	}

}
