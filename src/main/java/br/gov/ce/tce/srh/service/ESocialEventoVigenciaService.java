package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.ESocialEventoVigenciaDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service
public class ESocialEventoVigenciaService {
	
	@Autowired
	private ESocialEventoVigenciaDAO vigenciaDao;

	@Transactional
	public ESocialEventoVigencia salvar(ESocialEventoVigencia entidade) {			
		validarDadosObrigatorios(entidade);
		validarFlagTransmissao(entidade);
		validarPeriodoVigencia(entidade);
		return vigenciaDao.salvar(entidade);		
	}

	private void validarPeriodoVigencia(ESocialEventoVigencia entidade) {		
		List<ESocialEventoVigencia> vigencias = vigenciaDao.findByReferenciaAndTipoEvento(entidade.getReferencia(), entidade.getTipoEvento());
		for (ESocialEventoVigencia vigencia : vigencias) {
			if (entidade.getId() == null || !entidade.getId().equals(vigencia.getId())) {				
				if(entidade.getInicioValidade() != null && entidade.getInicioValidade().equals(vigencia.getInicioValidade())) {
					throw new SRHRuntimeException("O início da vigência informada está sendo usado em outro cadastro de referência " + entidade.getReferencia() + ".");
				}
			}
		}		
	}

	private void validarDadosObrigatorios(ESocialEventoVigencia entidade) {		
		if(entidade.getFimValidade() != null && entidade.getInicioValidade() == null) {
			throw new SRHRuntimeException("Informe o início do período de vigência.");
		}
		if(entidade.getFimNovaValidade() != null && entidade.getInicioNovaValidade() == null) {
			throw new SRHRuntimeException("Informe o início da nova validade da vigência.");
		}
		if(entidade.getReferencia() == null || entidade.getReferencia().isEmpty()) {			
			throw new SRHRuntimeException("É obrigatório informar uma referência no cadastra de vigência para o eSocial.");
		}
		if(entidade.getTipoEvento() == null) {			
			throw new SRHRuntimeException("É obrigatório informar um tipo de evento no cadastra de vigência para o eSocial.");
		}		
	}

	private void validarFlagTransmissao(ESocialEventoVigencia entidade) {
		if (entidade.getId() != null) {
			ESocialEventoVigencia entidadeNoBanco = vigenciaDao.findById(entidade.getId());
			
			if (entidadeNoBanco != null && !entidade.equals(entidadeNoBanco)) {
				entidade.setTransmitido(false);
			}			
		}
	}
	
	public ESocialEventoVigencia findById(Long id) {
		return vigenciaDao.findById(id);
	}

}
