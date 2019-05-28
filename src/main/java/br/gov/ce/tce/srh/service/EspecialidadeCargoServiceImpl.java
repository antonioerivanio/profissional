package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.EspecialidadeCargoDAO;
import br.gov.ce.tce.srh.domain.EspecialidadeCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("especialidadeCargoService")
public class EspecialidadeCargoServiceImpl implements EspecialidadeCargoService {

	@Autowired
	private EspecialidadeCargoDAO dao;


	@Override
	@Transactional
	public void salvar(EspecialidadeCargo entidade) throws SRHRuntimeException {
		dao.salvar(entidade);
	}


	@Override
	@Transactional
	public void excluir(EspecialidadeCargo entidade) {
		dao.excluir(entidade);
	}


	@Override
	@Transactional
	public void excluirAll(Long ocupacao) {
		dao.excluirAll(ocupacao);
	}


	@Override
	public List<EspecialidadeCargo> findByOcupacao(Long ocupacao) {
		return dao.findByOcupacao(ocupacao);
	}
	
	
	@Override
	public EspecialidadeCargo getById(Long id) {
		return dao.getById(id);
	}


	public void setDAO(EspecialidadeCargoDAO dao) {this.dao = dao;}

}
