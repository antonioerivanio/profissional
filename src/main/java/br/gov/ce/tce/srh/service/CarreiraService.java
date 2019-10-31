package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.CarreiraDAO;
import br.gov.ce.tce.srh.domain.Carreira;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("carreiraService")
public class CarreiraService{

	@Autowired
	private CarreiraDAO dao;
	
	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;

	@Transactional
	public Carreira salvar(Carreira entidade) {		
		
		// TODO Validações		
		
		validaCamposObrigatorios(entidade);
		
		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCodigo());
		vigencia.setTipoEvento(TipoEventoESocial.S1035);
		esocialEventoVigenciaService.salvar(vigencia);
		
		return dao.salvar(entidade);
	}
	
	private void validaCamposObrigatorios(Carreira entidade) {
		// TODO Auto-generated method stub
		if (entidade.getCodigo().toUpperCase().indexOf("ESOCIAL") == 0) {
			throw new SRHRuntimeException("O código não pode ter eSocial nos sete primeiros caracteres.");
		}
		
	}

	@Transactional
	public void excluir(Carreira entidade) {
		dao.excluir(entidade);
	}

	public Carreira getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<Carreira> search(String descricao, Integer first, Integer rows) {
		return dao.search(descricao, first, rows);
	}

}
