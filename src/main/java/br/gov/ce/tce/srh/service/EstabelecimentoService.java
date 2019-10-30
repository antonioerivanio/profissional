package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.EstabelecimentoDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.Estabelecimento;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;

@Service("estabelecimentoService")
public class EstabelecimentoService {
	
	@Autowired
	private EstabelecimentoDAO dao;
	
	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;
	
	@Transactional
	public Estabelecimento salvar(Estabelecimento entidade) {		
		
		// TODO Validações		
		
		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getNumeroInscricao());
		vigencia.setTipoEvento(TipoEventoESocial.S1005);
		esocialEventoVigenciaService.salvar(vigencia);
		
		return dao.salvar(entidade);
	}
	
	@Transactional
	public void excluir(Estabelecimento entidade) {
		dao.excluir(entidade);
	}

	public Estabelecimento findById(Long id) {
		return dao.findById(id);
	}
	
	public List<Estabelecimento> findAll() {
		return dao.findAll();
	}

}
