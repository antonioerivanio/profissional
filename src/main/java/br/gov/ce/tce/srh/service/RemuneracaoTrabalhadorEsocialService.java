package br.gov.ce.tce.srh.service;

import java.math.BigDecimal;
import java.util.ArrayList;
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
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.domain.RemuneracaoOutraEmpresa;
import br.gov.ce.tce.srh.domain.RemuneracaoTrabalhador;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;
import br.gov.ce.tce.srh.util.SRHUtils;

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
	public void salvar(ArrayList<RemuneracaoTrabalhador> remuneracaoTrabalhadorList) throws CloneNotSupportedException {
		for (RemuneracaoTrabalhador remuneracaoTrabalhador : remuneracaoTrabalhadorList) {	
			salvar(remuneracaoTrabalhador);
		}							
	}

	public Byte getIndMVRemunOutrEmpr(List<RemuneracaoOutraEmpresa> remunOutrEmpr, String anoMes) {
		BigDecimal totalValoresOutrEmpr = BigDecimal.ZERO;
		for (RemuneracaoOutraEmpresa remuneracaoOutraEmpresa : remunOutrEmpr) {	
			totalValoresOutrEmpr = totalValoresOutrEmpr.add(remuneracaoOutraEmpresa.getVlrRemunOE());
		}
		
		if(totalValoresOutrEmpr.compareTo(remuneracaoOutraEmpresaService.getINSS(anoMes)) > 0) {
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
		String periodoApuracao = SRHUtils.getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		return dao.count(nome, cpf, periodoApuracao, isRGPA, isEstagiario);
	}
	
	public List<RemuneracaoTrabalhador> search(String nome, String cpf, String anoReferencia, String mesReferencia, boolean isRGPA, boolean isEstagiario, Integer first, Integer rows) {
		String periodoApuracao = SRHUtils.getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		return dao.search(nome, cpf, periodoApuracao, isRGPA, isEstagiario, first, rows);
	}
	
	
	
	public RemuneracaoTrabalhador getEventoS1200(String mesReferencia, String anoReferencia,
			Funcional servidorFuncional) throws CloneNotSupportedException {
		String periodoApuracao = SRHUtils.getPeriodoApuracaoStr(mesReferencia, anoReferencia);
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
		String periodoApuracao = SRHUtils.getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		RemuneracaoTrabalhador remuneracaoTrabalhador = dao.getEventoS1200RPA(mesReferencia, anoReferencia, periodoApuracao, servidorFuncional);
		RemuneracaoTrabalhador remuneracaoTrabalhadorClonado = remuneracaoTrabalhador.clone();
		remuneracaoTrabalhadorClonado.setId(null);
		return remuneracaoTrabalhadorClonado;
	}

	@Transactional
	private void salvaTrabalhadorRGPA(String mesReferencia, String anoReferencia, RemuneracaoTrabalhador remuneracaoTrabalhador, Long id) {
		remuneracaoTrabalhador.setId(null);
		dao.salvar(remuneracaoTrabalhador);
		salvaNotificacaoEsocial(remuneracaoTrabalhador);
		
	}

	public ArrayList<RemuneracaoTrabalhador> geraRemuneracaoTrabalhadorLote(String mesReferencia, String anoReferencia,
			boolean isEstagiario) throws CloneNotSupportedException {
		ArrayList<RemuneracaoTrabalhador> remuneracaoTrabalhadorList = new ArrayList<RemuneracaoTrabalhador>();
		
		String periodoApuracao = SRHUtils.getPeriodoApuracaoStr(mesReferencia, anoReferencia);		
		List<Funcional> servidorEnvioList = funcionalService.findServidoresEvento1200(anoReferencia, mesReferencia);
		
		List<Funcional> servidorEnvioListTeste = new ArrayList<Funcional>();
		
		for (int i = 0; i < 5; i++) {
			servidorEnvioListTeste.add(servidorEnvioList.get(i));
		}
		
		for (Funcional servidorFuncional : servidorEnvioListTeste) {
			RemuneracaoTrabalhador remuneracaoTrabalhador = dao.getEventoS1200(mesReferencia, anoReferencia, periodoApuracao, servidorFuncional );
			RemuneracaoTrabalhador remuneracaoTrabalhadorClonado = remuneracaoTrabalhador.clone();
			remuneracaoTrabalhadorClonado.setId(null);
			
			if( remuneracaoTrabalhadorClonado != null) {
				if(mesReferencia.equals("13")) {
					remuneracaoTrabalhadorClonado.setIndApuracao(new Byte("2"));
				}
				/*if(remuneracaoTrabalhador.getRemunOutrEmpr() != null && remuneracaoTrabalhador.getRemunOutrEmpr().isEmpty()) {
					remuneracaoTrabalhador.setIndMV(getIndMVRemunOutrEmpr(remuneracaoTrabalhador.getRemunOutrEmpr()));
					
				}*/
				List<RemuneracaoOutraEmpresa> remuneracaoOutraEmpresaList = remuneracaoOutraEmpresaService.findRemuneracaoOutraEmpresa(mesReferencia, anoReferencia, remuneracaoTrabalhadorClonado, servidorFuncional.getId());
				List<DemonstrativosDeValores> demonstrativosDeValoresList = demonstrativosDeValoresService.findDemonstrativosDeValores(mesReferencia, anoReferencia, remuneracaoTrabalhadorClonado, servidorFuncional.getId());
				infoRemuneracaoPeriodoAnterioresService.findInfoRemuneracaoPeriodoAnteriores(mesReferencia, anoReferencia, demonstrativosDeValoresList, servidorFuncional.getId(), isEstagiario);
				infoRemuneracaoPeriodoApuracaoService.findInfoRemuneracaoPeriodoApuracao(mesReferencia, anoReferencia, demonstrativosDeValoresList, servidorFuncional.getId(), isEstagiario);
				itensRemuneracaoTrabalhadorService.findByDemonstrativosDeValores(demonstrativosDeValoresList);
				
				remuneracaoTrabalhadorClonado.setDmDev(demonstrativosDeValoresList);
				remuneracaoTrabalhadorClonado.setRemunOutrEmpr(remuneracaoOutraEmpresaList);
				remuneracaoTrabalhadorList.add(remuneracaoTrabalhadorClonado);
			}			
			
		}
		return remuneracaoTrabalhadorList;
	}

	public ArrayList<RemuneracaoTrabalhador> geraRemuneracaoTrabalhadorRPGALote(String mesReferencia, String anoReferencia) throws CloneNotSupportedException {
		
		ArrayList<RemuneracaoTrabalhador> remuneracaoTrabalhadorList = new ArrayList<RemuneracaoTrabalhador>();
		String periodoApuracao = SRHUtils.getPeriodoApuracaoStr(mesReferencia, anoReferencia);		
		List<CadastroPrestador> servidorEnvioList = funcionalService.findRGPAEvento1200(anoReferencia, mesReferencia);
		
		for (CadastroPrestador servidorFuncional : servidorEnvioList) {
			RemuneracaoTrabalhador remuneracaoTrabalhador = dao.getEventoS1200RPA(mesReferencia, anoReferencia, periodoApuracao, servidorFuncional );
			RemuneracaoTrabalhador remuneracaoTrabalhadorClone = remuneracaoTrabalhador.clone();
			
			if( remuneracaoTrabalhador != null) {
				if(mesReferencia.equals("13")) {
					remuneracaoTrabalhador.setIndApuracao(new Byte("2"));
				}
				if(remuneracaoTrabalhador.getRemunOutrEmpr() != null && !remuneracaoTrabalhador.getRemunOutrEmpr().isEmpty()) {
					remuneracaoTrabalhador.setIndMV(getIndMVRemunOutrEmpr(remuneracaoTrabalhador.getRemunOutrEmpr(),anoReferencia+mesReferencia));
					
				}
				
				List<RemuneracaoOutraEmpresa> remuneracaoOutraEmpresaList = remuneracaoOutraEmpresaService.findRemuneracaoOutraEmpresaRPA(mesReferencia, anoReferencia, remuneracaoTrabalhadorClone, servidorFuncional.getId());
				List<DemonstrativosDeValores> demonstrativosDeValoresList = demonstrativosDeValoresService.findDemonstrativosDeValoresRPA(mesReferencia, anoReferencia, remuneracaoTrabalhadorClone, servidorFuncional.getId());
				infoRemuneracaoPeriodoApuracaoService.findInfoRemuneracaoPeriodoApuracaoRPA( demonstrativosDeValoresList, servidorFuncional.getId());
				itensRemuneracaoTrabalhadorService.findByDemonstrativosDeValoresRPA(mesReferencia, anoReferencia, demonstrativosDeValoresList);
				
				remuneracaoTrabalhadorClone.setDmDev(demonstrativosDeValoresList);
				remuneracaoTrabalhadorClone.setRemunOutrEmpr(remuneracaoOutraEmpresaList);				
			}	
		}
		return remuneracaoTrabalhadorList;
	
	}

}
