package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.DemonstrativosDeValoresDAO;
import br.gov.ce.tce.srh.dao.InfoRemuneracaoPeriodoAnterioresDAO;
import br.gov.ce.tce.srh.dao.InfoRemuneracaoPeriodoApuracaoDAO;
import br.gov.ce.tce.srh.dao.ItensRemuneracaoTrabalhadorDAO;
import br.gov.ce.tce.srh.dao.RemuneracaoOutraEmpresaDAO;
import br.gov.ce.tce.srh.dao.RemuneracaoTrabalhadorEsocialDAO;
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
import br.gov.ce.tce.srh.util.SRHUtils;

@Service("remuneracaoTrabalhadorESocialService")
public class RemuneracaoTrabalhadorEsocialService{

	@Autowired
	private RemuneracaoTrabalhadorEsocialDAO dao;
	
	@Autowired
	private DemonstrativosDeValoresDAO demonstrativosDeValoresDAO;
	
	@Autowired
	private InfoRemuneracaoPeriodoAnterioresDAO infoRemuneracaoPeriodoAnterioresDAO;
	
	@Autowired
	private InfoRemuneracaoPeriodoApuracaoDAO infoRemuneracaoPeriodoApuracaoDAO;
	
	@Autowired
	private ItensRemuneracaoTrabalhadorDAO itensRemuneracaoTrabalhadorDAO;
	
	@Autowired
	private RemuneracaoOutraEmpresaDAO remuneracaoOutraEmpresaDAO;
		
	@Autowired
	private EventoService eventoService;

	@Autowired
	private NotificacaoService notificacaoService;
	
	@Transactional
	public void salvar(String mesReferencia, String anoReferencia, Funcional servidorFuncional) throws CloneNotSupportedException {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		List<RemuneracaoTrabalhador> remuneracaoTrabalhadorList = dao.getEventoS1200(mesReferencia, anoReferencia, periodoApuracao, servidorFuncional);
		RemuneracaoTrabalhador remuneracaoTrabalhadorClonado = null;
		for (RemuneracaoTrabalhador remuneracaoTrabalhador : remuneracaoTrabalhadorList) {
			if (remuneracaoTrabalhador.getId() < 0) {
				 remuneracaoTrabalhadorClonado = remuneracaoTrabalhador.clone();
			}
			salvar(remuneracaoTrabalhadorClonado, mesReferencia, anoReferencia);
		}
	}

	@Transactional
	public RemuneracaoTrabalhador salvar(RemuneracaoTrabalhador remuneracaoTrabalhador, String mesReferencia, String anoReferencia) throws CloneNotSupportedException {		
				
		remuneracaoTrabalhador = dao.salvar(remuneracaoTrabalhador);
		
		List<RemuneracaoOutraEmpresa> remuneracaoOutraEmpresaList = remuneracaoOutraEmpresaDAO.findRemuneracaoOutraEmpresa(mesReferencia, anoReferencia, remuneracaoTrabalhador.getId(), remuneracaoTrabalhador.getFuncional().getId());
		
		for (RemuneracaoOutraEmpresa remuneracaoOutraEmpresa : remuneracaoOutraEmpresaList) {
			RemuneracaoOutraEmpresa remuneracaoOutraEmpresaClonado = remuneracaoOutraEmpresa.clone();
			remuneracaoOutraEmpresaClonado = remuneracaoOutraEmpresaDAO.salvar(remuneracaoOutraEmpresaClonado);
		}
		
		List<DemonstrativosDeValores> demonstrativosDeValoresList = demonstrativosDeValoresDAO.findDemonstrativosDeValores(mesReferencia, anoReferencia, remuneracaoTrabalhador.getId(), remuneracaoTrabalhador.getFuncional().getId());
		for (DemonstrativosDeValores demonstrativosDeValores : demonstrativosDeValoresList) {
			DemonstrativosDeValores demonstrativosDeValoresClonado = demonstrativosDeValores.clone();
			demonstrativosDeValoresClonado = demonstrativosDeValoresDAO.salvar(demonstrativosDeValoresClonado);
			if(demonstrativosDeValoresClonado.getFlInfoRemunPerAnteriores().equals(0)) {
				List<InfoRemuneracaoPeriodoApuracao> infoRemuneracaoPeriodoApuracaoList = infoRemuneracaoPeriodoApuracaoDAO.findInfoRemuneracaoPeriodoApuracao(mesReferencia, anoReferencia, demonstrativosDeValoresClonado, remuneracaoTrabalhador.getFuncional().getId());
				
				for (InfoRemuneracaoPeriodoApuracao infoRemuneracaoPeriodoApuracao : infoRemuneracaoPeriodoApuracaoList) {
					InfoRemuneracaoPeriodoApuracao infoRemuneracaoPeriodoApuracaoClonado = infoRemuneracaoPeriodoApuracao.clone();
					infoRemuneracaoPeriodoApuracaoClonado = infoRemuneracaoPeriodoApuracaoDAO.salvar(infoRemuneracaoPeriodoApuracaoClonado);
					List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList = itensRemuneracaoTrabalhadorDAO.findByDemonstrativosDeValores(demonstrativosDeValoresClonado,infoRemuneracaoPeriodoApuracaoClonado.getMatricula());
					if(itensRemuneracaoTrabalhadorList != null && !itensRemuneracaoTrabalhadorList.isEmpty()) {
						for (ItensRemuneracaoTrabalhador itensRemuneracaoTrabalhador : itensRemuneracaoTrabalhadorList) {
							ItensRemuneracaoTrabalhador itensRemuneracaoTrabalhadorClonado = itensRemuneracaoTrabalhador.clone();
							itensRemuneracaoTrabalhadorClonado.setInfoRemuneracaoPeriodoApuracao(infoRemuneracaoPeriodoApuracao);
							//itensRemuneracaoTrabalhadorClonado.setDemonstrativosDeValores(demonstrativosDeValoresClonado);
							System.out.println(itensRemuneracaoTrabalhadorClonado.getVlrRubr().toString());
							itensRemuneracaoTrabalhadorClonado.setVlrRubr(SRHUtils.valorMonetarioStringParaBigDecimal2(itensRemuneracaoTrabalhadorClonado.getVlrRubr().toString()));
							System.out.println(itensRemuneracaoTrabalhadorClonado.getVlrRubr());
							itensRemuneracaoTrabalhadorDAO.salvar(itensRemuneracaoTrabalhadorClonado);
						}			
					}
				}
				
			}
			
			else if(demonstrativosDeValoresClonado.getFlInfoRemunPerAnteriores().equals(1)) {
				List<InfoRemuneracaoPeriodoAnteriores> infoRemuneracaoPeriodoAnterioresList = infoRemuneracaoPeriodoAnterioresDAO.findInfoRemuneracaoPeriodoAnteriores(mesReferencia, anoReferencia, demonstrativosDeValoresClonado, remuneracaoTrabalhador.getFuncional().getId());
				
				for (InfoRemuneracaoPeriodoAnteriores infoRemuneracaoPeriodoAnteriores : infoRemuneracaoPeriodoAnterioresList) {
					InfoRemuneracaoPeriodoAnteriores infoRemuneracaoPeriodoAnterioresClonado = infoRemuneracaoPeriodoAnteriores.clone();
					infoRemuneracaoPeriodoAnterioresClonado = infoRemuneracaoPeriodoAnterioresDAO.salvar(infoRemuneracaoPeriodoAnterioresClonado);
					List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList = itensRemuneracaoTrabalhadorDAO.findByDemonstrativosDeValores(demonstrativosDeValoresClonado, infoRemuneracaoPeriodoAnterioresClonado.getMatricula());
					if(itensRemuneracaoTrabalhadorList != null && !itensRemuneracaoTrabalhadorList.isEmpty()) {
						for (ItensRemuneracaoTrabalhador itensRemuneracaoTrabalhador : itensRemuneracaoTrabalhadorList) {
							ItensRemuneracaoTrabalhador itensRemuneracaoTrabalhadorClonado = itensRemuneracaoTrabalhador.clone();
							itensRemuneracaoTrabalhadorClonado.setInfoRemuneracaoPeriodoAnteriores(infoRemuneracaoPeriodoAnterioresClonado);
							//itensRemuneracaoTrabalhadorClonado.setDemonstrativosDeValores(demonstrativosDeValoresClonado);
							System.out.println(itensRemuneracaoTrabalhadorClonado.getVlrRubr().toString());
							itensRemuneracaoTrabalhadorClonado.setVlrRubr(SRHUtils.valorMonetarioStringParaBigDecimal2(itensRemuneracaoTrabalhadorClonado.getVlrRubr().toString()));
							System.out.println(itensRemuneracaoTrabalhadorClonado.getVlrRubr());
							itensRemuneracaoTrabalhadorDAO.salvar(itensRemuneracaoTrabalhadorClonado);
						}			
					}
				}
			}			
			/*else {
				List<ItensRemuneracaoTrabalhador> itensRemuneracaoTrabalhadorList = itensRemuneracaoTrabalhadorDAO.findByDemonstrativosDeValores(demonstrativosDeValoresClonado);
				if(itensRemuneracaoTrabalhadorList != null && !itensRemuneracaoTrabalhadorList.isEmpty()) {
					for (ItensRemuneracaoTrabalhador itensRemuneracaoTrabalhador : itensRemuneracaoTrabalhadorList) {									
						ItensRemuneracaoTrabalhador itensRemuneracaoTrabalhadorClonado = itensRemuneracaoTrabalhador.clone();
						itensRemuneracaoTrabalhadorClonado.setDemonstrativosDeValores(demonstrativosDeValoresClonado);
						System.out.println(itensRemuneracaoTrabalhadorClonado.getVlrRubr().toString());
						itensRemuneracaoTrabalhadorClonado.setVlrRubr(SRHUtils.valorMonetarioStringParaBigDecimal2(itensRemuneracaoTrabalhadorClonado.getVlrRubr().toString()));
						itensRemuneracaoTrabalhadorDAO.salvar(itensRemuneracaoTrabalhadorClonado);
					}			
				}
			}*/
		}
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

		return remuneracaoTrabalhador;
	}		

	@Transactional
	public void excluir(RemuneracaoTrabalhador entidade) {
		dao.excluir(entidade);
	}	

	public RemuneracaoTrabalhador getById(Long id) {
		return dao.getById(id);
	}	
	
	public int count(String nome, String cpf,  String anoReferencia, String mesReferencia) {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		return dao.count(nome, cpf, periodoApuracao);
	}
	
	public List<RemuneracaoTrabalhador> search(String nome, String cpf, String anoReferencia, String mesReferencia, Integer first, Integer rows) {
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

	public RemuneracaoTrabalhador getEventoS1200(String mesReferencia, String anoReferencia,
			Funcional servidorFuncional) {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		List<RemuneracaoTrabalhador> remuneracaoTrabalhadorList = dao.getEventoS1200(mesReferencia, anoReferencia, periodoApuracao, servidorFuncional);
		if(remuneracaoTrabalhadorList != null && !remuneracaoTrabalhadorList.isEmpty()) {	
			RemuneracaoTrabalhador remuneracaoTrabalhador = remuneracaoTrabalhadorList.get(0);;
			if (remuneracaoTrabalhador.getId() < 0) {
				remuneracaoTrabalhador.setId(null);
			}
			return remuneracaoTrabalhador;
		}
		else {
			return null;
		}
	}

}
