package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.GradeHorarioDAO;
import br.gov.ce.tce.srh.domain.ESocialEventoVigencia;
import br.gov.ce.tce.srh.domain.GradeHorario;
import br.gov.ce.tce.srh.enums.TipoEventoESocial;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

@Service("gradeHorarioService")
public class GradeHorarioService {

	@Autowired
	private GradeHorarioDAO dao;
	
	@Autowired
	private ESocialEventoVigenciaService esocialEventoVigenciaService;
	
	@Transactional
	public GradeHorario salvar(GradeHorario entidade) {		
		
		// TODO Validações		
		
		validaCamposObrigatorios(entidade);
		
		ESocialEventoVigencia vigencia = entidade.getEsocialVigencia();
		vigencia.setReferencia(entidade.getCodigo());
		vigencia.setTipoEvento(TipoEventoESocial.S1020);
		esocialEventoVigenciaService.salvar(vigencia);
		
		return dao.salvar(entidade);
	}
	
	private void validaCamposObrigatorios(GradeHorario entidade) {
		if (entidade.getCodigo().toUpperCase().indexOf("ESOCIAL") == 0) {
			throw new SRHRuntimeException("O código não pode ter eSocial nos sete primeiros caracteres.");
		}		
	}
	
	@Transactional
	public void excluir(GradeHorario entidade) {
		dao.excluir(entidade);
	}

	public GradeHorario findById(Long id) {
		return dao.findById(id);
	}
	
	public List<GradeHorario> findAll() {
		return dao.findAll();
	}
	
}
