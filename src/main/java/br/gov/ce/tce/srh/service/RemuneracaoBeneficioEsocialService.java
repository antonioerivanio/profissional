package br.gov.ce.tce.srh.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RemuneracaoBeneficioEsocialDAO;
import br.gov.ce.tce.srh.domain.DemonstrativosDeValores;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Funcional;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.domain.RemuneracaoBeneficio;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;

@Service("remuneracaoBeneficioESocialService")
public class RemuneracaoBeneficioEsocialService{

	@Autowired
	private RemuneracaoBeneficioEsocialDAO dao;		
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
	
	
	public void salvar(ArrayList<RemuneracaoBeneficio> remuneracaoBeneficioList) throws CloneNotSupportedException {
		for (RemuneracaoBeneficio remuneracaoBeneficio : remuneracaoBeneficioList) {	
			salvar(remuneracaoBeneficio);
		}				
	}
	
	public void salvaNotificacaoEsocial(RemuneracaoBeneficio remuneracaoBeneficio) {
		// salvando notificação
				Evento evento = this.eventoService.getById(TipoEventoESocial.S1207.getCodigo());
				Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), remuneracaoBeneficio.getReferencia());
				if (notificacao == null) {
					notificacao = new Notificacao();
					notificacao.setDescricao("Evento S1207 com pendência de envio.");
					notificacao.setData(new Date());
					notificacao.setTipo(TipoNotificacao.N);
					notificacao.setEvento(evento);
					notificacao.setReferencia(remuneracaoBeneficio.getReferencia());
				} else {
					notificacao.setData(new Date());
				}

				this.notificacaoService.salvar(notificacao);
	}

	@Transactional
	public void excluir(RemuneracaoBeneficio entidade) {
		dao.excluir(entidade);
	}	

	public RemuneracaoBeneficio getById(Long id) {
		return dao.getById(id);
	}	
	
	public int count(String nome, String cpf,  String anoReferencia, String mesReferencia) {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		return dao.count(nome, cpf, periodoApuracao);
	}
	
	public List<RemuneracaoBeneficio> search(String nome, String cpf, String anoReferencia, String mesReferencia, Integer first, Integer rows) {
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
	
	public RemuneracaoBeneficio getEventoS1207(String mesReferencia, String anoReferencia, Funcional beneficioFuncional) throws CloneNotSupportedException {
		String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);
		RemuneracaoBeneficio remuneracaoBeneficio = dao.getEventoS1207(mesReferencia, anoReferencia, periodoApuracao, beneficioFuncional);
		RemuneracaoBeneficio remuneracaoBeneficioClonado = remuneracaoBeneficio.clone();
		remuneracaoBeneficioClonado.setId(null);
		return remuneracaoBeneficioClonado;
	}

	@Transactional
	public void salvar(RemuneracaoBeneficio remuneracaoBeneficio) {
		remuneracaoBeneficio = dao.salvar(remuneracaoBeneficio);
		salvaNotificacaoEsocial(remuneracaoBeneficio);
	}

	public ArrayList<RemuneracaoBeneficio> geraRemuneracaoBeneficioLote(String mesReferencia, String anoReferencia, boolean isEstagiario) throws CloneNotSupportedException {
		
			ArrayList<RemuneracaoBeneficio> remuneracaoBeneficioList = new ArrayList<RemuneracaoBeneficio>();
			
			String periodoApuracao = getPeriodoApuracaoStr(mesReferencia, anoReferencia);		
			List<Funcional> beneficioEnvioList = funcionalService.findBeneficioesEvento1207(anoReferencia, mesReferencia);
			
			for (Funcional beneficioFuncional : beneficioEnvioList) {
				RemuneracaoBeneficio remuneracaoBeneficio = dao.getEventoS1207(mesReferencia, anoReferencia, periodoApuracao, beneficioFuncional);
				RemuneracaoBeneficio remuneracaoBeneficioClonado = remuneracaoBeneficio.clone();
				remuneracaoBeneficioClonado.setId(null);
				
				if( remuneracaoBeneficioClonado != null) {
					if(mesReferencia.equals("13")) {
						remuneracaoBeneficioClonado.setIndApuracao(new Byte("2"));
					}
	
					List<DemonstrativosDeValores> demonstrativosDeValoresList = demonstrativosDeValoresService.findDemonstrativosDeValoresBeneficio(mesReferencia, anoReferencia, remuneracaoBeneficio, beneficioFuncional.getId());
					infoRemuneracaoPeriodoAnterioresService.findInfoRemuneracaoPeriodoAnteriores(mesReferencia, anoReferencia, demonstrativosDeValoresList, beneficioFuncional.getId(), isEstagiario);
					infoRemuneracaoPeriodoApuracaoService.findInfoRemuneracaoPeriodoApuracao(mesReferencia, anoReferencia, demonstrativosDeValoresList, beneficioFuncional.getId(), isEstagiario);
					itensRemuneracaoTrabalhadorService.findByDemonstrativosDeValores(demonstrativosDeValoresList);
					
					remuneracaoBeneficio.setDmDev(demonstrativosDeValoresList);
					remuneracaoBeneficioClonado.setDmDev(demonstrativosDeValoresList);				
					remuneracaoBeneficioList.add(remuneracaoBeneficioClonado);
				}			
				
			}
			return remuneracaoBeneficioList;
	}

}
