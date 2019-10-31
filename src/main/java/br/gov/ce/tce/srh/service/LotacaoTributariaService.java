package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.LotacaoTributariaDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.LotacaoTributaria;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.enums.TipoLotacaoTributaria;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("lotacaoTributariaService")
public class LotacaoTributariaService {

	@Autowired
	private LotacaoTributariaDAO dao;
	
	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;
	
	@Transactional
	public LotacaoTributaria salvar(LotacaoTributaria entidade) {		
		
		// TODO Validações		
		
		validaCamposObrigatorios(entidade);
		
		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCodigo());
		vigencia.setTipoEvento(TipoEventoESocial.S1020);
		esocialEventoVigenciaService.salvar(vigencia);
		
		return dao.salvar(entidade);
	}
	
	private void validaCamposObrigatorios(LotacaoTributaria entidade) {
		if (entidade.getCodigo().toUpperCase().indexOf("ESOCIAL") == 0) {
			throw new SRHRuntimeException("O código não pode ter eSocial nos sete primeiros caracteres.");
		}
		if (entidade.getTipoInscricao() != null && entidade.getTipoLotacao() == TipoLotacaoTributaria.TLT01) {
			throw new SRHRuntimeException("O campo tipo de inscrição só é necessário quando o tipo de lotação for " + TipoLotacaoTributaria.TLT04.getSimplificado());
		}
		
		
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
