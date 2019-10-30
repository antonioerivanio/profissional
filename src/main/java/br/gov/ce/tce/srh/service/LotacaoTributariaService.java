package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.LotacaoTributariaDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.LotacaoTributaria;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;

@Service("lotacaoTributariaService")
public class LotacaoTributariaService {

	@Autowired
	private LotacaoTributariaDAO dao;
	
	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;
	
	@Transactional
	public LotacaoTributaria salvar(LotacaoTributaria entidade) {		
		
		// TODO Validações		
		
		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCodigo());
		vigencia.setTipoEvento(TipoEventoESocial.S1020);
		esocialEventoVigenciaService.salvar(vigencia);
		
		return dao.salvar(entidade);
	}
	
	@Transactional
	public void excluir(LotacaoTributaria entidade) {
		dao.excluir(entidade);
	}

	public LotacaoTributaria findById(Long id) {
		return dao.findById(id);
	}
	
	public List<LotacaoTributaria> findAll() {
		return dao.findAll();
	}
	
}
