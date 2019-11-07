package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.ProcessoESocialDAO;
import br.gov.ce.tce.srh.dao.ProcessoESocialSuspensaoDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.domain.ProcessoESocial;
import br.gov.ce.tce.srh.domain.ProcessoESocialSuspensao;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;
import br.gov.ce.tce.srh.enums.TipoProcesso;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("processoESocialService")
public class ProcessoESocialService {

	@Autowired
	private ProcessoESocialDAO dao;

	@Autowired
	private ProcessoESocialSuspensaoDAO suspensaoDao;

	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;
	
	@Autowired
	private EventoService eventoService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Transactional
	public void salvar(ProcessoESocial entidade) {
		// TODO Validações

		validaCamposObrigatorios(entidade);

		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getNumero());
		vigencia.setTipoEvento(TipoEventoESocial.S1070);
		esocialEventoVigenciaService.salvar(vigencia);

		dao.salvar(entidade);
		salvarSuspensoes(entidade);

		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S1070.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipo(evento.getId());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evendo S1070 com pendência de envio.");
			notificacao.setData(new Date());
			notificacao.setTipo(TipoNotificacao.N);
			notificacao.setEvento(evento);
			notificacao.setReferencia(entidade.getReferenciaESocial());
		} else {
			notificacao.setData(new Date());
		}

		this.notificacaoService.salvar(notificacao);

	}

	private void salvarSuspensoes(ProcessoESocial entidade) {
		List<ProcessoESocialSuspensao> suspensoes = entidade.getSuspensoes();
		if (suspensoes != null && suspensoes.size() > 0) {
			for (ProcessoESocialSuspensao suspensao : suspensoes) {
				suspensao.setProcesso(entidade.getId());
				if (suspensao.isExcluida()) {
					suspensaoDao.excluir(suspensao);
				} else {
					suspensaoDao.salvar(suspensao);
				}
			}
		}
	}

	private void validaCamposObrigatorios(ProcessoESocial entidade) {
		if (entidade.getTipoProcesso() == TipoProcesso.ADMINISTRATIVO
				&& !(entidade.getNumero().length() == 17 || entidade.getNumero().length() == 21)) {
			throw new SRHRuntimeException("Processo do tipo " + TipoProcesso.ADMINISTRATIVO.getDescricao()
					+ " deve possuir 17 (dezessete) ou 21 (vinte e um) algarismos.");
		}

		if (entidade.getTipoProcesso() == TipoProcesso.JUDICIAL && !(entidade.getNumero().length() == 20)) {
			throw new SRHRuntimeException("Processo do tipo " + TipoProcesso.JUDICIAL.getDescricao()
					+ " deve possuir 20 (vinte) algarismos.");
		}

		if (entidade.getTipoProcesso() == TipoProcesso.NB_INSS && !(entidade.getNumero().length() == 10)) {
			throw new SRHRuntimeException(
					"Processo do tipo " + TipoProcesso.NB_INSS.getDescricao() + " deve possuir 10 (vinte) algarismos.");
		}

		if (entidade.getTipoProcesso() == TipoProcesso.FAP && !(entidade.getNumero().length() == 16)) {
			throw new SRHRuntimeException("Processo do tipo " + TipoProcesso.NB_INSS.getDescricao()
					+ " deve possuir 16 (dezesseis) algarismos. Regra de validação.");
		}

	}

	@Transactional
	public void salvarSuspensao(ProcessoESocialSuspensao suspensao) {
		validaSuspensao(suspensao);
		suspensaoDao.salvar(suspensao);
	}

	@Transactional
	public void excluir(ProcessoESocial entidade) {
		dao.excluir(entidade);
	}

	public ProcessoESocial getById(Long id) {
		return dao.getById(id);
	}

	public List<ProcessoESocial> search(String numero, Integer first, Integer rows) {
		return dao.search(numero, first, rows);
	}

	public List<ProcessoESocialSuspensao> getSuspensoes(Long idProcesso) {
		return suspensaoDao.getSuspensoes(idProcesso);
	}

	public void validaSuspensao(ProcessoESocialSuspensao suspensao) {
		if (!suspensao.isValido()) {
			throw new SRHRuntimeException("Todos os campos de suspensão de exigibilidade são obrigatório.");
		}
	}

}
