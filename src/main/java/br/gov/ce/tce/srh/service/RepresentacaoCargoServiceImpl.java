package br.gov.ce.tce.srh.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RepresentacaoCargoDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.Evento;
import br.gov.ce.tce.srh.domain.Notificacao;
import br.gov.ce.tce.srh.domain.RepresentacaoCargo;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoNotificacao;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("representacaoCargoService")
public class RepresentacaoCargoServiceImpl implements RepresentacaoCargoService {

	@Autowired
	private RepresentacaoCargoDAO dao;

	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;

	@Autowired
	private EventoService eventoService;

	@Autowired
	private NotificacaoService notificacaoService;

	@Override
	@Transactional
	public void salvar(RepresentacaoCargo entidade) throws SRHRuntimeException {

//		verificandoSeEntidadeExiste(entidade);		

		// TODO Fazer as validações para o eSocial

		validaCamposObrigatorios(entidade);

		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCodFuncao());
		vigencia.setTipoEvento(TipoEventoESocial.S1040);
		esocialEventoVigenciaService.salvar(vigencia);

		dao.salvar(entidade);

		// salvando notificação
		Evento evento = this.eventoService.getById(TipoEventoESocial.S1040.getCodigo());
		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipo(evento.getId());
		if (notificacao == null) {
			notificacao = new Notificacao();
			notificacao.setDescricao("Evendo S1040 com pendência de envio.");
			notificacao.setData(new Date());
			notificacao.setTipo(TipoNotificacao.N);
			notificacao.setEvento(evento);
			notificacao.setReferencia(entidade.getReferenciaESocial());
		} else {
			notificacao.setData(new Date());
		}

		this.notificacaoService.salvar(notificacao);

	}

	private void validaCamposObrigatorios(RepresentacaoCargo entidade) {
		if (entidade.getCodFuncao().toUpperCase().indexOf("ESOCIAL") == 0) {
			throw new SRHRuntimeException("O código não pode ter eSocial nos sete primeiros caracteres.");
		}

	}

	@Override
	@Transactional
	public void excluir(RepresentacaoCargo entidade) {
		dao.excluir(entidade);
	}

	@Override
	public int count(String nomenclatura) {
		return dao.count(nomenclatura);
	}

	@Override
	public List<RepresentacaoCargo> search(String nomenclatura, int first, int rows) {
		return dao.search(nomenclatura, first, rows);
	}

	@Override
	public List<RepresentacaoCargo> findAll() {
		return dao.findAll();
	}

	/**
	 * Regra de Negocio:
	 * 
	 * Verifica na base se nao existe cargo comissionado cadastrado com a mesma
	 * NOMENCLATURA e SIMBOLO
	 * 
	 * @throws SRHRuntimeException
	 * 
	 */
	@SuppressWarnings("unused")
	private void verificandoSeEntidadeExiste(RepresentacaoCargo entidade) throws SRHRuntimeException {

		RepresentacaoCargo entidadeJaExiste = dao.getByNomenclaturaSimbolo(entidade.getNomenclatura(),
				entidade.getSimbolo());
		if (entidadeJaExiste != null && !entidade.getId().equals(entidadeJaExiste.getId()))
			throw new SRHRuntimeException("Nomenclatura e Simbolo já cadastrada. Operação cancelada.");

	}

	public void setDAO(RepresentacaoCargoDAO dao) {
		this.dao = dao;
	}

}
