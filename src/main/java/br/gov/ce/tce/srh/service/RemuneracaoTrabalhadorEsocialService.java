package br.gov.ce.tce.srh.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RemuneracaoTrabalhadorEsocialDAO;
import br.gov.ce.tce.srh.domain.CadastroPrestador;
import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.InfoRemuneracaoPeriodoAnteriores;
import br.gov.ce.tce.srh.domain.InfoRemuneracaoPeriodoApuracao;
import br.gov.ce.tce.srh.domain.ItensRemuneracaoTrabalhador;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.domain.RemuneracaoOutraEmpresa;
import br.gov.ce.tce.srh.domain.RemuneracaoTrabalhador;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("remuneracaoTrabalhadorESocialService")
public class RemuneracaoTrabalhadorEsocialService{

	@Autowired
	private RemuneracaoTrabalhadorEsocialDAO dao;		
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
			RemuneracaoTrabalhador remuneracaoTrabalhador = dao.getEventoS1200(mesReferencia, anoReferencia, periodoApuracao, servidorFuncional );
			
			if( remuneracaoTrabalhador != null) {
				if(mesReferencia.equals("13")) {
					remuneracaoTrabalhador.setIndApuracao(new Byte("2"));
				}
				if(remuneracaoTrabalhador.getRemunOutrEmpr() != null && remuneracaoTrabalhador.getRemunOutrEmpr().isEmpty()) {
					remuneracaoTrabalhador.setIndMV(getIndMVRemunOutrEmpr(remuneracaoTrabalhador.getRemunOutrEmpr()));
					
				}
				List<RemuneracaoOutraEmpresa> remuneracaoOutraEmpresaList = remuneracaoOutraEmpresaService.findRemuneracaoOutraEmpresa(mesReferencia, anoReferencia, remuneracaoTrabalhador, servidorFuncional.getId());
				List<DemonstrativosDeValores> demonstrativosDeValoresList = demonstrativosDeValoresService.findDemonstrativosDeValores(mesReferencia, anoReferencia, remuneracaoTrabalhador, servidorFuncional.getId());
				List<InfoRemuneracaoPeriodoAnteriores> infoRemuneracaoPeriodoAnterioresList = infoRemuneracaoPeriodoAnterioresService.findInfoRemuneracaoPeriodoAnteriores(mesReferencia, anoReferencia, demonstrativosDeValoresList, servidorFuncional.getId());
				List<InfoRemuneracaoPeriodoApuracao> infoRemuneracaoPeriodoApuracaoList = infoRemuneracaoPeriodoApuracaoService.findInfoRemuneracaoPeriodoApuracao(mesReferencia, anoReferencia, demonstrativosDeValoresList, servidorFuncional.getId());
				List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList = itensRemuneracaoTrabalhadorService.findByDemonstrativosDeValores(demonstrativosDeValoresList);
				
				remuneracaoTrabalhador.setDmDev(demonstrativosDeValoresList);
				remuneracaoTrabalhador.setRemunOutrEmpr(remuneracaoOutraEmpresaList);
				salvar(remuneracaoTrabalhador);
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
	
	public void salvaNotificacaoEsocial(RemuneracaoTrabalhador remuneracaoTrabalhador) {
		// salvando notificação
				Evento evento = this.eventoService.getById(TipoEventoESocial.S1200.getCodigo());
				Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), remuneracaoTrabalhador.getReferencia());
				if (notificacao == null) {
					notificacao = new Notificacao();
					notificacao.setDescricao("Evento S1200 com pendência de envio.");
					notificacao.setData(new Date());
					notificacao.setTipo(TipoNotificacao.N);
					notificacao.setEvento(evento);
					notificacao.setReferencia(remuneracaoTrabalhador.getReferencia());
				} else {
					notificacao.setData(new Date());
				}

				this.notificacaoService.salvar(notificacao);
	}

	@Transactional
	public void excluir(RemuneracaoTrabalhador entidade) {
		dao.excluir(entidade);
	}	

	public RemuneracaoTrabalhador getById(Long id) {
		return dao.getById(id);
	}	
	
	public int count(String nome, String cpf,  String anoReferencia, String mesReferencia, boolean isRGPA, boolean isEstagiario) {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		return dao.count(nome, cpf, periodoApuracao, isRGPA, isEstagiario);
	}
	
	public List<RemuneracaoTrabalhador> search(String nome, String cpf, String anoReferencia, String mesReferencia, boolean isRGPA, boolean isEstagiario, Integer first, Integer rows) {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		return dao.search(nome, cpf, periodoApuracao, isRGPA, isEstagiario, first, rows);
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
	
	public RemuneracaoTrabalhador getEventoS1200(String mesReferencia, String anoReferencia,
			Funcional servidorFuncional) throws CloneNotSupportedException {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		RemuneracaoTrabalhador remuneracaoTrabalhador = dao.getEventoS1200(mesReferencia, anoReferencia, periodoApuracao, servidorFuncional);
		RemuneracaoTrabalhador remuneracaoTrabalhadorClonado = remuneracaoTrabalhador.clone();
		remuneracaoTrabalhadorClonado.setId(null);
		return remuneracaoTrabalhadorClonado;
	}

	@Transactional
	public void salvar(RemuneracaoTrabalhador remuneracaoTrabalhador) {
		remuneracaoTrabalhador = dao.salvar(remuneracaoTrabalhador);
		salvaNotificacaoEsocial(remuneracaoTrabalhador);
	}

	public RemuneracaoTrabalhador getEventoS1200RPA(String mesReferencia, String anoReferencia,
			CadastroPrestador servidorFuncional) throws CloneNotSupportedException {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		RemuneracaoTrabalhador remuneracaoTrabalhador = dao.getEventoS1200RPA(mesReferencia, anoReferencia, periodoApuracao, servidorFuncional);
		RemuneracaoTrabalhador remuneracaoTrabalhadorClonado = remuneracaoTrabalhador.clone();
		remuneracaoTrabalhadorClonado.setId(null);
		return remuneracaoTrabalhadorClonado;
	}

	

	@Transactional
	public void salvarRGPA(String mesReferencia, String anoReferencia, CadastroPrestador funcionarioRGPA) throws CloneNotSupportedException {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		if(funcionarioRGPA == null) {
			List<CadastroPrestador> servidorEnvioList = funcionalService.findRGPAEvento1200(anoReferencia, mesReferencia);
			
			for (CadastroPrestador servidorFuncional : servidorEnvioList) {
				RemuneracaoTrabalhador remuneracaoTrabalhador = dao.getEventoS1200RPA(mesReferencia, anoReferencia, periodoApuracao, servidorFuncional );
				RemuneracaoTrabalhador remuneracaoTrabalhadorClone = remuneracaoTrabalhador.clone();
				salvaTrabalhadorRGPA(mesReferencia, anoReferencia, remuneracaoTrabalhadorClone, servidorFuncional.getId());
				if( remuneracaoTrabalhador != null) {
					if(mesReferencia.equals("13")) {
						remuneracaoTrabalhador.setIndApuracao(new Byte("2"));
					}
					if(remuneracaoTrabalhador.getRemunOutrEmpr() != null && remuneracaoTrabalhador.getRemunOutrEmpr().isEmpty()) {
						remuneracaoTrabalhador.setIndMV(getIndMVRemunOutrEmpr(remuneracaoTrabalhador.getRemunOutrEmpr()));
						
					}
					List<RemuneracaoOutraEmpresa> remuneracaoOutraEmpresaList = remuneracaoOutraEmpresaService.findRemuneracaoOutraEmpresa(mesReferencia, anoReferencia, remuneracaoTrabalhador, servidorFuncional.getId());
					List<DemonstrativosDeValores> demonstrativosDeValoresList = demonstrativosDeValoresService.findDemonstrativosDeValores(mesReferencia, anoReferencia, remuneracaoTrabalhador, servidorFuncional.getId());
					List<InfoRemuneracaoPeriodoAnteriores> infoRemuneracaoPeriodoAnterioresList = infoRemuneracaoPeriodoAnterioresService.findInfoRemuneracaoPeriodoAnteriores(mesReferencia, anoReferencia, demonstrativosDeValoresList, servidorFuncional.getId());
					List<InfoRemuneracaoPeriodoApuracao> infoRemuneracaoPeriodoApuracaoList = infoRemuneracaoPeriodoApuracaoService.findInfoRemuneracaoPeriodoApuracao(mesReferencia, anoReferencia, demonstrativosDeValoresList, servidorFuncional.getId());
					List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList = itensRemuneracaoTrabalhadorService.findByDemonstrativosDeValores(demonstrativosDeValoresList);
					
					remuneracaoTrabalhador.setDmDev(demonstrativosDeValoresList);
					remuneracaoTrabalhador.setRemunOutrEmpr(remuneracaoOutraEmpresaList);
					salvar(remuneracaoTrabalhador);
				}			
				
			}
		}else {
			RemuneracaoTrabalhador remuneracaoTrabalhador = dao.getEventoS1200RPA(mesReferencia, anoReferencia, periodoApuracao, funcionarioRGPA );
			RemuneracaoTrabalhador remuneracaoTrabalhadorClone = remuneracaoTrabalhador.clone();
			salvaTrabalhadorRGPA(mesReferencia, anoReferencia, remuneracaoTrabalhadorClone, funcionarioRGPA.getId());
		}
	}

	@Transactional
	private void salvaTrabalhadorRGPA(String mesReferencia, String anoReferencia, RemuneracaoTrabalhador remuneracaoTrabalhador, Long id) {
		remuneracaoTrabalhador.setId(null);
		dao.salvar(remuneracaoTrabalhador);
		salvaNotificacaoEsocial(remuneracaoTrabalhador);
		
	}

}
