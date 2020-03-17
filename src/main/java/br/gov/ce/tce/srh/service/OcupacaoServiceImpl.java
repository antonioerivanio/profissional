package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.OcupacaoDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.EspecialidadeCargo;
import br.gov.ce.tce.srh.domain.Ocupacao;
import br.gov.ce.tce.srh.domain.Simbolo;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("ocupacaoService")
public class OcupacaoServiceImpl implements OcupacaoService {

	@Autowired
	private OcupacaoDAO dao;

	@Autowired
	private SimboloService simboloService;

	@Autowired
	private EspecialidadeCargoService especialidadeCargoService;
	
	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;
	
//	@Autowired
//	private EventoService eventoService;
//	
//	@Autowired
//	private NotificacaoService notificacaoService;

	@Override
	@Transactional
	public Ocupacao salvar(Ocupacao entidade) {
		return dao.salvar(entidade);
	}	

	@Override
	@Transactional
	public Ocupacao salvar(Ocupacao entidade, List<EspecialidadeCargo> especialidades, List<Simbolo> simbologias) throws SRHRuntimeException {

		//verificandoSeEntidadeExiste(entidade);
		
		validaCamposObrigatorios(entidade);
		
		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCodigoEsocial());
		vigencia.setTipoEvento(TipoEventoESocial.S1030);
		esocialEventoVigenciaService.salvar(vigencia);

		entidade = dao.salvar(entidade);
		
		for( Simbolo simbolo : simbologias ) {
			if (simbolo.getId() == null) {
				simbolo.setOcupacao(entidade);
				simboloService.salvar(simbolo);	
			}
		}

		for( EspecialidadeCargo especialidade : especialidades ) {
			if (especialidade.getId() == null) {
				especialidade.setOcupacao(entidade);
				especialidadeCargoService.salvar(especialidade);	
			}
		}
		
		// salvando notificação
//		Evento evento = this.eventoService.getById(TipoEventoESocial.S1030.getCodigo());
//		Notificacao notificacao = this.notificacaoService.findByEventoIdAndTipoAndReferencia(evento.getId(), entidade.getReferenciaESocial()); 
//		if (notificacao == null) {
//			notificacao = new Notificacao();
//			notificacao.setDescricao("Evento S1030 com pendência de envio.");
//			notificacao.setData(new Date());
//			notificacao.setTipo(TipoNotificacao.N);
//			notificacao.setEvento(evento);
//			notificacao.setReferencia(entidade.getReferenciaESocial());
//		} else {
//			notificacao.setData(new Date());
//		}
//		
//		this.notificacaoService.salvar(notificacao);
		
		return entidade;

	}


	private void validaCamposObrigatorios(Ocupacao entidade) {
		if (entidade.getCodigoEsocial().toUpperCase().indexOf("ESOCIAL") == 0) {
			throw new SRHRuntimeException("O código não pode ter eSocial nos sete primeiros caracteres.");
		}
		
	}

	@Override
	@Transactional
	public void excluir(Ocupacao entidade) {

		// excluindo todas as simbologias da ocupacao
		simboloService.excluirAll(entidade.getId());

		// excluindo todas as especialidade da ocupacao
		especialidadeCargoService.excluirAll(entidade.getId());

		// excluindo a ocupacao
		dao.excluir(entidade);
	}

	
	@Override
	public int count(String nomenclatura) {
		return dao.count(nomenclatura);
	}


	@Override
	public int count(String nomenclatura, Long situacao) {
		return dao.count(nomenclatura, situacao);
	}

	
	@Override
	public List<Ocupacao> search(String nomenclatura, int first, int rows) {
		return dao.search(nomenclatura, first, rows);
	}


	@Override
	public List<Ocupacao> search(String nomenclatura, Long situacao, int first, int rows) {
		return dao.search(nomenclatura, situacao, first, rows);
	}


	@Override
	public Ocupacao getById(Long idOcupacao) {
		return dao.getById(idOcupacao);
	}


	@Override
	public List<Ocupacao> findAll() {
		return dao.findAll();
	}


	@Override
	public List<Ocupacao> findByTipoOcupacao(Long tipoOcupacao) {
		return dao.findByTipoOcupacao(tipoOcupacao);
	}

	@Override
	public List<Ocupacao> findByPessoa(Long idPessoal) {
		return dao.findByPessoa(idPessoal);
	}

	
	/**
	 * Regra de Negocio: 
	 * 
	 * Verifica na base se nao existe entidade cadastrada com a mesma NOMENCLATURA.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException
	 *  
	 */
	@SuppressWarnings("unused")
	private void verificandoSeEntidadeExiste(Ocupacao entidade) throws SRHRuntimeException {

		Ocupacao entidadeJaExistente = dao.findByNomenclaturaAndTipoOcupacaoAndSituacao(
				entidade.getNomenclatura(), 
				entidade.getTipoOcupacao().getId(), 
				entidade.getSituacao());
		
			
		if(entidadeJaExistente != null 
				&& (entidade.getId() == null || !entidadeJaExistente.getId().equals(entidade.getId()))) {
			throw new SRHRuntimeException("Cargo já cadastrado. Operação cancelada.");
		}					
		
	}


	public void setDAO(OcupacaoDAO dao) {this.dao = dao;}
	public void setSimboloService(SimboloService simboloService) {this.simboloService = simboloService;}
	public void setEspecialidadeCargoService(EspecialidadeCargoService especialidadeCargoService) {this.especialidadeCargoService = especialidadeCargoService;}

}
