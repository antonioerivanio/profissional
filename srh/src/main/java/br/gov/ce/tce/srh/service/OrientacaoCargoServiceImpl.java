package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.OrientacaoCargoDAO;
import br.gov.ce.tce.srh.domain.Especialidade;
import br.gov.ce.tce.srh.domain.OrientacaoCargo;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("orientacaoCargoService")
public class OrientacaoCargoServiceImpl implements OrientacaoCargoService {

	@Autowired
	private OrientacaoCargoDAO dao;

	@Override
	public int count(String descricao) {
		return dao.count(descricao);
	}

	@Override
	public List<OrientacaoCargo> search(String descricao, int first, int rows) {
		return dao.search(descricao, first, rows);
	}
	
	@Override
	@Transactional
	public void salvar(OrientacaoCargo entidade) throws SRHRuntimeException {
		dao.salvar(entidade);
	}

	@Override
	@Transactional
	public void excluir(OrientacaoCargo entidade) {
		dao.excluir(entidade);
	}
	
	@Override
	public List<Especialidade> findAll() {
		return dao.findAll();
	}
	
	@Override
	public List<OrientacaoCargo> findByEspecialidade(Long especialidade) {
		return dao.findByEspecialidade(especialidade);
	}

	public void setDAO(OrientacaoCargoDAO dao) {this.dao = dao;}

}
