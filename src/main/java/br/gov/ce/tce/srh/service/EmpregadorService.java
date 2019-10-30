package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.EmpregadorDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.Empregador;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;

@Service("empregadorService")
public class EmpregadorService {
	
	@Autowired
	private EmpregadorDAO dao;
	
	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;
	
	@Transactional
	public Empregador salvar(Empregador entidade) {		
		
		// TODO Validações		
		
		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCnpj());
		vigencia.setTipoEvento(TipoEventoESocial.S1000);
		esocialEventoVigenciaService.salvar(vigencia);
		
		return dao.salvar(entidade);
	}
	
	@Transactional
	public void excluir(Empregador entidade) {
		dao.excluir(entidade);
	}

	public Empregador findById(Long id) {
		return dao.findById(id);
	}
	
	public List<Empregador> findAll() {
		return dao.findAll();
	}

}
