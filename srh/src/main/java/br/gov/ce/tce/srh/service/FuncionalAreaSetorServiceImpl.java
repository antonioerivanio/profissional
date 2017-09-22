package br.gov.ce.tce.srh.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.ce.tce.srh.dao.FuncionalAreaSetorDAO;
import br.gov.ce.tce.srh.domain.FuncionalAreaSetor;
import br.gov.ce.tce.srh.exception.SRHRuntimeException;

/**
 * 
 * @author robstown
 *
 */
@Service("funcionalAreaSetorService")
public class FuncionalAreaSetorServiceImpl implements FuncionalAreaSetorService {

	@Autowired
	private FuncionalAreaSetorDAO dao;


	@Override
	@Transactional
	public void salvar(FuncionalAreaSetor entidade) throws SRHRuntimeException {

		// validando dados obrigatorios.
		validarDados(entidade);

		// persistindo
		dao.salvar(entidade);
	}

	
	@Override
	@Transactional
	public void excluir(FuncionalAreaSetor entidade) throws SRHRuntimeException {
		dao.excluir(entidade);
	}


	@Override
	public int count(Long funcional) {
		return dao.count(funcional);
	}


	@Override
	public List<FuncionalAreaSetor> search(Long funcional, int first, int rows) {
		return dao.search(funcional, first, rows);
	}


	@Override
	public List<FuncionalAreaSetor> findyByAreaSetor(Long areaSetor) {
		return dao.findyByAreaSetor(areaSetor);
	}


	@Override
	public List<FuncionalAreaSetor> findByFuncional(Long funcional) {
		return dao.findByFuncional(funcional);
	}
	
	@Override
	public List<FuncionalAreaSetor> findByFuncionalComSetorAtual(Long funcional) {
		return dao.findByFuncionalComSetorAtual(funcional);
	}


	/**
	 * Validar:
	 * 
	 *  Deve ser setado a pessoa (servidor).
	 *  Deve ser setado a area.
	 * 
	 * @param entidade
	 * 
	 * @throws SRHRuntimeException 
	 *  
	 */
	private void validarDados(FuncionalAreaSetor entidade) {

		if (entidade.getPk().getFuncional() == null || entidade.getPk().getFuncional().equals(0l)) {
			throw new SRHRuntimeException("Funcionario é obrigatório.");
		}

		if (entidade.getPk().getAreaSetor() == null || entidade.getPk().getAreaSetor().equals(0l)) {
			throw new SRHRuntimeException("Área é obrigatória.");
		}

	}


	public void setDAO(FuncionalAreaSetorDAO dao) {this.dao = dao;}
}
