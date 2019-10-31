package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.RubricaESocialTCEDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.RubricaESocialTCE;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("rubricaESocialTCEService")
public class RubricaESocialTCEService{

	@Autowired
	private RubricaESocialTCEDAO dao;
	
	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;

	@Transactional
	public RubricaESocialTCE salvar(RubricaESocialTCE entidade) {		
		
		validaCamposObrigatorios(entidade);
		
		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCodigo());
		vigencia.setTipoEvento(TipoEventoESocial.S1010);
		esocialEventoVigenciaService.salvar(vigencia);
		
		return dao.salvar(entidade);
	}
	
	private void validaCamposObrigatorios(RubricaESocialTCE entidade) {		
		if (entidade.getCodigo().toUpperCase().indexOf("ESOCIAL") == 0) {
			throw new SRHRuntimeException("O código não pode ter eSocial nos sete primeiros caracteres.");
		}
		
	}

	@Transactional
	public void excluir(RubricaESocialTCE entidade) {
		dao.excluir(entidade);
	}	

	public RubricaESocialTCE getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<RubricaESocialTCE> search(String descricao, Integer first, Integer rows) {
		return dao.search(descricao, first, rows);
	}

}
