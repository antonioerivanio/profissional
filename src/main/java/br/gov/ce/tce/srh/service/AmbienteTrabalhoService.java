package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.AmbienteTrabalhoDAO;
import br.gov.ce.tce.srh.domain.AmbienteTrabalho;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("ambienteTrabalhoService")
public class AmbienteTrabalhoService{

	@Autowired
	private AmbienteTrabalhoDAO dao;
	
	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;

	@Transactional
	public AmbienteTrabalho salvar(AmbienteTrabalho entidade) {
		// TODO Validações
		
		validaCamposObrigatorios(entidade);
		
		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCodigo());
		vigencia.setTipoEvento(TipoEventoESocial.S1035);
		esocialEventoVigenciaService.salvar(vigencia);
		
		return dao.salvar(entidade);
	}
	
	private void validaCamposObrigatorios(AmbienteTrabalho entidade) {
		// TODO Auto-generated method stub
		if (entidade.getCodigo().toLowerCase().indexOf("ESOCIAL") == 0) {
			throw new SRHRuntimeException("O código não pode ter eSocial nos sete primeiros caracteres.");
		}
		
	}

	@Transactional
	public void excluir(AmbienteTrabalho entidade) {
		dao.excluir(entidade);
	}	

	public AmbienteTrabalho getById(Long id) {
		return dao.getById(id);
	}	
	
	public List<AmbienteTrabalho> search(String descricao, Integer first, Integer rows) {
		return dao.search(descricao, first, rows);
	}

}
